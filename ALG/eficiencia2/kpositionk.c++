#include<iostream>

int* mayor_serie(int* v, int a, int b){
  int diff = b - a + 1;
  int* res = NULL;
  std::cout << diff << std::endl;
  switch(diff){
    case 1: res = new int[1]{ v[a] }; break;
    case 2:
      if(v[a] >= v[b]){
        res = new int[2]{ v[a], v[b] };
      }
    break;
    default:
      int m = (a + b) / 2;

      int* s1 = mayor_serie(v, a, m - 1);
      int* s2 = mayor_serie(v, m, b);

      if(s1 != NULL && s2 != NULL){
        if(s1[m-2] <= s2[0]){
          res = new int[diff];
          for(int i = 0; i < (m - 1 - a); ++i){
            res[i] = s1[i];
          }
          for(int i = 0; i < (b - m); ++i){
            res[m + i] = s2[i];
          }
        }
        delete[] s1, s2;
      }else if(s1 != NULL){
        delete[] s2;
        res = s1;
      }else if(s2 != NULL){
        delete[] s1;
        res = s2;
      }
    break;
  }
  return res;
}

int main(){
  int v[5] = {0, 2, 3, 5, 1};
  int* p = mayor_serie(v, 0, 4);
  for(int i = 0; i < 4; ++i){
    	std::cout << i << "=>" << p[i] << std::endl;
  }
  return 1;
}
