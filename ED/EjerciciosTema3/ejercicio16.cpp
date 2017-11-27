#include <iostream>
#include <list>
#include <utility>
#include <iterator>

template<typename T>
std::list<std::pair<T, int>> comprimir(const std::list<T> &);

template<typename T>
std::list<T> descomprimir(const std::list<std::pair<T, int>>&);

int main(){
  std::list<int> list;
  list.insert(list.begin(), {0, 0, 1, 1, 1, 1, 2, 2, 1, 1, 1, 0, 3, 3, 3});
  std::list<std::pair<int, int>> compressed = comprimir(list);
  std::list<std::pair<int, int>>::iterator it;
  for(it = compressed.begin(); it != compressed.end(); ++it){
    std::cout << it->first << " => " << it->second << std::endl;
  }
  std::list<int> uncompressed = descomprimir(compressed);
  std::list<int>::iterator ti;
  std::cout << "La lista descomprimida es:";
  for(ti = uncompressed.begin(); ti != uncompressed.end(); ++ti){
    std::cout << ' ' << *ti;
  }
  return 0;
}

template<typename T>
std::list<std::pair<T, int>> comprimir(const std::list<T>& l){
  typename std::list<std::pair<T, int>> result;
  typename std::list<T>::const_iterator it, ti;
  for(it = l.begin(), ti = l.begin(); it != l.end(); ++it){
    if(*ti != *++it || it == l.end()){
      result.push_back(std::pair<T, int>(*ti, std::distance(ti, it)));
      ti = it;
    }
    --it;
  }
  return result;
}

template<typename T>
std::list<T> descomprimir(const std::list<std::pair<T, int>>& l){
  typename std::list<T> result;
  typename std::list<std::pair<T, int>>::const_iterator it;
  for(it = l.begin(); it != l.end(); ++it){
    for(int i = 0; i < it->second; ++i){
      result.push_back(it->first);
    }
  }
  return result;
}
