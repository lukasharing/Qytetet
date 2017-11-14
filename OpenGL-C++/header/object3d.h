#ifndef OBJECT3D_H
#define OBJECT3D_H
#include <GL/gl.h>
#include <iostream>
#include <fstream>
#include <string>
#include "vector3d.h"
#include "camera.h"

class Object3D{
  protected:
    std::string name;

    std::vector<float> normals;// normals
    std::vector<float> vertices;// Vertices
    std::vector<unsigned int> sides; // Sides
    std::vector<float> colors;

    float color[3] = {0.5f, 0.3f, 0.4f};
    int covers;

    // Properties
    bool chess;
    GLenum draw_type;

    // Transformations
    float scale;
    Vector3D position; // Posicion
    Vector3D rotation; // Rotacion
  public:
    void setColor(float r, float g, float b){ color[0]=r; color[1]=g; color[2]=b; }
    // PLY
    void load_ply(std::string);

    virtual void draw(long int);

    // Transformaciones
    float getScale() const;
    void setScale(float);
    Vector3D& getPosition();
    Vector3D& getRotation();

    // Drawing type
    virtual void setDrawType(GLenum);

    // console
    void console_vertices();
    void console_faces();

    // getter
    virtual void setChess(bool);
    void setName(std::string);
    void setVertices(float*);
    std::string getName();

    void normal_calculation();
    void new_object();
    // Empty
    Object3D();
    // Load From Arrays
    Object3D(float*, int*, int, int);
    // Load From PLY
    Object3D(std::string);
    ~Object3D();
};


/* Get and Set Methods (No explanation needed). */
float Object3D::getScale() const{  return scale; };
void Object3D::setScale(float _scale){ scale = _scale; };
Vector3D& Object3D::getPosition(){ return position; };
Vector3D& Object3D::getRotation(){ return rotation; };

void Object3D::setDrawType(GLenum t){ draw_type = t; };

void Object3D::setChess(bool _c){ chess = _c; };
void Object3D::setName(std::string _n){ name = _n; };
std::string Object3D::getName(){ return name; };


/* Draw method, need 1 parameter, the camera */
void Object3D::draw(long int delta){
  if(sides.size() > 0){
    glEnableClientState(GL_VERTEX_ARRAY);
    glPushMatrix();
      glTranslated(position.getX(), position.getY(), position.getZ());
      glScalef(scale, scale, scale);

      glPolygonMode(GL_FRONT, draw_type);
      glPolygonMode(GL_BACK, draw_type);
      if(colors.size() > 0 && !chess){
        glEnableClientState(GL_COLOR_ARRAY);
        glColorPointer(3, GL_FLOAT, 0, &colors[0]);
      }else{
        glColor3f(color[0], color[1], color[2]);
      }
      glVertexPointer(3, GL_FLOAT, 0, &vertices[0]);
      if(chess){
        glDrawElements(GL_TRIANGLES, sides.size()/2, GL_UNSIGNED_INT, &sides[0]);
        glColor3f(1.f, 0.3f, 0.4f);
        glDrawElements(GL_TRIANGLES, sides.size()/2, GL_UNSIGNED_INT, &sides[sides.size()/2]);
      }else{
        glDrawElements(GL_TRIANGLES, sides.size() - covers, GL_UNSIGNED_INT, &sides[0] + covers);
      }
      //glDrawElements(GL_TRIANGLES, sides.size(), GL_UNSIGNED_INT, &sides[sides.size()/3-1]);

    glPopMatrix();
    glDisableClientState(GL_VERTEX_ARRAY);

    if(colors.size() > 0 && !chess){
      glDisableClientState(GL_COLOR_ARRAY);
    }
  }
};

/* Normal calculation method (No parameters) */
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

void Object3D::console_vertices(){
  std::cout << "Vertices from the object" << std::endl;
  for(int i = 0; i < vertices.size() / 3; i++){
    for(int j = 0; j < 3; j++){
      std::cout << vertices[i * 3 + j] << ',';
    }
    std::cout << std::endl;
  }
};

void Object3D::console_faces(){
  std::cout << "Faces from the object" << std::endl;
  for(int i = 0; i < sides.size() / 3; i++){
    for(int j = 0; j < 3; j++){
      std::cout << sides[i * 3 + j] << ',';
    }
    std::cout << std::endl;
  }
};

/* New object method */
void Object3D::new_object(){
  covers = 0;
  draw_type = GL_FILL;
  position.new_object();
  rotation.new_object();
  scale = 1.f;
  chess = false;
};

/* Empty contructor */
Object3D::Object3D(){
  new_object();
};

/* Constructor by vectors */
Object3D::Object3D(float* _v, int* _s, int total_vertices, int total_sides){
  new_object();
  vertices.assign(_v, _v + total_vertices);
  sides.assign(_s, _s + total_sides);
  normal_calculation();
};

/* Constructor by path, it has to be existent. */
Object3D::Object3D(std::string name){
  new_object();
  load_ply(name);
  normal_calculation();
};

void Object3D::load_ply(std::string name){
  std::ifstream ply_object(name.c_str());
  if (ply_object.is_open()){
    std::string str;
    ply_object >> str;
    if(str == "ply"){
      std::cout << "Loading " << name << " PLY File...\n";
      ply_object >> str;
      while(str != "end_header"){
        if(str == "element"){
          ply_object >> str;
          int size;
          ply_object >> size;
          std::cout << str << " -> " << size << "\n";
          if(str == "vertex"){ vertices.resize(3 * size); }
          else if(str == "face"){ sides.resize(3 * size); }
        }
        ply_object >> str;
      }

      // Load Vertex
      for(int i = 0; i < vertices.size(); i++){
        float v;
        ply_object >> v;
        vertices[i] = v;
      }
      // Load Edges
      for(int i = 0; i < sides.size(); i++){
        int e;
        if(i % 3 == 0){ ply_object >> e; }
        ply_object >> e;
        sides[i] = e;
      }
    }
  }
  ply_object.close();
}

/* Destructor. */
Object3D::~Object3D(){
  normals.clear();// normals
  vertices.clear();// Vertices
  sides.clear(); // Sides
  colors.clear();
};
#endif
