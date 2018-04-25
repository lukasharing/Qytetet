#include<iostream>
#include<utility>
#include<cmath>
#include <ctime>

std::pair<int, int> Mayor_Frecuencia(int* set, int n){
  int* ht0 = new int[n];
  int* ht1 = new int[n];
  int h0 = 0, h1 = 0, m = 1;
  int p = set[0];
  // Si el tamaño es < 1
  if(n <= 1){
    return std::pair<int, int>(-1, 0);
  }
  // Metemos según la homogeneidad de los elementos.
  for(int i = 1; i < n; ++i){
    int df = p - set[i];
    if(df > 0){
      ht0[h0] = set[i];
      ++h0;
    }else if(df < 0){
      ht1[h1] = set[i];
      ++h1;
    }else{
      ++m;
    }
  }
  std::pair<int, int> v0 = Mayor_Frecuencia(ht0, h0);
  std::pair<int, int> v1 = Mayor_Frecuencia(ht1, h1);
  delete[] ht0;
  delete[] ht1;

  // Buscamos en los dos conjuntos heterogéneos (Aquel mayor).
  if(m > v0.first && m > v1.first){
    return std::pair<int, int>(n, p);
  }else if(v0.first > m && v0.first > v1.first){
    return v0;
  }else{
    return v1;
  }
}

const int NUM_ITERATION = 1000;
int main(int total, char* str[]){
  if(total != 2){
    return -1;
  }

  int n = atoi(str[1]);
  int* a = new int[n];

  // Llenamos el vector de números pseudo-aleatorios
  for(int i = 0; i < n; ++i){
    a[i] = (i * 747 + 113) % 503;
  }

  clock_t t_antes = clock();
  std::pair<int, int> c;
  for(int k = 0; k < NUM_ITERATION; ++k){
    c = Mayor_Frecuencia(a, n);
  }
  clock_t t_despues = clock();

  // Mostramos el tiempo de cómputo
  double dt = t_despues - t_antes;
  double ciclos = CLOCKS_PER_SEC * NUM_ITERATION;
  std::cout << n << " \t  " << dt / ciclos << std::endl;

  // Borramos de la memoria.
  delete[] a;

  return 0;
}
