#include <iostream>
#include "../include/permutacion.h"
#include <string>
#include <ctime>
#include <stdlib.h>     /* srand, rand */
#include <vector>

int factorial(int k){
  return (k < 1) ? 1 : (k * factorial(k - 1));
}

int main(int argc, char * argv[]){
  int n = atoi(argv[1]);
  int total = factorial(n);

  vector<unsigned int> akk = vector<unsigned int>(n);
  cout << akk.size();

  srand(time(0));
  for(int k = 0; k < total; ++k){
    akk[k] = rand();
  }

  // Permutacion permutacion(n);
  // Permutacion::iterator it;
  // for(it = permutacion.begin(); it != permutacion.end(); ++it){
  //   vector<unsigned int>::const_iterator ti;
  //   for(ti = (*it).begin(); ti < (*it).end(); ++ti){
  //     cout << ((*ti) - 1) << ' ';
  //   }
  //   cout << '\n';
  // }
}
