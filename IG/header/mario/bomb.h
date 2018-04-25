#ifndef MARIOBOMB_H
#define MARIOBOMB_H
#include "../hierarchical_object.h"
#include "../revolution_object.h"
#include "../vector3d.h"
#include <cmath>
class Bomb : public Hierarchy{
  private:
    int time;
    Vector3D position;
    Vector3D velocity;
  public:
    Bomb();
    void draw(long int, bool) override;

    Vector3D& getPosition(){ return position; };
    Vector3D& getVelocity(){ return velocity; };
};

Bomb::Bomb(){
  setLibertyValue(0, 0.f);
  setLibertyValue(1, 0.f);
  setLibertyValue(2, 0.f);
  position = Vector3D(0.0, 0.0, 0.0);
  velocity = Vector3D(0.0, 0.0, 0.0);

  Revolution* body = new Revolution("../models/model_mario/bomb/body.ply", 10, 1);
  body->setColor(0.2f, 0.2f, 0.2f);
  add_element("body", body);

  Revolution* eye = new Revolution("../models/model_mario/bomb/eye.ply", 10, 1);
  eye->setColor(0.9f, 0.9f, 0.9f);
  add_element("eye", eye);

  Revolution* lamp = new Revolution("../models/model_mario/bomb/lamp.ply", 10, 1);
  lamp->setColor(0.5f, 0.75f, 0.9f);
  add_element("lamp", lamp);

  Revolution* wick = new Revolution("../models/model_mario/bomb/wick.ply", 10, 1);
  wick->setColor(1.f, 1.f, 1.f);
  add_element("wick", wick);

  Revolution* wick_burnt = new Revolution("../models/model_mario/bomb/wick.ply", 10, 1);
  wick_burnt->setColor(0.4f, 0.4f, 0.4f);
  add_element("wick_burnt", wick_burnt);

  Revolution* needle_hook = new Revolution("../models/model_mario/bomb/needle_hook.ply", 10, 0);
  needle_hook->setColor(.5f, .25f, 0.2f);
  needle_hook->setCover(60);
  add_element("needle_hook", needle_hook);

  Revolution* needle_body = new Revolution("../models/model_mario/bomb/needle_body.ply", 10, 2);
  needle_body->setColor(.5f, .25f, 0.2f);
  add_element("needle_body", needle_body);

  Revolution* leg = new Revolution("../models/model_mario/bomb/leg.ply", 10, 1);
  leg->setColor(.9f, .58f, 0.1f);
  add_element("leg", leg);

  Revolution* feet = new Revolution("../models/model_mario/bomb/feet.ply", 10, 2, 2.f);
  feet->setColor(.9f, .58f, 0.1f);
  add_element("feet", feet);
};

void Bomb::draw(long int delta, bool ex){
  position += velocity;
  velocity *= 0.4;

  glPushMatrix();
    glTranslatef(position.getX(), 0.0, position.getZ());
    glTranslatef(0.f, 1.4f, 0.0f);
    get_element("body")->draw(delta, ex);

    // EYES
    glPushMatrix();
      //EYE 1
      glPushMatrix();
        glTranslatef(-0.3f, 0.f, -0.90f);
        get_element("eye")->draw(delta, ex);
      glPopMatrix();

      // EYE 2
      glPushMatrix();
        glTranslatef(0.3f, 0.f, -0.90f);
        get_element("eye")->draw(delta, ex);
      glPopMatrix();
    glPopMatrix();

    // LAMP
    glPushMatrix();
      glTranslatef(0.f, 0.9f, 0.f);
      get_element("lamp")->draw(delta, ex);
      float size = std::abs(std::cos(delta / 10000.f)/3.f * getLibertyValue(0)) + 0.1;
      glTranslatef(0.f, 0.3f, 0.f);
      glScalef(1.f, size, 1.f);
      get_element("wick")->draw(delta, ex);
      glTranslatef(0.f, 1.f, 0.f);
      glScalef(1.f, 0.3f, 1.f);
      glRotatef(3, 1.f, 0.f, 0.f);
      get_element("wick_burnt")->draw(delta, ex);
    glPopMatrix();

    // Needle
    glPushMatrix();
      glTranslatef(0.f, 0.f, 1.2f);
      glRotatef((int)(delta/60) * getLibertyValue(1), 1.f, 0.f, 0.f);
      get_element("needle_body")->draw(delta, ex);
      glTranslatef(0.0f, 0.f, 0.25f);
      glPushMatrix();
        glTranslatef(0.0f, -0.25f, 0.f);
        get_element("needle_hook")->draw(delta, ex);
      glPopMatrix();
      glPushMatrix();
        glTranslatef(0.0f, 0.25f, 0.f);
        get_element("needle_hook")->draw(delta, ex);
      glPopMatrix();
    glPopMatrix();

    // Legs
    glPushMatrix();
      glTranslatef(0.f, -.9f, 0.f);
      float rm = std::sin(delta/1000.f * getLibertyValue(2)) * 180.f / 3.14f;
      glPushMatrix();
        glTranslatef(-0.3f, 0.0f, 0.0f);
        get_element("leg")->draw(delta, ex);
        glTranslatef(0.f, -0.5f, -.45f);
        get_element("feet")->draw(delta, ex);
      glPopMatrix();
      glPushMatrix();
        glTranslatef(0.3f, 0.0f, 0.0f);
        get_element("leg")->draw(delta, ex);
        glTranslatef(0.f, -0.5f, -.45f);
        get_element("feet")->draw(delta, ex);
      glPopMatrix();
    glPopMatrix();
  glPopMatrix();
};
#endif
