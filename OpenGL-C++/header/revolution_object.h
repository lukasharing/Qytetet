#ifndef REVOLUTION_H
#define REVOLUTION_H
#include "object3d.h"
#include <string>
#include <cmath>
#define EPSILON 0.0001
class Revolution : public Object3D{
  private:
    int revolution_height;
    int current_slices;
    bool draw_covers;
  public:
    Revolution(std::string, int);
    Revolution(int*, int, int);
    void generate_revolution(int);
    bool hasUpperCover() const;
    bool hasLowerCover() const;

    int getSlices() const{ return current_slices; };
};


bool Revolution::hasUpperCover() const{ return (vertices[(revolution_height - 1) * 3] - 0.0f) < EPSILON; };
bool Revolution::hasLowerCover() const{ return (vertices[0] - 0.0f) < EPSILON; };

void Revolution::generate_revolution(int n){
  current_slices = n;
  float slices  = 6.283185307f / n;
  vertices.erase(vertices.begin() + revolution_height * 3, vertices.end());;
  sides.clear();
  for(int i = 1; i < n + 1; i++){
    float angle = i * slices;
    float cs = cos(angle), sn = sin(angle);
    for(int j = 0; j < revolution_height; j++){
      unsigned int id_nxt = j + (i % n) * revolution_height;
      unsigned int id_bfr = j + (i - 1) * revolution_height;
      if(i < n){
        int id = j * 3;
        float x = vertices[id + 0];
        float y = vertices[id + 1];
        float z = vertices[id + 2];
        vertices.push_back(x * cs - y * sn);
        vertices.push_back(x * sn + y * cs);
        vertices.push_back(z);
      }
      if(j > 0){
        sides.insert(sides.end(), { id_bfr, id_nxt,  id_nxt - 1});
      }
      if(j < revolution_height - 1){
        sides.insert(sides.end(), { id_bfr, id_bfr + 1, id_nxt });
      }
    }
  }
};

Revolution::Revolution(std::string name, int n){
  new_object();
  load_ply(name);
  revolution_height = vertices.size() / 3;
  generate_revolution(n);
  draw_covers = false;
  // Normal Calculation
  normal_calculation();
};

Revolution::Revolution(int* _p, int s, int n){
  new_object();
  revolution_height = s;
  vertices.resize(s * n * 3);
  vertices.assign(_p, _p + s * 3);
  generate_revolution(n);
  draw_covers = false;
  // Normal Calculation
  normal_calculation();
};
#endif
