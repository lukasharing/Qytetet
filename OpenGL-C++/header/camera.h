#ifndef CAMERA_H
#define CAMERA_H
#include <GL/gl.h>
#include "vector3d.h"
class Camera{
  private:
    float w, h; // Tamaño
    float front_plane, back_plane;
    float fov;
    bool perspective;

    Vector3D old_position;
    Vector3D position;

    Vector3D top;
    Vector3D look;

    void copy(const Camera& c){
      resize(c.w, c.h);
      setPlane(c.front_plane, c.back_plane);
      fov = c.fov;
      perspective = c.perspective;
      old_position = c.old_position;
      position = c.position;
      top = c.top;
      look = c.look;
    }
  public:
    // Ancho de la Pantalla
    float getWidth() const;
    void setWidth(float);

    // Alto de la Pantalla
    float getHeight() const;
    void setHeight(float);

    // Tamaño
    void resize(int, int);
    void zoom(float);
    void setFov(float);

    // Translation / Look
    void projection();
    Vector3D& getPosition();
    Vector3D& getOldPosition();
    Vector3D& getLookAt();
    Vector3D& getTopVector();

    // Planes
    float getFrontPlane() const;
    float getBackPlane() const;
    void setPlane(float, float);

    Camera();
    Camera(float, float, float, float, float, bool);
    Camera(const Camera&);

    Camera* operator = (const Camera& c){
      if(this != &c){
        copy(c);
      }
      return this;
    }
};

float Camera::getWidth() const{ return w; };
void  Camera::setWidth(float _w){ w = _w; };

float Camera::getHeight() const{ return h; };
void  Camera::setHeight(float _h){ h = _h; };

void Camera::resize(int _w, int _h){
  w = _w;
  h = _h;
	projection();
	glViewport(0, 0, _w, _h);
};
void  Camera::zoom(float z){ front_plane *= z; projection(); };
void  Camera::setFov(float f){ fov = f; };

// Translation / Rotation
Vector3D& Camera::getPosition(){ return position; };
Vector3D& Camera::getOldPosition(){ return old_position; };
Vector3D& Camera::getLookAt(){ return look; };
Vector3D& Camera::getTopVector(){ return top; };

// Planes
float Camera::getFrontPlane() const{ return front_plane; };
float Camera::getBackPlane() const{ return back_plane; };
void  Camera::setPlane(float _front, float _back){ front_plane = _front; back_plane = _back; };

void Camera::projection(){
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	GLfloat aspect = w / h;
  if(perspective){
	   gluPerspective(fov, aspect, front_plane, back_plane);
  }else{
    // glOrtho(	GLdouble left,
   	// GLdouble right,
   	// GLdouble bottom,
   	// GLdouble top,
    // camera.getFrontPlane(),
   	// camera.getBackPlane());
  }
  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();
  gluLookAt(position.getX(), position.getY(), position.getZ(),
            look.getX(), look.getY(), look.getZ(),
            top.getX(), top.getY(), top.getZ());

};

Camera::Camera(const Camera& c){
  copy(c);
}
Camera::Camera(){
  fov = 43.0;
  resize(100.f, 100.f);
  setPlane(10.f, 1000.f);
  position.new_object();
  look.new_object();
  top.new_object();
};

Camera::Camera(float _w, float _h, float _front, float _back, float _fov, bool _pers){
  fov = _fov;
  perspective = _pers;
  resize(_w, _h);
  setPlane(_front, _back);
  look.new_object();
  top.new_object();
  position.new_object();
};

#endif
