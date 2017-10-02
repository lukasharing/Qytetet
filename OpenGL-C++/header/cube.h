#ifndef CUBE_H
#define CUBE_H
#include "../header/object3d.h"

class Cube : public Object3D{
  public:
    Cube();
};

Cube::Cube(){
  position.new_object();
  rotation.new_object();
  new_object();
  // Assigning Vertices
  float _v[24]{
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
  };
  vertices.assign(_v, _v + 24);

  // Assigning Sides
  int _e[36]{
  	0,1,2,	2,3,0,
  	0,1,5,	5,4,0,
   	0,4,7,	7,3,0,

  	6,2,1,	1,5,6,
  	6,2,3,	3,7,6,
   	6,7,4,	4,5,6
  };
  sides.assign(_e, _e + 36);

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
