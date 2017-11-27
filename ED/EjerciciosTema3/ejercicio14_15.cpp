#include <iostream>
#include <string>
#include <list>
#include <algorithm>
#include <utility>
#include <vector>

template<typename T>
class vdisperso{
  private:
    std::list<typename std::pair<int, T>> values;
    T null_value;
    int virtual_size;
  public:
    vdisperso(){ virtual_size = 0; null_value = T(); };
    vdisperso(const std::vector<T>& v){
      null_value = T();
      virtual_size = v.size();
      for(int i = 0; i < virtual_size; ++i){
        if(v.at(i) != null_value){
          values.push_back(std::pair<int, T>(i, v.at(i)));
        }
      }
    };
    vdisperso(const std::vector<T>& v, const T& n){
      null_value = n;
      virtual_size = v.size();
      for(int i = 0; i < virtual_size; ++i){
        if(v.at(i) != null_value){
          values.push_back(std::pair<int, T>(i, v.at(i)));
        }
      }
    };
    void asignar_coeficiente(int i, const T& k){
      if(k != null_value){
        typename std::list<typename std::pair<int, T>>::iterator look;
        look = std::find_if(values.begin(), values.end(),
        [i](std::pair<int, T> const& e){
          return e.first == i;
        });
        if(look == values.end()){
          // Leets look who is its nearest left neighbour
          look = values.begin();
          while(look->first <= i && look != values.end()){ ++look; }
          values.insert(look, std::pair<int, T>(i, k));
        }else{
          look->second = k;
        }
      }
      if(i + 1 > virtual_size){
        virtual_size = i + 1;
      }
    };

    // Esto se puede hacer de manera similar con menos líneas de código
    // utilizando el método assing y sobrescribiendo valores ya existentes.
    std::vector<T> convertir(){
      typename std::vector<T> cpy;
      typename std::list<typename std::pair<int, T>>::iterator it;
      int last_index = 0;
      for(it = values.begin(); it != values.end(); ++it){
        for(int i = last_index; i < it->first; ++i){
            cpy.push_back(null_value);
        }
        cpy.push_back(it->second);
        last_index = it->first + 1;
      }
      for(int i = last_index; i < virtual_size; ++i){
        cpy.push_back(null_value);
      }
      return cpy;
    };

    void cambiar_nulo(const T& n){
      std::list<typename std::pair<int, T>> cpy;
      typename std::list<typename std::pair<int, T>>::iterator it;
      int last_index = 0;
      for(it = values.begin(); it != values.end(); ++it){
        for(int i = last_index; i < it->first; ++i){
            cpy.push_back(std::pair<int, T>(i, null_value));
        }
        if(it->second != n){
          cpy.push_back(std::pair<int, T>(it->first, it->second));
        }
        last_index = it->first + 1;
      }
      for(int i = last_index; i < virtual_size; ++i){
        cpy.push_back(std::pair<int, T>(i, null_value));
      }
      values = cpy;
      null_value = n;
    };
};

template<typename T>
std::string vector_toString(const std::vector<T>& v){
  typename std::vector<T>::const_iterator it;
  std::string result = "";
  for(it = v.begin(); it != v.end(); ++it){
    result += std::to_string(*it);
  }
  return result;
}

int main(){
  vdisperso<int> values;
  values.asignar_coeficiente(0, 1);
  values.asignar_coeficiente(2, 1);
  values.asignar_coeficiente(5, 1);
  std::vector<int> c = values.convertir();
  std::cout << vector_toString(c) << std::endl;

  values.cambiar_nulo(1);
  std::vector<int> c2 = values.convertir();
  std::cout << vector_toString(c2) << std::endl;
  return 0;
}
