#include <iostream>
#include <string>
#include <list>

template<typename T>
std::list<T>& remove(std::list<T>&, T);
template<typename T>
std::string list_toString(const std::list<T>&);

int main(){
  std::list<int> list;
  list.insert(list.begin(), {1, 1, 2, 2, 1, 3, 4, 4, 1, 1});

  std::cout << list_toString(list) << std::endl;
  remove(list, 1);
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
std::list<T>& remove(std::list<T>& list, T elm){
  typename std::list<T>::iterator it;
  for(it = list.begin(); it != list.end(); ++it){
    if(*it == elm){
      typename std::list<T>::iterator cpy = it;
      --it;
      list.erase(cpy);
    }
  }
  return list;
}
