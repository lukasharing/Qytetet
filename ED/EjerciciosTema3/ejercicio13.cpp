#include <iostream>
#include <string>
#include <list>
#include <algorithm>

template<typename T>
std::list<T> sorted_iterator(const std::list<T>&, const std::list<typename std::list<T>::iterator>&);
template<typename T>
std::string list_toString(const std::list<T>&);

int main(){
  std::list<int> l0;
  l0.insert(l0.begin(), {3, 1, 2});
  std::cout << "lista " << list_toString(l0) << std::endl;
  std::list<std::list<int>::iterator> l1;
  l1.insert(l1.begin(), {++l0.begin(),--l0.end(),l0.begin()});
  std::cout << "lista ordenada por iteradores: " << list_toString(sorted_iterator(l0, l1)) << std::endl;
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
std::list<T> sorted_iterator(const std::list<T>& l0, const std::list<typename std::list<T>::iterator>& l1){
  std::list<T> result;
  typename std::list<typename std::list<T>::iterator>::const_iterator it;
  for(it = l1.begin(); it != l1.end(); ++it){
    result.push_back(**it);
  }
  return result;
}
