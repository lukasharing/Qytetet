#ifndef HIERARCHY3D_H
#define HIERARCHY3D_H
#include<vector>
#include<map>
#include<string>
#include "object3d.h"

class Hierarchy : public Object3D{
  private:
    std::map<std::string, Object3D*> models;
    std::vector<float> liberty;

  public:
    void add_element(std::string, Object3D*);
    Object3D* get_element(std::string);
    void setDrawType(GLenum) override;
    void setChess(bool) override;
    void setLibertyValue(int, float);
    float getLibertyValue(int);
};

void Hierarchy::setLibertyValue(int d, float v){ if(d >= liberty.size()){ liberty.push_back(v); } else { liberty[d] += v; }; };
float Hierarchy::getLibertyValue(int i){ return liberty[i]; };

void Hierarchy::add_element(std::string name, Object3D* object){ models[name] = object; };

Object3D* Hierarchy::get_element(std::string name){ return models[name]; };

void Hierarchy::setDrawType(GLenum t){
  for (std::map<std::string, Object3D*>::iterator it = models.begin(); it != models.end(); ++it){
    it->second->setDrawType(t);
  }
};

void Hierarchy::setChess(bool t){
  for (std::map<std::string, Object3D*>::iterator it = models.begin(); it != models.end(); ++it){
    it->second->setChess(t);
  }
};

#endif
