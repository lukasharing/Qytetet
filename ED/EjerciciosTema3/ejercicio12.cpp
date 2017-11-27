#include <iostream>
#include <string>
#include <list>
#include <algorithm>

template<typename T>
bool contenida(const std::list<T>&, const std::list<T>&);
template<typename T>
std::string list_toString(const std::list<T>&);

int main(){
  std::list<int> l0;
  l0.insert(l0.begin(), {3, 4, 4, 1, 1, 5, 1, 1, 1, 1});
  std::list<int> l1;
  l1.insert(l1.begin(), {1, 1, 5, 1, 1});
  std::list<int> l2;
  l1.insert(l2.begin(), {1, 1, 5, 3, 1});

  std::cout << "lista 0" << list_toString(l0) << std::endl;
  std::cout << "lista 1" << list_toString(l1) << std::endl;
  std::cout << "lista 2" << list_toString(l2) << std::endl;

  std::cout << "La lista 1 está contenida en la lista 0: " << contenida(l1, l0) << std::endl;
  std::cout << "La lista 2 está contenida en la lista 0: " << contenida(l2, l0) << std::endl;
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
bool contenida(const std::list<T>& l0, const std::list<T>& l1){
  typename std::list<T>::const_iterator it = l0.begin(), ti = l1.begin();
  while(it != l0.end() && ti != l1.end()){
    if(*ti == *it){ ++it; }
    else{ it = l0.begin(); }
    ++ti;
  }
  return (it == l0.end());
}
