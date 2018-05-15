#include<iostream>
#include<cstdlib>
#include<fstream>
#include<vector>

void secuencia_suma(int valor, const std::vector<int>& open, std::vector<int>& close){
  int total = 1 << open.size();
  bool found = false;
  int i_lowest = 0, n_nearest = valor;
  for(int i = 0; i < total && !found; i += 1){
    int sum = 0;
    bool supera = false;
    for(int k = i, p = 0; k > 0 && !supera; k >>= 1, ++p){
      sum += open[p] * (k & 1);
      if(sum > valor){ supera = true; }
    }
    if(sum == valor){
      i_lowest = i;
      found = true;
    }else if(n_nearest < sum){
      n_nearest = sum;
      i_lowest = i;
    }
  }

  int p = 0, j = -1;
  std::cout << i_lowest << std::endl;
  for(int k = i_lowest; k > 0; k >>= 1, ++p){
    if(k & 1){
      close[++j] = open[p];
    }
  }
  close.resize(j + 1);
}

int main(int n_args, char* args[]){
  if(n_args != 3){
    std::cout << "Error: numero de argumentos incorrecto." << std::endl;
    return -1;
  }
  int sumando = atoi(args[2]);

  std::ifstream file;
  file.open(args[1]);
  if(file.is_open()){
    std::vector<int> open;
    int n;
    while(file >> n){
      open.push_back(n);
    }
    std::vector<int> close(open.size());

    secuencia_suma(sumando, open, close);
    if(close.size() > 0){
      std::cout << sumando << " ~ ";
      for(int i = 0; i < close.size() - 1; ++i){
        std::cout << close.at(i) << " + ";
      }
       std::cout << close.back() << std::endl;
    }else{
      std::cout << "Error: No hay solucion." << std::endl;
    }
  }else{
    std::cout << "Error: No se pudo abrir el archivo." << std::endl;
    return -1;
  }
  return 1;
}
