#ifndef CUBE_H
#define CUBE_H
#include "object3d.h"

class Cube : public Object3D{
  public:
    Cube();
};

Cube::Cube(){
  position.new_object();
  rotation.new_object();
  new_object();
  // Assigning Vertices
  float _v[48]{
  	/* TOPFACE */
		-1.0f, -1.0f, -1.0f, //0
    -1.0f, +1.0f, -1.0f, //1
		+1.0f, +1.0f, -1.0f, //2
    +1.0f, -1.0f, -1.0f, //3
  	/* BOTTOM FACE */
		-1.0f, -1.0f, +1.0f, //4
    -1.0f, +1.0f, +1.0f, //5
		+1.0f, +1.0f, +1.0f, //6
    +1.0f, -1.0f, +1.0f, //7

    -1.0f, +1.0f, -1.0f, //1
    +1.0f, +1.0f, -1.0f, //2
    -1.0f, +1.0f, +1.0f, //5
		+1.0f, +1.0f, +1.0f, //6

		-1.0f, -1.0f, -1.0f, //0
    +1.0f, -1.0f, -1.0f, //3
		-1.0f, -1.0f, +1.0f, //4
    +1.0f, -1.0f, +1.0f, //7
  };
  vertices.assign(_v, _v + 48);

  float _t[32]{
    0.0, 0.0,
    0.0, 1.0,
    1.0, 1.0,
    1.0, 0.0,

    1.0, 0.0,
    1.0, 1.0,
    0.0, 1.0,
    0.0, 0.0,

    0.0, 0.0,
    1.0, 0.0,
    0.0, 1.0,
    1.0, 1.0,

    0.0, 0.0,
    1.0, 0.0,
    0.0, 1.0,
    1.0, 1.0,
  };
  texture.assign(_t, _t + 32);

  // Assigning Sides
  const int size = 6;
  int _e[size * 6]{
  	0,1,2, 2,3,0,
    11,9,8, 8,10,11,
  	5,1,0, 0,4,5,
    4,7,6, 6,5,4,
  	6,7,3, 3,2,6,
    0,13,15, 15,14,0,
  };
  sides.assign(_e, _e + size * 6);

  float _c[36]{
  	1.f,0.f,1.f,	1.f,0.f,1.f,
  	1.f,0.f,1.f,	1.f,0.f,1.f,

    1.f,0.f,1.f,	1.f,0.f,1.f,
  	1.f,0.f,1.f,	1.f,0.f,1.f,

  	1.f,0.f,1.f,	1.f,0.f,1.f,
  	1.f,0.f,1.f,	1.f,0.f,1.f,
  };
  colors.assign(_c, _c + 36);

  // Normal Calculation
  normal_calculation();
};
#endif
