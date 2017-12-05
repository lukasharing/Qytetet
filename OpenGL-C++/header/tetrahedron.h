#ifndef TETRAEDRO_H
#define TETRAEDRO_H
#include "object3d.h"

class Tetrahedron : public Object3D{
  public:
    Tetrahedron();
};

Tetrahedron::Tetrahedron(){
  new_object();
  // Assigning Vertices
  float _v[12]{
  	/* TOPFACE */
		+1.0f, -1.0f, -1.0f, //0
    -1.0f, -1.0f, -1.0f, //1
		+1.0f, +1.0f, -1.0f, //2
		+1.0f, -1.0f, +1.0f, //4
  };
  vertices.assign(_v, _v + 12);

  // Assigning Sides
  int _s[12]{
  	2,1,0,
    3,1,0,
    2,3,0,
    1,2,3
  };
  sides.assign(_s, _s + 12);

  float _c[12]{
  	0.f,1.f,0.f,	0.f,1.f,0.f,
  	0.f,1.f,0.f,	1.f,0.f,1.f,
  };
  colors.assign(_c, _c + 12);


  // Normal Calculation
  normal_calculation();
};
#endif
