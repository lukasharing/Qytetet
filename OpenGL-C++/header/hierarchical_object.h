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
    void setLibertyValue(int, float);
    float getLibertyValue(int);
    bool sameIntColor(unsigned char[4]);
};

void Hierarchy::setLibertyValue(int d, float v){ if(d >= liberty.size()){ liberty.push_back(v); } else { liberty[d] += v; }; };
float Hierarchy::getLibertyValue(int i){ return liberty[i]; };

void Hierarchy::add_element(std::string name, Object3D* object){ models[name] = object; };

Object3D* Hierarchy::get_element(std::string name){ return models[name]; };

void Hierarchy::setDrawType(GLenum t){
  std::map<std::string, Object3D*>::iterator it;
  for(it = models.begin(); it != models.end(); ++it){
    it->second->setDrawType(t);
  }
};

bool Hierarchy::sameIntColor(unsigned char c[4]){
  bool exist = false;
  std::map<std::string, Object3D*>::iterator it;
  for(it = models.begin(); it != models.end() && !exist; ++it){
    if(it->second->sameIntColor(c)){
      exist = true;
    }
  }
  return exist;
};

#endif
