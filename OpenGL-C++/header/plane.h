#ifndef Plane_H
#define Plane_H
#include "object3d.h"

class Plane : public Object3D{
  public:
    Plane(float);
};

Plane::Plane(float s){
  new_object();

  setColor(0.0, 0.0, 1.0);
  // Assigning Verticesl
  s *= 2;
  for(float j = 0; j <= s; j += 1.0){
    for(float i = 0; i <= s; i += 1.0){
      vertices.insert(vertices.begin(), {i - s / 2.0, 0.0, j - s / 2.0});
    }
  }
  std::cout << "TODOS" << std::endl;
  for(int j = 0; j <= s - 1; ++j){
    int jj = j * (s + 1);
    for(int i = 0; i <= s - 1; ++i){
      unsigned int n = i + jj;
      unsigned int m = s + n + 1;
      sides.insert(sides.begin(), {n, n + 1, m, m, n + 1, m + 1});
    }
    std::cout << std::endl;
  }

  // Normal Calculation
  normal_calculation();
};
#endif
