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
#include <string.h>

#define TAM 1024
#define NOM 256

int main(int argc, char* argv[])
{
  char buffer[TAM], descriptor[NOM], mensaje[NOM];
  FILE* p_temporal;
  int leidos;
  int fd_bloqueo;
  struct flock cerrojo;

  if ((fd_bloqueo = open("bloqueo", O_WRONLY)) < 0)
  {
    perror("\nProxy: error al abrir el archivo de bloqueo");
    exit(EXIT_FAILURE);
  }

  cerrojo.l_whence = SEEK_SET;
  cerrojo.l_start = 0;
  cerrojo.l_len = 0;

  if ((p_temporal = tmpfile()) == NULL)
  {
    perror("\nProxy: error al crear el tmp.");
    exit(EXIT_FAILURE);
  }

  while ((leidos = read(STDIN_FILENO, buffer, TAM)) > 0)
  {
    if (fwrite(buffer, sizeof(char), leidos, p_temporal) != leidos)
    {
      perror("\nProxy: error al leer del fifo_proxy.");
      exit(EXIT_FAILURE);
    }
  }

  cerrojo.l_type = F_WRLCK;

  if (fcntl(fd_bloqueo, F_SETLKW, &cerrojo) < 0)
  {
    perror("\nProxy: error al colocar el cerrojo.");
    exit(EXIT_FAILURE);
  }

  sprintf(mensaje, "\nCliente %i, escribo:\n", getpid());

  if ((leidos = write(STDOUT_FILENO, mensaje, strlen(mensaje))) < 0)
  {
    perror("\nProxy: error al escribir.");
    exit(EXIT_FAILURE);
  }

  rewind(p_temporal);

  while (!feof(p_temporal))
  {
    if ((leidos = fread(buffer, sizeof(char), TAM, p_temporal)) > 0)
    {
      if (write(STDOUT_FILENO, buffer, leidos) != leidos)
      {
        perror("\nError al escribir.");
        exit(EXIT_FAILURE);
      }
    }
  }

  cerrojo.l_type = F_UNLCK;

  if (fcntl(fd_bloqueo, F_SETLKW, &cerrojo) < 0)
  {
    perror("\nProxy: error al liberar el cerrojo.");
    exit(EXIT_FAILURE);
  }

  if (close(STDIN_FILENO) < 0)
  {
    perror("\nProxy: error al cerrar el fifo del proxy.");
    exit(EXIT_FAILURE);
  }

  sprintf(descriptor, "fifo.%d", getpid());

  if (unlink(descriptor) < 0)
  {
    perror("\nProxy: error al destruir el fifo del proxy.");
    exit(EXIT_FAILURE);
  }

  printf("\nCliente %i, termino mi ejecución.\n", getpid());

  exit(EXIT_SUCCESS);
}
