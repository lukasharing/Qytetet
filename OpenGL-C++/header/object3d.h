#ifndef OBJECT3D_H
#define OBJECT3D_H
#include <GL/gl.h>
#include <GL/glut.h>

class Object3D{
  private:
    float* v;// Vertices
    int* l; // Edges
    int c; // Faces

    // Transformations
    float scale;
    unsigned int color;
  public:

    void dibujar();

    // setter
    void setCaras(int);
    void setScalado(float);
    void setColor(int);
    void setVertices(float*);

    // getter
    int getColor();
    float* getVertices();

    void setLados(int*);

    Object3D();
    Object3D(float*, int*, int c);
};

void Object3D::setCaras(int _caras){ c = _caras; };
void Object3D::setScalado(float _scale){ scale = _scale; };
void Object3D::setColor(int _color){ color = _color; };
void Object3D::setVertices(float* _v){ v = _v; };

float* Object3D::getVertices(){ return v; };
int Object3D::getColor(){ return color; };

void Object3D::setLados(int* _l){ l = _l; };

void Object3D::dibujar(){
  if(c > 0){
    glEnableClientState(GL_VERTEX_ARRAY);
    glPushMatrix();
      glScalef(scale, scale, scale);
    	glColor3f((color>>16&0xff)/255.f, (color>>8&0xff)/255.f, (color&0xff)/255.f);
      glVertexPointer(3, GL_FLOAT, 0, v);
      glDrawElements(GL_LINE_STRIP, c * 3, GL_UNSIGNED_INT, l);
    glPopMatrix();

    glDisableClientState(GL_VERTEX_ARRAY);
  }
};

Object3D::Object3D(){ v = NULL; l = NULL; c = 0; scale = 1.f; color = 0; };
Object3D::Object3D(float* _v, int* _l, int _c){ v = _v; l = _l; c = _c; scale = 1.f; color = 0; };

#endif
