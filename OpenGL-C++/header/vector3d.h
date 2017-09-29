#ifndef Vector3D_H
#define Vector3D_H
class Vector3D{
  private:
    float x, y, z;
  public:
    float getX() const;
    void setX(float);
    void addX(float);
    void multX(float);

    float getY() const;
    void setY(float);
    void addY(float);
    void multY(float);

    float getZ() const;
    void setZ(float);
    void addZ(float);
    void multZ(float);

    void set(float, float, float);
    void new_object();

    Vector3D();
    Vector3D(float, float, float);
    Vector3D(float*);
};

float Vector3D::getX() const{ return x; };
void Vector3D::setX(float _x){ x = _x; };
void Vector3D::addX(float _x){ x += _x; };
void Vector3D::multX(float _x){ x *= _x; };

float Vector3D::getY() const{ return y; };
void Vector3D::setY(float _y){ y = _y; };
void Vector3D::addY(float _y){ y += _y; };
void Vector3D::multY(float _y){ y *= _y; };

float Vector3D::getZ() const{ return z; };
void Vector3D::setZ(float _z){ z = _z; };
void Vector3D::addZ(float _z){ z += _z; };
void Vector3D::multZ(float _z){ z *= _z; };

void Vector3D::set(float _x, float _y, float _z){ x = _x; y = _y; z = _z; };

void Vector3D::new_object(){
  x = 0.f; y = 0.f; z = 0.f;
};

Vector3D::Vector3D(){ x = 0.f; y = 0.f; z = 0.f; };
Vector3D::Vector3D(float _x, float _y, float _z){ x = _x; y = _y; z = _z;  };
Vector3D::Vector3D(float* v){ x = v[0]; y = v[1]; z = v[2];  };
#endif
