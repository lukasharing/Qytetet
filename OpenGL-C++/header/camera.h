#ifndef CAMERA_H
#define CAMERA_H
#include <GL/gl.h>
#include "vector3d.h"
class Camera{
  private:
    float w, h; // Tamaño
    float front_plane, back_plane;
    Vector3D position; // Coordenadas
    Vector3D rotation;

  public:
    // Ancho de la Pantalla
    float getWidth() const;
    void setWidth(float);

    // Alto de la Pantalla
    float getHeight() const;
    void setHeight(float);

    // Tamaño
    void resize(int, int);

    // Rotacion X/Y
    void setRotacion(float, float);

    // Planes
    float getZoom() const;
    Vector3D& getPosition();
    Vector3D& getRotation();
    float getFrontPlane() const;
    float getBackPlane() const;
    void setPlane(float, float);

    Camera();
    Camera(float, float, float, float, float, float);
};


float Camera::getWidth() const{ return w; };
void  Camera::setWidth(float _w){ w = _w; };

float Camera::getHeight() const{ return h; };
void  Camera::setHeight(float _h){ h = _h; };

void  Camera::resize(int _w, int _h){ w = _w; h = _h; };

float Camera::getZoom() const{ return 0.2f; }
Vector3D& Camera::getPosition(){ return position; };
Vector3D& Camera::getRotation(){ return rotation; };
float Camera::getFrontPlane() const{ return front_plane; };
float Camera::getBackPlane() const{ return back_plane; };
void  Camera::setPlane(float _front, float _back){ front_plane = _front; back_plane = _back; };

Camera::Camera(){ resize(100.f, 100.f); setPlane(10.f, 1000.f); position.new_object(); rotation.new_object(); };
Camera::Camera(float _x, float _y, float _w, float _h, float _front, float _back){ position.new_object(); rotation.new_object(); resize(_w, _h); setPlane(_front, _back); };

#endif
