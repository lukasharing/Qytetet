#include <iostream>
#include <list>
#include <cassert>
#include <string>

/*
  A dynamic vector is such vector that can be resized and is resized each time
  (if there isn't enought space) the user inserts a new element.
  For example, http://www.cplusplus.com/reference/vector/vector/
  Because we are using the stl list, most of the method are the same,
  (Efficiency is shown in the link above) thats why I'm only going to implement
  the at operator and overload [] (The mayor difference btw vector and list).
*/
template<typename T>
class vector{
  private:
    std::list<T> values;
    // O(n), Ω(1)
    T getValue(const int& i){
      typename std::list<T>::iterator it = values.begin();
      for(int k = 0; k < i; ++k,++it);
      return *it;
    };
  public:
    // O(n)
    vector(T* v, int size){
      for(int i = 0; i < size; ++i){
        values.push_back(v[i]);
      }
    };

    // O(1) -> http://www.cplusplus.com/reference/list/list/push_back/
    void push_back(const T& k){ values.push_back(k); };
    // Efficiency: See getValue
    T at(const int& i){
      assert(i >= 0 && i < values.size());
      return getValue(i);
    };
    // Efficiency: See getValue
    T operator [](const int& i){
      return at(i);
    };

    // O(n)
    std::string to_string(){
      std::string result;
      typename std::list<T>::iterator it;
      for(it = values.begin(); it != values.end(); ++it){
        result += ' ' + std::to_string(*it);
      }
      return result;
    };
};

int main(){
  const int size = 10;
  int v[size];
  for(int i = 0; i < size; i++){
    v[i] = (((i - 5) % size) + size) % size;
  }
  vector<int> values(v, size);

  values.push_back(10);
  std::cout << values.to_string() << std::endl;

  std::cout << "El valor en la posicion 5 es " << values.at(5) << std::endl;
  std::cout << "El valor en la posicion 6 es " << values[6] << std::endl;
  return 0;
}
