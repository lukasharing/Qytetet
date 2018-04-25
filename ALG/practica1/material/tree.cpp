#include "./abb/abb.h"
#include <stdlib.h>     /* srand, rand */
#include <iostream>
#include <ctime>
#include <cstdlib>
#include <climits>
#include <cassert>

using namespace std;



int main(int argc, char * argv[])
{
  int n = atoi(argv[1]);
	int* A = new int[n];
	ABB<int> ab_bus;
  srand(time(0));
	// Introducimos
  for (int i = 0; i < n; i++){
		ab_bus.Insertar(rand());
  }

  ABB<int>::nodo k;
	int m = 0;
  clock_t t_antes = clock();
	for (k = ab_bus.begin(); k != ab_bus.end(); ++k){
		A[m++] = *k; // Generar código O(1) que el compilador no va a eliminar / desenrrollar.
  }
  clock_t t_despues = clock();
	cout << n << "  " << ((double)(t_despues - t_antes)) / CLOCKS_PER_SEC << endl;

  return 0;
};
