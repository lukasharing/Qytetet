// AYUDITA: https://www.tutorialspoint.com/design_and_analysis_of_algorithms/design_and_analysis_of_algorithms_max_min_problem.htm

#include<iostream>
#include<utility>
#include<limits>
#include<cmath>
#include<ctime>

// Suponemos que n > 0
// PAR RESULTANTE: (MAX, MIN)
std::pair<int, int> Max_Min(int* v, int n){
  std::pair<int, int> result(*v, *v);
  // Si solo hay un elemento, entonces ese es el mínimo y máximo.
  if(n == 1){
    return result;
  }
  // Si hay dos elementos, entonces el mínimo y el máximo son ambos.
  if(n == 2){
    if(*(v + 0) < *(v + 1)){
      result.first = *(v + 1);
    }else{
      result.second = *(v + 1);
    }
    return result;
  }
  // Calculamos las particiones (Según sea par o impar).
  int bt = floor(n / 2.0), tp = ceil(n / 2.0);
  // Partición izquierda.
  std::pair<int, int> left = Max_Min(v, bt);
  // Partición derecha.
  std::pair<int, int> right = Max_Min(v + bt, tp);

  // Si  (max0, min0) y (max1, min1), buscamos
  // mínimo(min0, min1) y el máximo de (max0, max1)
  result.first = std::max(left.first, right.first);
  result.second = std::min(left.second, right.second);

  // Devolvemos el resultado.
  return result;
}

const int NUM_ITERATION = 1000;
int main(int total, char* str[]){
  if(total != 2){
    return -1;
  }

  int n = atoi(str[1]);
  int* a = new int[n];
  // Generamos números pseudo-aleatorios.
  for(int i = 0; i < n; ++i){
    a[i] = (i * 747 + 113) % 503;
  }
  clock_t t_antes = clock();
  for(int k = 0; k < NUM_ITERATION; ++k){
    auto ab = Max_Min(a, n);
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
