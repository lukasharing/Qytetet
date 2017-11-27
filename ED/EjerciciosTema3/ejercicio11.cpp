#include <iostream>
#include <string>
#include <list>
#include <algorithm>

std::list<int>& change_left(std::list<int>& l0, const int& i);
template<typename T>
std::string list_toString(const std::list<T>&);

int main(){
  std::list<int> l0;
  l0.insert(l0.begin(), {3, 4, 4, 1, 1, 5, 1, 1, 1, 1});

  std::cout << list_toString(l0) << std::endl;
  change_left(l0, 1);
  std::cout << list_toString(l0) << std::endl;
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

std::list<int>& change_left(std::list<int>& l0, const int& i){
  std::list<int>::const_iterator it;
  for(it = l0.begin(); it != l0.end(); ++it){
    if(*it == i){
      l0.insert(++it, i - 1);
      --it;
    }
  }
  return l0;
}
