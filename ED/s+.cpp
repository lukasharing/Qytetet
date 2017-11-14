#include<iostream>
#include<string>
#include<queue>
using namespace std;

const int ROWS = 5, COLS = 5;
const char matrix[ROWS][COLS] = {{'1', '1', 'E', '1', '1'},
                                {'0', '0', '0', '1', '0'},
                                {'0', '1', '1', '0', '1'},
                                {'0', '1', '0', '0', '0'},
                                {'0', '1', 'S', '1', '1'}};
bool in_matrix(int i, int j){ return  ((i >= 0 && j >= 0) && (i < ROWS && j < COLS)); }
bool existe_recorrido(const char labyrinth[ROWS][COLS]){
  //Buscamos la entrada.
  std::queue<int> cola;
  // O(n^2)
  for(int j = 0; j < COLS && cola.empty(); j++){
    for(int i = 0; i < ROWS && cola.empty(); i++){
      if(labyrinth[j][i] == 'E'){
        cola.push(i + j * ROWS);
      }
    }
  }

  // O(n^2).
  bool exist = false;
  while(!cola.empty() && !exist){
    int i = cola.front() % ROWS, j = cola.front() / ROWS;

    int path_splits = -1;
    for(int k = 1; k <= 7; k += 2){
      int p = (k / 3) % 2; // 0 -> 0, 3 -> 1, 5 -> 1, 7-> 0
      int q = (1 - 2 * (k >> 1 & 1)); // Map [-1, 1] depending on the 2 bit
      int ii = i + p * q;
      int jj = j + (1 - p) * q;
      if(in_matrix(ii, jj) && matrix[jj][ii] == '0'){
        path_splits++;
      }
    }
    cout << "->" << path_splits << endl;
    cola.pop();
  }
  return exist;
}

int main(){
  cout << existe_recorrido(matrix);
}
