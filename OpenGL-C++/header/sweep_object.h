#ifndef SWEEP_H
#define SWEEP_H
#include "object3d.h"
#include <string>
#include <cmath>
#include <vector>
class Sweep : public Object3D{
  private:
    std::vector<float> curve;
    std::vector<float> polygon;
    unsigned int polygon_vertices;
    unsigned int curve_vertices;
  public:
    Sweep(std::string, float*, int);
    Sweep(float*, int, float*, int);
    void generate_sweep();
};

// Tenemos que suponer que el polígono está
// dibujado en el plano X^Z
void Sweep::generate_sweep(){
  sides.clear();
  vertices.clear();

  // Polygon normal
  float lx = curve[0], ly = curve[1], lz = curve[2];
  for(int i = 3; i < curve_vertices * 3; i+= 3){
    float sx = curve[i + 0], sy = curve[i + 1], sz = curve[i + 2];

    float dx = lx - sx, dy = ly - sy, dz = lz - sz;
    float ds = sqrt(dx * dx + dy * dy + dz * dz);
    dx /= ds; dy /= ds; dz /= ds;

    float angle_x = acos(dx), angle_z = acos(dz);
    float csx = cos(angle_x), snx = sin(angle_x);
    float csz = cos(angle_z), snz = sin(angle_z);
    for(int j = 0; j < polygon_vertices * 3; j += 3){
      float x = polygon[j + 0], y = polygon[j + 1], z = polygon[j + 2];

      float rx = x;
      float ry = y * csz - z * snz;
      float rz = y * snz + z * csz;

      float gx = rx * csz - ry * snz;
      float gy = rx * snz + ry * csz;
      float gz = rz;

      vertices.insert(vertices.end(), { lx + gx, ly + gy, lz + gz });
    }
    if(i + 3 == curve_vertices * 3){
      for(int j = 0; j < polygon_vertices * 3; j += 3){
        float x = polygon[j + 0], y = polygon[j + 1], z = polygon[j + 2];

        float rx = x;
        float ry = y * csx - z * snx;
        float rz = y * snx + z * csx;

        float gx = rx * csz - ry * snz;
        float gy = rx * snz + ry * csz;
        float gz = rz;
        vertices.insert(vertices.end(), { sx + gx, sy + gy, sz + gz });
      }
    }
    lx = sx; ly = sy; lz = sz;
  }

  // Creating faces
  for(int i = 0; i < curve_vertices-1; i++){
    for(int j = 0; j < polygon_vertices; j++){
      unsigned int u1 = polygon_vertices * i + j;
      unsigned int u21 = u1 + polygon_vertices;
      unsigned int u3 = (polygon_vertices * (i + 1) + j + 1);
      unsigned int u22 = u3 - polygon_vertices;
      if(j == polygon_vertices - 1){
        u3 -= polygon_vertices;
        u22 -= polygon_vertices;
      }
      sides.insert(sides.end(), { u1, u21, u3 });
      sides.insert(sides.end(), { u1, u22, u3 });
    }
  }
};

Sweep::Sweep(std::string name, float* _c, int _sc){
  new_object();
  load_ply(name);
  polygon_vertices = vertices.size() / 3;
  curve_vertices = _sc;
  polygon.assign(vertices.begin(), vertices.end());
  curve.assign(_c, _c + _sc * 3);
  generate_sweep();
  // Normal Calculation
  normal_calculation();
};

Sweep::Sweep(float* _p, int _sp, float* _c, int _sc){
  new_object();
  polygon_vertices = _sp;
  curve_vertices = _sc;
  polygon.assign(_p, _p + _sp * 3);
  curve.assign(_c, _c + _sc * 3);
  generate_sweep();
  // Normal Calculation
  normal_calculation();
};
#endif
