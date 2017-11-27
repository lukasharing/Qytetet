#include <iostream>
#include <string>
#include <list>
#include <algorithm>

template<typename T>
std::list<T>& elimina_duplicados(std::list<T>&);
template<typename T>
std::string list_toString(const std::list<T>&);

int main(){
  std::list<int> list;
  list.insert(list.begin(), {5, 1, 1, 2, 2, 1, 3, 4, 4, 1, 1, 5});

  std::cout << list_toString(list) << std::endl;
  elimina_duplicados(list);
  std::cout << list_toString(list) << std::endl;
  return 0;
};

template<typename T>
std::string list_toString(const std::list<T>& list){
  typename std::list<T>::const_iterator it;
  std::string result = "";
  for(it = list.begin(); it != list.end(); ++it){
    result += std::to_string(*it);
  }
  return result;
}

template<typename T>
std::list<T>& elimina_duplicados(std::list<T>& list){
  typename std::list<T>::iterator it;
  for(it = list.begin(); it != list.end(); ++it){
    typename std::list<T>::iterator find = std::find(list.begin(), it, *it);
    if(find != it){
      typename std::list<T>::iterator cpy = it;
      --it;
      list.erase(cpy);
    }
  }
  return list;
}
