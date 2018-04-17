#include "../Comportamientos_Jugador/jugador.hpp"
#include "motorlib/util.h"

#include <iostream>
#include <cmath>
#include <queue>
#include <list>
#include <stack>
void ComportamientoJugador::PintaPlan(list<Action> plan) {
	auto it = plan.begin();
	while (it!=plan.end()){
		if (*it == actFORWARD){
			cout << "A ";
		}
		else if (*it == actTURN_R){
			cout << "D ";
		}
		else if (*it == actTURN_L){
			cout << "I ";
		}
		else {
			cout << "- ";
		}
		it++;
	}
	cout << endl;
}

// Definimos nuestro struct Nodo A*
struct a_node{
    int i, j;
    a_node* c;
    int h;
    int g;
    bool operator < (const a_node& a) const{ return (a.h + a.g) < (h + g); };
    bool operator == (const a_node& a) const{ return (j == a.j && i == a.i); };
    bool operator != (const a_node& a) const{ return (j != a.j || i != a.i); };
    bool operator == (const estado& a) const{ return (j == a.columna && i == a.fila); };
};

// Distancia matemática
inline int ds(int a, int b){ return (abs(a) + abs(b)); }

// Distancias
inline int distancia(const a_node& a, const estado& b){ return ds(a.i - b.fila, a.j - b.columna); }

bool puede_pasar(char a){ return (a != 'P' && a != 'A' && a != 'B' && a != 'M'); };

int rotar_vector(const a_node& n0, const a_node& n1, int h, list<Action>& plan){
    // ATAN(-dy, dx)
    int dx = n1.i - n0.i;
    int dy = n0.j - n1.j;

    int h0 = h;
    bool k1 = true;
    do{
        int k = (((h0 ^ (h0 >> 1)) & 1) << 1) - 1;
        int jj = k * (h0 & 1);
        int ii = k - jj;
        if(ii == dx && jj == dy){
            k1 = false;
        }else{
            int rot = 1;
            //double at2 = atan2(dy, dx) - atan2(jj, ii);
            //if(at2 < 0) rot = -1;
            // actTURN_R => rot = -1;
            h0 = (((h0 + rot) % 4) + 4) % 4;
            if(rot == 1){
                plan.push_back(actTURN_L);
            }else{
                plan.push_back(actTURN_R);
            }
        }
    }while(k1);
    return h0;
}

bool ComportamientoJugador::pathFinding(const estado& origen, const estado& destino, list<Action>& plan){
    if(origen == destino) return false;
    plan.clear();
    // Limpiamos el mapa planeado
    int w = mapaConPlan.size(), h = mapaConPlan[0].size();
    bool visited[w][h];
    for(int j = 0; j < h; ++j){
        for(int i = 0; i < w; ++i){
            mapaConPlan[i][j] = 0;
            visited[i][j] = false;
        }
    }
    // STD Necesarios
    priority_queue<a_node> abiertos;
    list<a_node> cerrados;

    // Declaramos nuestro primer nodo.
    a_node inicio;
    inicio.i = origen.fila;
    inicio.j = origen.columna;
    inicio.h = distancia(inicio, destino);
    inicio.g = 0;
    inicio.c = &inicio;

    abiertos.push(inicio);
    cerrados.push_back(inicio);

    bool solucion = false;
    while(!abiertos.empty() && !solucion){
        a_node actual = abiertos.top();
        cerrados.push_back(actual);

        if(actual == destino){
            solucion = true;
        }else{
            abiertos.pop();
            visited[actual.i][actual.j] = true;

            // Pasamos por los vecinos
            for(int k = 1; k <= 7; k += 2){
                // Aplicando reducción de Karnaugh se pueden obtener m y d.
                int d = ((k >> 2) ^ (k >> 1)) & 1;
                int m = 1 - 2 * ((k >> 1) & 1);
                int i = actual.i + d * m, j = actual.j + (d - 1) * m;
                if(!visited[i][j] && (i >= 0 && j >= 0) && ((i + 1) < w && (j + 1) < h) && puede_pasar(mapaResultado[i][j])){
                    // Asociamos a dicho estado, un Nodo A*
                    visited[i][j] = true;
                    a_node nuevo;
                    nuevo.i = i;
                    nuevo.j = j;
                    nuevo.h = distancia(nuevo, destino);
                    nuevo.g = actual.g + 1;
                    nuevo.c = &cerrados.back();

                    abiertos.push(nuevo);
                }
            }
        }
    }
    // Reconstruimos el recorrido como un plan
    if(solucion){
        list<a_node>::iterator it = --cerrados.end();
        // Maximo 4 pasos (4 vecinos añadidos)
        while(*it != abiertos.top()) --it;
        a_node* ti = &(*it);
        // Le damos la vuelta pues queremos empezar desde la posicion inicial
        stack<a_node*> pila;
        do{
            pila.push(ti);
            mapaConPlan[ti->i][ti->j] = 1;
        }while((ti = ti->c) != &inicio);

        // Miramos en la pila
        int h0 = h;
        do{
            a_node* ik = pila.top();
            pila.pop();
            a_node* im = pila.top();
            // Rotamos y almacenamos la última horientación
            h0 = rotar_vector(*ik, *im, h0, plan);
            plan.push_back(actFORWARD);
        }while(pila.size() > 2);
        // Ultima rotación
        rotar_vector(*pila.top(), *it, h0, plan);
        plan.push_back(actFORWARD);

        // Tiene solución
        return true;
    }
    // No tiene solución
    return false;
}

Action ComportamientoJugador::think(Sensores sensores){
    Action movimiento = actIDLE;
    if (sensores.mensajeF != -1){
		fil = sensores.mensajeF;
		col = sensores.mensajeC;
	}

    // A* Origen
    if(plan.empty() || sensores.reset){
        estado origen;
        origen.fila = fil;
        origen.columna = col;
        origen.orientacion = brujula;
        // A* Destino
        estado destino;
        destino.fila = sensores.destinoF;
        destino.columna = sensores.destinoC;

        bool puede_llegar = pathFinding(origen, destino, plan);
        if(!puede_llegar){ plan.clear(); }
    }

    if(!plan.empty()){
        // Hay plan, nos movemos
        movimiento = plan.front();
        plan.erase(plan.begin());
    }else{
        // No hay plan, decidimos hacer otra cosa
        cout << "SIN PLAN" << endl;
        int total = 0;
        if(puede_pasar(sensores.superficie[0])){
            for(int i = 0; i < 9; ++i){
                total += puede_pasar(sensores.superficie[i]);
            }
            double k = total / 9;
            if(aleatorio(20) < (int)(k * 20)){
                movimiento = actFORWARD;
            }else{
                movimiento = actTURN_R;
            }
        }else{
            movimiento = actTURN_R;
        }
    }

    // Actualizamos las fil y col según el movimiento
    switch (movimiento){
        case actTURN_R: brujula = (brujula + 1) % 4; break;
        case actTURN_L: brujula = (brujula + 3) % 4; break;
        case actFORWARD:
            switch (brujula){
                case 0: fil--; break;
                case 1: col++; break;
                case 2: fil++; break;
                case 3: col--; break;
            }
        break;
    }

    return movimiento;
}

int ComportamientoJugador::interact(Action accion, int valor){
  return false;
}
