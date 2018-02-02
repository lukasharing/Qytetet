#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>
#include <time.h>
#include <signal.h>
#include <errno.h>
#include <unistd.h>
#include <sys/wait.h>

#define TAM 1024
#define NOM 256

int n_hijos, no_abortado;

void handler(int sigNum)
{
  if (sigNum == SIGCONT)
  {
    printf("\nServidor: señal de apagado recibida.");
    no_abortado = 0;
  }
  else if (sigNum == SIGCHLD)
  {
    if (wait(NULL) < 0)
    {
      perror("\nServidor: error en el wait del manejador.");
      exit(EXIT_FAILURE);
    }

    n_hijos--;
  }
  else
  {
    perror("\nServidor: señal no válida.");
    exit(EXIT_FAILURE);
  }
}

int main(int argc, char* argv[])
{
  int fd_entrada, fd_salida, fd_proxy, fd_bloqueo;
  char buffer[TAM], fifo_entrada[NOM], fifo_salida[NOM], fifo_proxy[NOM];
  int leidos;
  struct sigaction manejador;
  pid_t pid;

  if ((fd_bloqueo = open("bloqueo", O_CREAT|O_TRUNC|O_WRONLY, S_IRWXU)) < 0)
  {
    perror("\nServidor: error al crear el archivo de bloqueo.");
    exit(EXIT_FAILURE);
  }

  close(fd_bloqueo);

  sprintf(fifo_entrada, "%se", argv[1]);
  sprintf(fifo_salida, "%ss", argv[1]);

  unlink(fifo_salida);
  unlink(fifo_entrada);

  if (mkfifo(fifo_entrada, S_IRWXU) < 0)
  {
    perror("\nServidor: error al crear el fifo conocido de entrada.");
    exit(EXIT_FAILURE);
  }

  if (mkfifo(fifo_salida, S_IRWXU) < 0) {
    perror("\nServidor: error al crear el fifo conocido de salida.");
    exit(EXIT_FAILURE);
  }

  if ((fd_entrada = open(fifo_entrada, O_RDWR)) < 0)
  {
    perror("\nServidor: error al abrir el fifo conocido de entrada.");
    exit(EXIT_FAILURE);
  }

  if ((fd_salida = open(fifo_salida, O_RDWR)) < 0)
  {
    perror("\nServidor: error al abrir el fifo conocido de salida.");
    exit(EXIT_FAILURE);
  }

  manejador.sa_handler = handler;
  sigemptyset(&manejador.sa_mask);
  manejador.sa_flags = 0;

  if (sigaction(SIGCHLD, &manejador, NULL) < 0)
  {
    perror("\nServidor: error al configurar el maejador para SIGCHLD.");
    exit(EXIT_FAILURE);
  }

  if (sigaction(SIGCONT, &manejador, NULL) < 0)
  {
    perror("\nServidor: error al configurar el maejador para SIGCONT.");
    exit(EXIT_FAILURE);
  }

  n_hijos = 0;
  no_abortado = 1;

  while (no_abortado)
  {
    leidos = read(fd_entrada, buffer, sizeof(int));
    if (leidos < 0)
    {
      if (errno == EINTR)
        continue;
      else
      {
        perror("Servidor: error al leer del fifo conocido de entrada.");
        exit(EXIT_FAILURE);
      }
    }

    n_hijos++;

    if ((pid = fork()) < 0)
    {
      perror("\nServidor: error en fork.");
      exit(EXIT_FAILURE);
    }

    if (pid == 0)
		{
      pid = getpid();
      sprintf(fifo_proxy, "fifo.%d", pid);

      if (mkfifo(fifo_proxy, S_IRWXU) < 0)
      {
        perror("\nHijo: error al crear el fifo_proxy.");
        exit(EXIT_FAILURE);
      }

      if (write(fd_salida, &pid, sizeof(int)) < 0)
      {
        perror("\nHijo: error al escribir el PID del hijo en fifo conocido.");
        exit(EXIT_FAILURE);
      }

      if((fd_proxy = open(fifo_proxy, O_RDONLY)) < 0)
      {
        perror("\nHijo: error al abrir el fifoproxy por parte del hijo.");
        exit(EXIT_FAILURE);
      }

      close(fd_entrada);
      close(fd_salida);

      if (dup2(fd_proxy, STDIN_FILENO) < 0)
      {
        perror("\nHijo: error al redireccionar la entrada estándar del hijo.");
        exit(EXIT_FAILURE);
      }

      if (execl("./proxy","proxy", NULL) < 0)
      {
        perror("\nHijo: error en execl.");
        exit(EXIT_FAILURE);
      }
    }
  }

  while (n_hijos != 0)
  {
    if (wait(NULL) < 0)
    {
      perror("\nServidor: error en wait.");
      exit(EXIT_FAILURE);
    }

    n_hijos--;
  }

  close(fd_entrada);
  close(fd_salida);

  if (unlink(fifo_entrada) < 0)
  {
    perror("\nServidor: error al destruir el fifo de entrada.");
    exit(EXIT_FAILURE);
  }

  if (unlink(fifo_salida) < 0)
  {
    perror("\nServidor: error al destruir el fifo de salida.");
    exit(EXIT_FAILURE);
  }

  printf("\nServidor apagándose.");

  exit(EXIT_SUCCESS);
}
