#ifndef COMPORTAMIENTOJUGADOR_H
#define COMPORTAMIENTOJUGADOR_H

#include "comportamientos/comportamiento.hpp"
# define M_PI           3.14159265358979323846
#include <iostream>
#include <list>
#include <cmath>
#include <queue>
#include <list>
#include <stack>

// Distancia matemática
inline int ds(int di, int dj){ return (abs(di) + abs(dj)); }

struct estado{
    estado(){};
    estado(const estado& cpy){
        fila = cpy.fila;
        columna = cpy.columna;
        orientacion = cpy.orientacion;
    };
    estado(int i, int j, int _o):fila(j),columna(i),orientacion(_o){};
    int fila; // j
    int columna; // i
    int orientacion;
    inline bool operator == (const estado& a) const{ return (a.columna == columna && a.fila == fila); };
    inline bool operator != (const estado& a) const{ return (a.columna != columna || a.fila != fila); };
    inline int distance(const estado& a) const{ return ds(columna - a.columna, fila - a.fila); };
};

// Definimos nuestro struct Nodo A*
struct a_node{
    a_node(estado _e, a_node* _c, int _h, int _g):e(_e), c(_c), h(_h), g(_g){};
    a_node(const a_node& n){
        e = n.e;
        c = n.c;
        h = n.h;
        g = n.g;
    };
    estado e;
    a_node* c;
    int h;
    int g;
    bool operator < (const a_node& a) const{
        return (h + g) > (a.h + a.g);
    };
};

class ComportamientoJugador : public Comportamiento {
  public:
    ComportamientoJugador(unsigned int size) : Comportamiento(size) {
      // Inicializar Variables de Estado
      fil = col = -1;
      brujula = 0; // 0: Norte, 1:Este, 2:Sur, 3:Oeste
      destino.fila = -1;
      destino.columna = -1;
      destino.orientacion = -1;
    }
    ComportamientoJugador(std::vector< std::vector< unsigned char> > mapaR) : Comportamiento(mapaR) {
      // Inicializar Variables de Estado
      fil = col = -1;
      brujula = 0; // 0: Norte, 1:Este, 2:Sur, 3:Oeste
      destino.fila = -1;
      destino.columna = -1;
      destino.orientacion = -1;
    }
    ComportamientoJugador(const ComportamientoJugador & comport) : Comportamiento(comport){}
    ~ComportamientoJugador(){}

    Action think(Sensores sensores);
    int interact(Action accion, int valor);
    ComportamientoJugador * clone(){return new ComportamientoJugador(*this);}

  private:
    // Declarar Variables de Estado
    int fil, col, brujula;
    estado destino;
    list<Action> plan;

    void pathFinding(Sensores&, list<Action>&);
    void PintaPlan(list<Action> plan);
};

#endif
