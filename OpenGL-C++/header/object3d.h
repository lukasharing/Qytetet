#ifndef OBJECT3D_H
#define OBJECT3D_H
#include <GL/gl.h>
#include <GL/glut.h>
#include <cmath>
#include "vector3d.h"
#define PI 3.14159265358979323846

class Object3D{
  protected:
    std::string name;

    std::vector<float> normals;// normals
    std::vector<float> vertices;// Vertices
    std::vector<unsigned int> sides; // Sides

    // Properties
    bool  solid;

    Vector3D position; // Posicion
    Vector3D rotation; // Rotacion

    // Transformations
    float scale;
    float color[3];
  public:

    void draw(double, double, double);

    // Transformaciones
    float getScale();
    void setScale(float);
    Vector3D& getPosition();
    Vector3D& getRotation();

    // color setter
    void setColor(int);
    void setColor(float, float, float);
    void setColor(int, int, int);
    void setColor(float*);

    void setName(std::string);
    std::string getName();
    void setVertices(float*);

    // getter
    int getColor() const;

    void normal_calculation();
    void new_object();
    Object3D();
    Object3D(float*, int*, int, int);
};

float Object3D::getScale(){  return scale; };
void Object3D::setScale(float _scale){ scale = _scale; };
Vector3D& Object3D::getPosition(){ return position; };
Vector3D& Object3D::getRotation(){ return rotation; };

void Object3D::setColor(int _c){ setColor((_c>>16)&0xff, (_c>>8)&0xff, _c&0xff); };
void Object3D::setColor(float r, float g, float b){ color[0] = r; color[1] = g; color[2] = b; };
void Object3D::setColor(int r, int g, int b){ color[0] = r/255.f; color[1] = g/255.f; color[2] = b/255.f; };
void Object3D::setColor(float* c){ for(int i = 0; i < 3; i++){ color[i] = c[i]; } };

void Object3D::setName(std::string _n){ name = _n; };
std::string Object3D::getName(){ return name; };
int Object3D::getColor() const{ return ((int)(color[0]*255)<<16|(int)(color[1]*255)<<8|(int)(color[2]*255)); };

void Object3D::draw(double _x, double _y, double _z){
  if(sides.size() > 0){
    glEnableClientState(GL_VERTEX_ARRAY);
    glPushMatrix();
      glTranslated(position.getX(), position.getY(), position.getZ());
      glScalef(scale, scale, scale);
      glRotatef(rotation.getX() * 180 / PI, 0.f, 1.f, 0.f);
      glRotatef(rotation.getY() * 180 / PI, 1.f, 0.f, 0.f);

      glBegin(GL_TRIANGLES);
        for(int i = 0; i < sides.size(); i+= 3){
          float x = normals[i + 0] + position.getX();
          float y = normals[i + 1] + position.getY();
          float z = normals[i + 2] + position.getZ();

          float cs = cos(rotation.getX()), sn = sin(rotation.getX());

          float tx = x;
          float ty = y * cs - z * sn;
          float tz = y * sn + z * cs;

          cs = cos(rotation.getY());
          sn = sin(rotation.getY());
          float t1x = tx * cs + tz * sn;
          float t1y = ty;
          float t1z = tz * cs - tx * sn;

          float dx = t1x - _x;
          float dy = t1y - _y;
          float dz = t1z - _z;

          float scale = sqrt(dx * dx + dy * dy + dz * dz) / 10.f;
          glColor3f(color[0] * scale, color[1] * scale, color[2] * scale);
          for(int j = 0; j < 3; j++){
            int side = 3 * sides[i + j];
            glVertex3f(vertices[side + 0], vertices[side + 1], vertices[side + 2]);
          }
        }
      glEnd();

      /*glVertexPointer(3, GL_FLOAT, 0, &vertices[0]);
      glDrawElements(solid ? GL_TRIANGLES : GL_LINE_STRIP, sides.size(), GL_UNSIGNED_INT, &sides[0]);*/
    glPopMatrix();

    glDisableClientState(GL_VERTEX_ARRAY);
  }
};


void Object3D::normal_calculation(){
  int total = sides.size();
  normals.resize(total);
  for(int i = 0; i < total; i += 3){
    float cx = vertices[3 * sides[i + 0] + 0];
    float x1 = cx - vertices[3 * sides[i + 1] + 0];
    float x2 = cx - vertices[3 * sides[i + 2] + 0];

    float cy = vertices[3 * sides[i + 0] + 1];
    float y1 = cy - vertices[3 * sides[i + 1] + 1];
    float y2 = cy - vertices[3 * sides[i + 2] + 1];

    float cz = vertices[3 * sides[i + 0] + 2];
    float z1 = cz - vertices[3 * sides[i + 1] + 2];
    float z2 = cz - vertices[3 * sides[i + 2] + 2];

    float nx = y1 * z2 - z1 * y2;
    float ny = z1 * x2 - x1 * z2;
    float nz = x1 * y2 - y1 * x2;
    float md = sqrt(nx * nx + ny * ny + nz * nz);
    normals[i + 0] = nx / md;
    normals[i + 1] = ny / md;
    normals[i + 2] = nz / md;
  }
};

void Object3D::new_object(){
  position.new_object();
  rotation.new_object();
  scale = 1.f;
  color[0] = color[1] = color[2] = 0.f;
  solid = false;
};

Object3D::Object3D(){
  new_object();
};

Object3D::Object3D(float* _v, int* _s, int total_vertices, int total_sides){
  new_object();
  vertices.assign(_v, _v + total_vertices);
  sides.assign(_s, _s + total_sides);
  normal_calculation();
  solid = false;
};
#endif
