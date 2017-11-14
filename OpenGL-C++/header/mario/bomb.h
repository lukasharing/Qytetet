#ifndef MARIOBOMB_H
#define MARIOBOMB_H
#include "../hierarchical_object.h"
#include "../revolution_object.h"
#include <cmath>
class Bomb : public Hierarchy{
  private:
    int time;
  public:
    Bomb();
    void draw(long int) override;
};

Bomb::Bomb(){
  setLibertyValue(0, 0.f);
  setLibertyValue(1, 0.f);
  setLibertyValue(2, 0.f);

  Revolution* body = new Revolution("../models/model_mario/bomb/body.ply", 10, 2);
  body->setColor(0.2f, 0.2f, 0.2f);
  add_element("body", body);

  Revolution* eye = new Revolution("../models/model_mario/bomb/eye.ply", 10, 2);
  eye->setColor(0.9f, 0.9f, 0.9f);
  add_element("eye", eye);

  Revolution* lamp = new Revolution("../models/model_mario/bomb/lamp.ply", 10, 2);
  lamp->setColor(0.5f, 0.75f, 0.9f);
  add_element("lamp", lamp);

  Revolution* wick = new Revolution("../models/model_mario/bomb/wick.ply", 10, 2);
  wick->setColor(1.f, 1.f, 1.f);
  add_element("wick", wick);

  Revolution* wick_burnt = new Revolution("../models/model_mario/bomb/wick.ply", 10, 2);
  wick_burnt->setColor(0.4f, 0.4f, 0.4f);
  add_element("wick_burnt", wick_burnt);

  Revolution* needle_hook = new Revolution("../models/model_mario/bomb/needle_hook.ply", 10, 1);
  needle_hook->setColor(.5f, .25f, 0.2f);
  needle_hook->setCover(60);
  add_element("needle_hook", needle_hook);

  Revolution* needle_body = new Revolution("../models/model_mario/bomb/needle_body.ply", 10, 0);
  needle_body->setColor(.5f, .25f, 0.2f);
  add_element("needle_body", needle_body);

  Revolution* leg = new Revolution("../models/model_mario/bomb/leg.ply", 10, 2);
  leg->setColor(.9f, .58f, 0.1f);
  add_element("leg", leg);

  Revolution* feet = new Revolution("../models/model_mario/bomb/feet.ply", 10, 1, -2.f);
  feet->setColor(.9f, .58f, 0.1f);
  add_element("feet", feet);
};

void Bomb::draw(long int delta){
  glEnableClientState(GL_VERTEX_ARRAY);
  glPushMatrix();
    glScalef(scale, scale, scale);
    get_element("body")->draw(delta);

    //EYE 1
    glPushMatrix();
      glTranslatef(-0.3f, 0.90f, 0.f);
      get_element("eye")->draw(delta);
    glPopMatrix();

    // EYE 2
    glPushMatrix();
      glTranslatef(0.3f, 0.90f, 0.f);
      get_element("eye")->draw(delta);
    glPopMatrix();

    // LAMP
    glPushMatrix();
      glTranslatef(0.f, 0.f, 0.9f);
      get_element("lamp")->draw(delta);
      float size = std::abs(std::cos(delta / 10000.f)/3.f * getLibertyValue(0)) + 0.1;
      glTranslatef(0.f, 0.f, 0.3f);
      glScalef(1.f, 1.f, size);
      get_element("wick")->draw(delta);
      glTranslatef(0.f, 0.f, 1.f);
      glScalef(1.f, 1.f, 0.3f);
      glRotatef(3, 1.f, 0.f, 0.f);
      get_element("wick_burnt")->draw(delta);
    glPopMatrix();

    // Needle
    glPushMatrix();
      glRotatef(90, 0.f, 0.f, 1.f);
      glTranslatef(-1.f, 0.f, 0.f);
      glRotatef((int)(delta/60) * getLibertyValue(1), 1.f, 0.f, 0.f);
      get_element("needle_body")->draw(delta);
      glPushMatrix();
        glTranslatef(-.4f, 0.f, -0.25f);
        get_element("needle_hook")->draw(delta);
      glPopMatrix();
      glPushMatrix();
        glTranslatef(-.4f, 0.f, 0.25f);
        get_element("needle_hook")->draw(delta);
      glPopMatrix();
    glPopMatrix();

    // Leg 1
    glPushMatrix();
      glTranslatef(0.f, 0.f, -.9f);
      float rm = std::sin(delta/1000.f * getLibertyValue(2)) * 180.f / 3.14f;
      glPushMatrix();
        glTranslatef(-0.3f, 0.0f, 0.0f);
        glRotatef(-rm, 1.0, 0.0, 0.0);
        get_element("leg")->draw(delta);
        glTranslatef(0.f, -.2f, -.45f);
        get_element("feet")->draw(delta);
      glPopMatrix();
      glPushMatrix();
        glTranslatef(0.3f, 0.0f, 0.0f);
        glRotatef(rm, 1.0, 0.0, 0.0);
        get_element("leg")->draw(delta);
        glTranslatef(0.f, -.2f, -.45f);
        get_element("feet")->draw(delta);
      glPopMatrix();
    glPopMatrix();
  glPopMatrix();
};
#endif
