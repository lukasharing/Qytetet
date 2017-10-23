#ifndef REVOLUTION_H
#define REVOLUTION_H
#include "object3d.h"
#include <string>
#include <cmath>
class Revolution : public Object3D{
  private:
    unsigned int revolution_height;
    unsigned int current_slices;
  public:
    Revolution(std::string, int, int);
    Revolution(float*, int, int, int);
    void generate_revolution(int, int);

    void setCover(int _c){ covers = _c; };
    int getCover() const{ return covers; };
    int getSlices() const{ return current_slices; };
};

void Revolution::generate_revolution(int n, int rotation = 0){
  current_slices = n;
  float slices  = 6.283185307f / n;
  sides.clear();
  vertices.resize(revolution_height * 3);

  int idx = revolution_height - 1;
  if(vertices[idx * 3] != 0.f || vertices[idx * 3 + 1] != 0.f){
    vertices.insert(vertices.end(), {0.f, 0.f, vertices[idx * 3 + 2]});
    revolution_height++;
  }

  if(vertices[0] != 0.f || vertices[1] != 0.f){
    vertices.insert(vertices.begin(), {0.f, 0.f, vertices[2]});
    revolution_height++;
  }

	for(int i = 0; i < n-1; i++){
    float angle = (i + 1) * slices;
    float cs = cos(angle), sn = sin(angle);

		for(int j = 1; j < revolution_height - 1; j++){
      int id = j * 3;
      float x = vertices[id + 0], y = vertices[id + 1], z = vertices[id + 2];
			switch(rotation){
				case 0:
      		vertices.insert(vertices.end(), {x, cs * y - sn * z, sn * y + cs * z});
				case 1:
      		vertices.insert(vertices.end(), {x * cs + z * sn, y, z * cs - x * sn});
				case 2:
      		vertices.insert(vertices.end(), {x * cs - y * sn, x * sn + y * cs, z});
			}
    }
  }

  // Adding Lower Cover
  unsigned int rhgt = revolution_height - 2;
  unsigned int last_bottom = revolution_height;
  sides.insert(sides.end(), {0, 1, last_bottom });
  for(int i = 0; i < n - 2; i++){
    sides.insert(sides.end(), { 0, last_bottom, last_bottom + rhgt });
    last_bottom += rhgt;
  }
  sides.insert(sides.end(), {0, last_bottom, 1});

  // Adding Upper Cover
  unsigned int last_top = revolution_height + rhgt - 1;
  sides.insert(sides.end(), {revolution_height - 1, rhgt, last_top });
  for(int i = 0; i < n - 2; i++){
    sides.insert(sides.end(), { revolution_height - 1, last_top, last_top + rhgt });
    last_top += rhgt;
  }
  sides.insert(sides.end(), {revolution_height - 1, last_top, rhgt});

  if(rhgt > 1){
    for(int j = 0; j < n; j++){
      int last = 1;
      for(int i = 0; i < rhgt - 1; i++){
        unsigned int u1 = i + j * rhgt;
        unsigned int u21 = u1 + 1;
        unsigned int u3 = (u1 + rhgt + 1) % (n * rhgt);
        unsigned int u22 = u3 - 1;

        u1 += u1 < rhgt ? 1 : 2;
        u21 += u21 < rhgt ? 1 : 2;
        u22 += u22 < rhgt ? 1 : 2;
        u3 += u3 < rhgt ? 1 : 2;

        sides.insert(sides.end(), { u1, u21, u3});
        sides.insert(sides.end(), { u1, u22, u3});

      }
    }
  }
};

Revolution::Revolution(std::string name, int n, int rotation){
  new_object();
  load_ply(name);
  revolution_height = vertices.size() / 3;
  generate_revolution(n, rotation);
  // Normal Calculation
  normal_calculation();
};

Revolution::Revolution(float* _p, int s, int n, int rotation){
  new_object();
  revolution_height = s;
  vertices.resize(s * 3);
  vertices.assign(_p, _p + s * 3);
  generate_revolution(n, rotation);
  // Normal Calculation
  normal_calculation();
};
#endif
