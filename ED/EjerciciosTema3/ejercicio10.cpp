#include <iostream>
#include <string>
#include <list>
#include <algorithm>

template<typename T>
std::list<T> mezclar(const std::list<T>&, const std::list<T>&);
template<typename T>
std::string list_toString(const std::list<T>&);

int main(){
  std::list<int> l0;
  l0.insert(l0.begin(), {3, 4, 4, 1, 1, 5, 1, 1, 1, 1});
  std::list<int> l1;
  l1.insert(l1.begin(), {5, 1, 1, 2, 2, 1});

  std::cout << list_toString(l0) << std::endl;
  std::cout << list_toString(l1) << std::endl;
  std::list<int> l2 = mezclar(l0, l1);
  std::cout << list_toString(l2) << std::endl;
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
std::list<T> mezclar(const std::list<T>& l0, const std::list<T>& l1){
  typename std::list<T> result;
  typename std::list<T>::const_iterator it = l0.begin(), ti = l1.begin();
  while(it != l0.end() && ti != l1.end()){
    result.push_back(*(it++));
    result.push_back(*(ti++));
  }
  // Metemos uno de los restantes
  for(; it != l0.end(); ++it){ result.push_back(*it); }
  for(; ti != l1.end(); ++ti){ result.push_back(*ti); }
  return result;
}
