#include "../include/dispersed_vector.h"
#include<iostream>
using namespace std;
int main(){
  DispersedVector<int> matriz(4, 0);
  matriz.push_back(10);
  matriz.push_back(20);
  matriz.push_back(30);
  std::cout << matriz.to_string() << std::endl;

  // Changing virtual value
  std::cout << "Changing value from position 0 (Virtual)" << std::endl;
  matriz.set(0, 3);
  // Changing existing value
  std::cout << "Changing value from position 4 (Real)" << std::endl;
  matriz.set(4, 3);

  // Checking value at N position (existing)
  std::cout << "At position 3 the values is -> " << matriz.at(4) << std::endl;

  // Adding Virtual value
  matriz.push_back(0);
  // Checking value at N position (virtual)
  std::cout << "At position 6 the values is -> " << matriz.at(6) << std::endl;
  std::cout << matriz.to_string() << std::endl;
  // Removing virtual value
  matriz.pop_back();
  std::cout << "Removed last element (Virtual)" << std::endl;
  // Removing existing value
  matriz.pop_back();
  std::cout << "Removed last element (Real)" << std::endl;

  std::cout << matriz.to_string() << std::endl;

  // Resized to 10 values
  matriz.resize(10);
  std::cout << "Resized to the size of 10" << std::endl;
  std::cout << matriz.to_string() << std::endl;

  // Resized to 3 values
  matriz.resize(3);
  std::cout << "Resized to the size of 3" << std::endl;
  std::cout << matriz.to_string() << std::endl;
}
