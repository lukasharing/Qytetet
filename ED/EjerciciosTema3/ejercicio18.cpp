#include <iostream>
#include <vector>
#include <string>
#include <utility>
#include <iterator>
#include <algorithm>
#include <cassert>

template<typename T>
void intercalar(std::vector<T> &, const std::vector<std::pair<int, T>>&);
template<typename T>
std::string vector_toString(const std::vector<T>&);

int main(){
  std::vector<int> v;
  v.insert(v.begin(), {3, 4, 6, 7, 8});
  std::vector<std::pair<int, int>> p;
  p.push_back(std::pair<int, int>(0, 0));
  p.push_back(std::pair<int, int>(1, 1));
  p.push_back(std::pair<int, int>(2, 2));
  p.push_back(std::pair<int, int>(5, 5));
  p.push_back(std::pair<int, int>(9, 9));
  intercalar(v, p);
  std::cout << vector_toString(v) << std::endl;
  return 0;
}

template<typename T>
std::string vector_toString(const std::vector<T>& v){
  typename std::vector<T>::const_iterator it;
  std::string result = "";
  for(it = v.begin(); it != v.end(); ++it){
    result += std::to_string(*it);
  }
  return result;
}

template<typename T>
void intercalar(std::vector<T> & v, const std::vector<std::pair<int, T>>& p){
  typename std::vector<std::pair<int, T>>::const_iterator it;
  for(it = p.begin(); it != p.end(); ++it){
    typename std::vector<T>::iterator ti = v.begin();
    for(int i = 0; i < it->first; ++i, ++ti);
    assert(std::distance(v.end(), ti) <= 0);
    v.insert(ti, it->second);
  }
}
