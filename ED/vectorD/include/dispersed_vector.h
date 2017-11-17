#ifndef DISPERSEDVECTOR_H
#define DISPERSEDVECTOR_H
#include <string>
#include <algorithm>
#include <set>
#include <vector>
#include <cassert>

#include<iostream>
template<typename T>
struct DispersedValue{
  mutable T value;
  int index;
  DispersedValue(T v, int i):value(v), index(i){};
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
    std::set< DispersedValue<T> > values;
    int virtual_size;
    T null_value;
    void copy(const DispersedVector<T>& c){
      values = c.values;
      virtual_size = c.virtual_size;
      null_value = c.null_value;
    };

    bool compare(const DispersedVector<T>& x){
      bool not_equal = true;
      if(x.values.size() == values.size()){
        typename std::set< DispersedValue<T> >::iterator i = x.values.begin();
        typename std::set< DispersedValue<T> >::iterator j = values.begin();
        for(; j != values.end() && not_equal; ++i, ++j){
          if((*i).value != (*j).value){
            not_equal = false;
          }
        }
      }
      return not_equal && virtual_size == x.virtual_size;
    };
  public:
    /**
      @brief Predefined Constructor
    */
    DispersedVector(){
      virtual_size = 0;
      null_value = T();
    };

    /**
      @brief Resize constructor
      @param
    */
    DispersedVector(int a, const T& b){
      assert(a >= 0);
      virtual_size = a;
      null_value = b;
    };

    /**
      @brief Copy constructor
      @param Container to copy
    */
    DispersedVector(const DispersedVector<T> & x){
      copy(x);
    };

    /**
      @brief Virtual /not of the container
      @param Size of the container
    */
    inline int size() const{ return virtual_size; };

    /**
      @brief
      @param Returns the null_value
    */
    inline T default_value() const{ return null_value; };

    /**
      @brief sets a value in a specified position
      @param position, value
    */
    void set(int a, const T& b){
      typename std::set< DispersedValue<T> >::iterator found = std::find(values.begin(), values.end(), a);
      if(found != values.end()){
        (*found).value = b;
      }else{
        if(a > virtual_size){
          virtual_size = a;
        }
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
        int i;
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
      }
      virtual_size--;
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
      typename std::set< DispersedValue<T> >::iterator v = values.end();

      std::vector<typename std::set< DispersedValue<T> >::iterator> elements;
      while(v-- != values.begin() && (*v).index >= r){
        elements.push_back(v);
      }
      while(!elements.empty()){
        values.erase(elements.back());
        elements.pop_back();
      }
      virtual_size = r;
    };

    /**
      @brief Get value at position n
      @param position
      @return value at position n if exist, if not, then null_value
    */
    T& at(int a){
      assert(a >= 0 && a < virtual_size);
      if(!values.empty()){
        typename std::set< DispersedValue<T> >::iterator found = std::find(values.begin(), values.end(), a);
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
      @brief Peeks from the first value in the container
      @return First element in the set
    */
    T& front(){
      if(!values.empty() && (*values.begin()).index == 0){
        return (*values.begin()).value;
      }else{
        return null_value;
      }
    };

    /**
      @brief Peeks from the last value in the container
      @return Last element in the set
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
    std::string to_string() const{
      std::string vector = "";
      int last = 0;

      typename std::set< DispersedValue<T> >::iterator i;
      for(i = values.begin(); i != values.end(); ++i){
        int size = (*i).index;
        for(int j = last; j < size; j++){
          vector += std::to_string(null_value) + ' ';
        }
        vector += std::to_string((*i).value) + ' ';
        last = size + 1;
      }
      for(int i = last; i < virtual_size; ++i){
        vector += std::to_string(null_value) + ' ';
      }
      return vector;
    };

    /**
      @brief Overloading = operator
      @param DispersedVector to copy
      @return pointer to the container
    */
    DispersedVector<T>& operator = (const DispersedVector<T> & x){
      if(this != &x){
        copy(x);
      }
      return *this;
    };


    /**
      @brief Overloading == operator
      @param DispersedVector to compare
      @return boolean if are equal in size and elements
    */
    inline bool operator == (const DispersedVector<T> & x){ return compare(x); };

    /**
      @brief Overloading != operator
      @param DispersedVector to compare
      @return boolean if are not equal in size and elements
    */
    inline bool operator != (const DispersedVector<T> & x){ return !compare(x); };
};



#endif
