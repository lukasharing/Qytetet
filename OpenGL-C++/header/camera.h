#ifndef CAMERA_H
#define CAMERA_H
class Camera{
  private:
    float w, h; // Tamaño
    float x, y; // Coordenadas
    float front_plane, back_plane;
    float x_rotacion, y_rotacion;

  public:
    // Ancho de la Pantalla
    float getWidth() const;
    void setWidth(float);

    // Alto de la Pantalla
    float getHeight() const;
    void setHeight(float);

    // Tamaño
    void resize(float, float);

    // Coordenada X
    float getX() const;
    void setX(float);

    // Coordenada Y
    float getY() const;
    void setY(float);

    // Coordenadas X/Y
    void set(float, float);

    // Rotacion X
    float getRotacionX() const;
    void addXRotacion(float);
    void setRotacionX(float);

    // Rotacion Y
    float getRotacionY() const;
    void addYRotacion(float);
    void setRotacionY(float);

    // Rotacion X/Y
    void setRotacion(float, float);

    // Planes
    float getZoom() const;
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

void  Camera::resize(float _w, float _h){ w = _w; h = _h; };

float Camera::getX() const{ return y; };
void  Camera::setX(float _x){ x = _x; };
float Camera::getY() const{ return y; };

void  Camera::setY(float _y){ y = _y; };
void  Camera::set(float _x, float _y){ x = _x; y = _y; };

float Camera::getRotacionX() const{ return x_rotacion; };
void  Camera::setRotacionX(float _rx){ x_rotacion = _rx; };
void  Camera::addXRotacion(float _rx){ x_rotacion += _rx; };

float Camera::getRotacionY() const{ return y_rotacion; };
void  Camera::setRotacionY(float _ry){ y_rotacion = _ry; };
void  Camera::addYRotacion(float _ry){ y_rotacion += _ry; };

void  Camera::setRotacion(float _rx, float _ry){ x_rotacion = _rx; y_rotacion = _ry; };

float Camera::getZoom() const{ return 0.2f; }
float Camera::getFrontPlane() const{ return front_plane; };
float Camera::getBackPlane() const{ return back_plane; };
void  Camera::setPlane(float _front, float _back){ front_plane = _front; back_plane = _back; };

Camera::Camera(){ resize(100.f, 100.f); set(0.f, 0.f); setPlane(10.f, 1000.f); setRotacion(0.f, 0.f); };
Camera::Camera(float _x, float _y, float _w, float _h, float _front, float _back){ set(_x, _y); resize(_w, _h); setPlane(_front, _back); setRotacion(0.f, 0.f); };

#endif
