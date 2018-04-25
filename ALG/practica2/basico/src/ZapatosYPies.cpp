#include<iostream>
#include<utility>
#include <ctime>

// Realiza el swap de dos valores en un vector.
void swap(int* v, int i, int j){
  int h = *(v + i);
  *(v + i) = *(v + j);
  *(v + j) = h;
}

void shift(int* v, int i, int n, int k){
  for(int j = n - 1; j > i; --j){
    *(v + j) = *(v + j - 1);
  }
  *(v + i) = k;
}

void Zapatos_Pies(int* shoes, int* toes, int n){
  if(n <= 1){
    return;
  }

  // Pivote (i) para los pies del ultimo zapato
  int i = 0;
  for(i = 0; *(toes + i) != *(shoes + n - 1); ++i);
  // Hacemos swap del pie pivote con el último
  swap(toes, i, n - 1);

  int m, w;
  /* MOVEMOS LOS PIES */
  m = -1;
  w = 0;
  while(w < n){
    // Comparamos el elemento con el pivote de los pies
    // Si este es menor, intercambiamos el muro con el actual.
    if(*(toes + w) < *(shoes + n - 1)){
      ++m;
      swap(toes, m, w);
    }
    ++w;
  }
  // Movemos los elementos a la derecha.
  shift(toes, ++m, n, *(shoes + n - 1));


  /* MOVEMOS LOS ZAPATOS */
  m = -1;
  w = 0;
  while(w < n){
    // Comparamos el elemento con el pivote de los pies
    // Si este es menor, intercambiamos el muro con el actual.
    if(*(shoes + w) < *(toes + n - 1)){
      ++m;
      swap(shoes, m, w);
    }
    ++w;
  }
  // Movemos los elementos a la derecha.
  shift(shoes, ++m, n, *(toes + n - 1));

  // Ahora los nuevos pivotes son m0, m1
  // Repetimos el algoritmo
  Zapatos_Pies(shoes, toes, m);
  Zapatos_Pies(shoes + m, toes, n - m);
}

const int NUM_ITERATION = 10;
int main(int total, char* str[]){
  if(total != 2){
   return -1;
  }

  int n = atoi(str[1]);

  // Creamos un buffer para luego solo tener que copiarlo (Por eficiencia).
  int copy[n];
  for(int j = 0; j < n; ++j){
    int m = j;
    copy[j] = m;
  }

  double dt = 0;
  for(int k = 0; k < NUM_ITERATION; ++k){
    int a[n];
    int b[n];
    for(int j = 0; j < n; ++j){
      a[j] = copy[j];
      b[(j + n/2) % n] = copy[j];
    }

    clock_t t_antes = clock();
    Zapatos_Pies(a, b, n);
    clock_t t_despues = clock();
    dt += (t_despues - t_antes);
  }
  // Mostramos el tiempo de cómputo
  dt /= CLOCKS_PER_SEC * NUM_ITERATION;
  std::cout << n << " \t  " << dt << std::endl;

  return 0;
}
