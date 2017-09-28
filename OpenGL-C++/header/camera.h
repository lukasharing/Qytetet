#ifndef CAMERA_H
#define CAMERA_H
class Camera{
  private:
    float w, h; // Tamaño
    float x, y; // Coordenadas
    float front_plane, back_plane;
    float x_rotation, y_rotation;

  public:
    // Ancho de la Pantalla
    float getWidth();
    void setWidth(float);

    // Alto de la Pantalla
    float getHeight();
    void setHeight(float);

    // Tamaño
    void resize(float, float);

    // Coordenada X
    float getX();
    void setX(float);

    // Coordenada Y
    float getY();
    void setY(float);

    // Coordenadas X/Y
    void set(float, float);

    // Rotation X
    float getRotationX();
    void addXRotation(float);
    void setRotationX(float);

    // Rotation Y
    float getRotationY();
    void addYRotation(float);
    void setRotationY(float);

    // Rotation X/Y
    void setRotation(float, float);

    // Planes
    float getZoom();
    float getFrontPlane();
    float getBackPlane();
    void setPlane(float, float);

    Camera();
    Camera(float, float, float, float, float, float);
};


float Camera::getWidth(){ return w; };
void  Camera::setWidth(float _w){ w = _w; };

float Camera::getHeight(){ return h; };
void  Camera::setHeight(float _h){ h = _h; };

void  Camera::resize(float _w, float _h){ w = _w; h = _h; };

float Camera::getX(){ return y; };
void  Camera::setX(float _x){ x = _x; };
float Camera::getY(){ return y; };

void  Camera::setY(float _y){ y = _y; };
void  Camera::set(float _x, float _y){ x = _x; y = _y; };

float Camera::getRotationX(){ return x_rotation; };
void  Camera::setRotationX(float _rx){ x_rotation = _rx; };
void  Camera::addXRotation(float _rx){ x_rotation += _rx; };

float Camera::getRotationY(){ return y_rotation; };
void  Camera::setRotationY(float _ry){ y_rotation = _ry; };
void  Camera::addYRotation(float _ry){ y_rotation += _ry; };

void  Camera::setRotation(float _rx, float _ry){ x_rotation = _rx; y_rotation = _ry; };

float Camera::getZoom(){ return 0.2f; }
float Camera::getFrontPlane(){ return front_plane; };
float Camera::getBackPlane(){ return back_plane; };
void  Camera::setPlane(float _front, float _back){ front_plane = _front; back_plane = _back; };

Camera::Camera(){ resize(100.f, 100.f); set(0.f, 0.f); setPlane(10.f, 1000.f); setRotation(0.f, 0.f); };
Camera::Camera(float _x, float _y, float _w, float _h, float _front, float _back){ set(_x, _y); resize(_w, _h); setPlane(_front, _back); setRotation(0.f, 0.f); };

#endif
