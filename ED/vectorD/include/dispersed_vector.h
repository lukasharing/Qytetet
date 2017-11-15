#ifndef DISPERSEDVECTOR_H
#define DISPERSEDVECTOR_H


#include<iostream>


#include <string>
#include <algorithm>
#include <set>
#include <cassert>
template<typename T>
struct DispersedValue{
  mutable T value;
  unsigned int index;
  DispersedValue(T v, unsigned int i):index(i), value(v){};
};

template<typename T>
inline bool operator<(const DispersedValue<T>& lhs, const DispersedValue<T>& rhs){
  return lhs.index < rhs.index;
}

template<typename T>
inline bool operator==(const DispersedValue<T>& lhs, const DispersedValue<T>& rhs){
  return lhs.index == rhs.index;
}

template<typename T>
inline bool operator==(const DispersedValue<T>& lhs, const T& rhs){
  return lhs.index == rhs;
}

template<typename T>
class DispersedVector{
  private:
    std::set<DispersedValue<T>> values;
    unsigned int virtual_size;
    T null_value;
  public:
    /**
      @brief Predefined Constructor
    */
    DispersedVector(){
      virtual_size = 0;
      null_value = NULL;
    };

    /**
      @brief Resize constructor
      @param
    */
    DispersedVector(unsigned int a, const T& b){
      virtual_size = a;
      null_value = b;
    };

    /**
      @brief sets a value in a specified position
      @param position, value
    */
    void set(int a, const T& b){
      assert(a >= 0 && a < virtual_size);
      typename std::set<DispersedValue<T>>::iterator found = std::find(values.begin(), values.end(), a);
      if(found != values.end()){
        (*found).value = b;
      }else{
        values.insert(DispersedValue<T>(b, a));
      }
    };

    /**
      @brief checks if the vector is empty.
      @return true or false if it's virtually / real empty.
    */
    inline bool empty() const{ return (virtual_size == 0 && values.empty()); };

    /**
      @brief Adds at the end one more element
      @param element to add
    */
    void push_back(const T& v){
      if(v != null_value){
        unsigned int i;
        if(!values.empty()){
          i = (*values.end()).index;
          if(i == virtual_size){
            i = virtual_size++;
          }
        }else{
          i = virtual_size++;
        }
        values.insert(DispersedValue<T>(v, i));
      }else{
        virtual_size++;
      }
    };

    /**
      @brief Removes the last element in the vector (If it's virtual,
      then just decrease the disperse vector size).
    */
    void pop_back(){
      assert(!values.empty());
      if(!values.empty() && (*--values.end()).index == virtual_size - 1){
        values.erase(--values.end());
        virtual_size--;
      }else{
        virtual_size--;
      }
    };

    /**
      @brief Insert an array into the dispersed vector
      @param position, array
    */
    //void insert(int a, const T t*){};

    /**
      @brief Clears the vector
    */
    void clear(){
      values.clear();
      virtual_size = 0;
    };

    /**
      @brief Resize yes/no virtually the vector
      @param new size
    */
    void resize(int r){
      assert(r >= 0);
      typename std::set<DispersedValue<T>>::iterator v = values.end();

      std::vector<typename std::set<DispersedValue<T>>::iterator> elements;
      while(v-- != values.begin() && (*v).index >= r){
        elements.push_back(v);
      }
      while(!elements.empty()){
        values.erase(elements.back());
        elements.pop_back();
      }

      std::cout << std::endl;
      virtual_size = r;
    };

    /**
      @brief Get value at position n
      @param position
      @return value at position n if exist, if not, then null_value
    */
    T at(int a){
      assert(a >= 0 && a < virtual_size);
      if(!values.empty()){
        typename std::set<DispersedValue<T>>::iterator found = std::find(values.begin(), values.end(), a);
        if(found != values.end()){
          return (*found).value;
        }else{
          return null_value;
        }
      }else{
        return null_value;
      }
    };

    /**
      @brief
      @param
      @return
    */
    T& front(){
      if(!values.empty() && (*values.begin()).index == 0){
        return (*values.begin()).value;
      }else{
        return null_value;
      }
    };

    /**
      @brief
      @param
      @return
    */
    T& back(){
      if(!values.empty() && (*values.end()).value == virtual_size - 1){
        return (*--values.end()).value;
      }else{
        return null_value;
      }
    };

    /**
      @brief Overloading [] operator
      @param index a
      @return value at the position a
    */
    T& operator[](int a){
      return at(a);
    };

    /**
      @brief Vector to string
    */
    std::string to_string(){
      std::string vector = "";
      int last = 0;
      for(auto i = values.begin(); i != values.end(); i++){
        int size = (*i).index;
        for(int j = last; j < size; j++){
          vector += std::to_string(null_value) + ' ';
        }
        vector += std::to_string((*i).value) + ' ';
        last = size + 1;
      }
      for(int i = last; i < virtual_size; i++){
        vector += std::to_string(null_value) + ' ';
      }
      return vector;
    };
};



#endif
