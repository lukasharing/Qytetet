#include<iostream>
#include<utility>
#include<limits>
#include<cmath>
#include <ctime>

// Suponemos que n > 0 y v es cuadrada
// PAR RESULTANTE: (MAX, MIN)
std::pair<int, int> Max_Min(int** v, int ii, int jj, int n){
  std::pair<int, int> result;
  // Si hay cuatro elementos, buscamos de entre estos.
  if(n == 2){
    int max = std::numeric_limits<int>::min();
    int min = std::numeric_limits<int>::max();
    for(int j = jj; j < jj + n; ++j){
      for(int i = ii; i < ii + n; ++i){
        min = std::min(v[j][i], min);
        max = std::max(v[j][i], max);
      }
    }
    result.first = max;
    result.second = min;
    return result;
  }
  // Calculamos la la siguiente particion.
  int k = n % 2, tp = ceil(n / 2.0);
  int rr = tp - k;
  // Partición arriba izquierda.
  std::pair<int, int> tl = Max_Min(v, ii     , jj     , tp);
  // Partición arriba derecha.
  std::pair<int, int> tr = Max_Min(v, ii + rr, jj     , tp);
  // Partición abajo izquierda.
  std::pair<int, int> bl = Max_Min(v, ii     , jj + rr, tp);
  // Partición abajo derecha.
  std::pair<int, int> br = Max_Min(v, ii + rr, jj + rr, tp);

  // Si  (max0, min0), (max1, min1), (max2, min2), (max3, min3),
  // mínimo(min0, min1, min2, min3) y el máximo de (max0, max1, max2, max3).
  result.first = std::max(std::max(tl.first, tr.first),
                          std::max(bl.first, br.first));
  result.second = std::min(std::min(tl.second, tr.second),
                           std::min(bl.second, br.second));

  // Devolvemos el resultado.
  return result;
}

const int NUM_ITERATION = 10;
int main(int total, char* str[]){
  if(total != 2){
    return -1;
  }

  int n = atoi(str[1]);

  int** a = new int*[n];
  for(int i = 0; i < n; ++i){
    a[i] = new int[n];
  }

  for(int j = 0; j < n; ++j){
    for(int i = 0; i < n; ++i){
      a[j][i] = ((i * n + j) * 503 + 113) % 747;
    }
  }

  clock_t t_antes = clock();
  for(int k = 0; k < NUM_ITERATION; ++k){
    std::pair<int, int> ab = Max_Min(a, 0, 0, n);
  }
  clock_t t_despues = clock();

  // Mostramos el tiempo de cómputo
  double dt = t_despues - t_antes;
  double ciclos = CLOCKS_PER_SEC * NUM_ITERATION;
  std::cout << n << " \t  " << dt / ciclos << std::endl;

  for(int j = 0; j < n; ++j){
    delete[] a[j];
  }

  // Borramos de la memoria.
  delete[] a;

  return 0;
}
