#include <iostream>
#include <string>
#include <list>
#include <algorithm>

template<typename T>
void inverso(std::list<T>&);
template<typename T>
std::string list_toString(const std::list<T>&);

int main(){
  std::list<int> list;
  list.insert(list.begin(), {5, 1, 1, 2, 2, 1, 3, 4, 4, 1, 1, 5});

  std::cout << list_toString(list) << std::endl;
  inverso(list);
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
void inverso(std::list<T>& list){
  typename std::list<T>::iterator it = list.begin(), ti = --list.end();
  for(int i = 0; i < list.size() / 2; ++i){
    std::swap(*(it++), *(ti--));
  }
}
