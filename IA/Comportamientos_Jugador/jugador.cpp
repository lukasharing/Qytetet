#include "../Comportamientos_Jugador/jugador.hpp"
#include "motorlib/util.h"

void ComportamientoJugador::PintaPlan(list<Action> plan){
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

bool puede_pasar(char a){ return (a != 'P' && a != 'A' && a != 'B' && a != 'M'); };

void rotar_vector(const estado& n0, const estado& n1, list<Action>& plan){
    int dr = n0.orientacion - n1.orientacion;
    for(int i = 0; i < abs(dr); ++i){
        if(dr < 0){
            plan.push_back(actTURN_R);
        }else{
            plan.push_back(actTURN_L);
        }
    }
}

void ComportamientoJugador::pathFinding(Sensores& sensores, list<Action>& plan){
    // Origen
    estado origen;
    origen.fila = fil;
    origen.columna = col;
    origen.orientacion = brujula;

    // Destino
    destino.fila = sensores.destinoF;
    destino.columna = sensores.destinoC;

    // Precondicion: nuestra ruta no es un punto fijo.
    if(origen == destino){
        return;
    }
    // Limpiamos el plan
    plan.clear();
    // Limpiamos el mapa planeado O(n^2)
    int w = mapaConPlan[0].size(), h = mapaConPlan.size();
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
    estado orig(origen);
    visited[origen.fila][origen.columna] = true;
    a_node inicio(orig, NULL, origen.distance(destino), 0);
    abiertos.push(inicio);
    cerrados.push_back(inicio);

    bool solucion = false;
    while(!abiertos.empty() && !solucion){
        // Realizamos una copia pues priority_queue es const.
        cerrados.push_back(abiertos.top());
        const a_node& actual = cerrados.back();
        if(actual.e == destino){
            solucion = true;
        }else{
            abiertos.pop();
            int o = actual.e.orientacion;
            int u = (((o ^ (o >> 1)) & 1) << 1) - 1;
            int di = u * (o & 1);
            int dj = u - di;
            // Pasamos por los vecinos
            for(int k = 1; k <= 7; k += 2){
                // Aplicando reducción de Karnaugh se pueden obtener m y d.
                int d = ((k >> 2) ^ (k >> 1)) & 1;
                int m = 1 - 2 * ((k >> 1) & 1);

                int i = actual.e.columna + d * m, j = actual.e.fila + (d - 1) * m;

                if(!visited[j][i] && (i >= 0 && j >= 0) && (i < (w - 1) && j < (h - 1)) && puede_pasar(mapaResultado[j][i])){
                    // Asociamos a dicho estado, un Nodo A*
                    visited[j][i] = true;
                    // Vemos los giros.
                    int new_dir = (k - 1 + (((k>>1)^(k>>2))&1)) % 4;
                    // Producto escalar a * b = |a||b|cos(alpha) => alpha=arcos(a.x*b.x+a.y*b.y) = abs(OAnterior - OActual).
                    int dg = abs(actual.e.orientacion - new_dir); //2 * acos(mi * di + dj * mj) / M_PI;
                    estado nuevo_estado(i, j, new_dir);
                    a_node nuevo_nodo(nuevo_estado, &cerrados.back(), nuevo_estado.distance(destino), actual.g + dg + 1);
                    abiertos.push(nuevo_nodo);
                }
            }
        }
    }
    // Reconstruimos el recorrido como un plan
    if(solucion){
        // Le damos la vuelta pues queremos empezar desde la posicion inicial
        a_node* ti = &(*--cerrados.end());
        stack<estado*> pila;
        do{
            pila.push(&ti->e);
            mapaConPlan[ti->e.fila][ti->e.columna] = 1;
        }while((ti = ti->c) != NULL);

        // Miramos en la pila
        estado* ik = pila.top();
        pila.pop();
        while(pila.size() >= 1){
            estado* im = pila.top();
            pila.pop();
            // Rotamos y almacenamos la última orientación
            rotar_vector(*ik, *im, plan);
            plan.push_back(actFORWARD);
            ik = im;
        }
        // Tiene solución
        return;
    }
    // No tiene solución
    plan.clear();
}

Action ComportamientoJugador::think(Sensores sensores){
    Action movimiento = actIDLE;
    if (sensores.mensajeF > 0 && sensores.mensajeC > 0){
		fil = sensores.mensajeF;
		col = sensores.mensajeC;
		sensores.reset = true;
	}
    // A* Origen
    if(sensores.reset || (destino.fila != sensores.destinoF && destino.fila != sensores.destinoC)){
        pathFinding(sensores, plan);
    }

    bool accion_conocida = false;
    if(!plan.empty()){
        // No hay aldeano en mi camino.
        if(sensores.superficie[2] != 'a'){
            accion_conocida = true;
            movimiento = plan.front();
            plan.erase(plan.begin());
        }

        if(movimiento == actFORWARD && !puede_pasar(sensores.terreno[2])){
            movimiento = actIDLE;
            pathFinding(sensores, plan);
        }

    }else if(puede_pasar(sensores.terreno[2])){

        /*
         Buscamos punto amarillo entre nuestros sensores
        */
        bool existe_amarillo = false;
        /*for(int i = 1; i < 16 && !existe_amarillo; ++i){
            existe_amarillo = sensores.terreno[i] == 'K';
        }*/

        if(existe_amarillo){
            char small_map[7][4];
            for(int j = 0, um = 0; j < 4; ++j){
                for(int i = -j; i <= j; ++i){
                    small_map[3+i][j] = sensores.terreno[um++];
                }
                for(int i = 0; i < 3-j; ++i){
                    small_map[6-i][j] = small_map[i][j] = 'P';
                }
            }

            queue<a_node> abiertos;
            list<a_node> cerrados;

            estado inicio_e(0, 3, 0);
            a_node inicio(inicio_e, NULL, 0, 0);
            small_map[3][0] = 'P';

            abiertos.push(inicio);
            bool encontrado = false;
            while(!abiertos.empty() && !encontrado){
                const a_node& actual = abiertos.front();
                cerrados.push_back(actual);
                abiertos.pop();

                for(int k = 1; k <= 7 && !encontrado; k += 2){
                    // Aplicando reducción de Karnaugh se pueden obtener m y d.
                    int d = ((k >> 2) ^ (k >> 1)) & 1;
                    int m = 1 - 2 * ((k >> 1) & 1);

                    int i = actual.e.columna + d * m, j = actual.e.fila + (d - 1) * m;
                    if((i >= 0 && j >= 0) && (i < 4 && j < 7) && puede_pasar(small_map[j][i])){
                        estado nuevo_estado(i, j, 0);
                        a_node nuevo_nodo(nuevo_estado, &cerrados.back(), 0, 0);
                        abiertos.push(nuevo_nodo);
                        if(small_map[j][i] == 'K'){
                            encontrado = true;
                        }
                        small_map[j][i] = 'P';
                    }
                }
            }

            /*if(encontrado){
                plan.clear();

                cout << "HAY CAMINO" << endl;
                stack<estado*> pila;
                a_node* last = &abiertos.back();
                // Damos la vuelta a la pila.
                int mm = 0;
                do{ pila.push(&last->e); }while((last = last->c) != NULL);

                // Brujula
                int u = (((brujula ^ (brujula >> 1)) & 1) << 1) - 1;
                int di = u * (brujula & 1);
                int dj = u - di;
                int ak = 2 * atan2(dj, di) / M_PI;

                // Recorremos hacia atrás.
                estado* left = pila.top();
                while(!pila.empty()){
                    estado* right = pila.top();
                    pila.pop();

                    int i = right->columna - left->columna;
                    int j = right->fila - left->fila;
                    int du = di * i + dj * j;
                    int dv = di * j + dj * i;
                    int bk = 2 * atan2(dj, di) / M_PI;

                    if(bk != ak){
                        plan.push_back(actTURN_L);
                    }else{
                        plan.push_back(actFORWARD);
                    }
                    ak = bk;
                    left = right;
                }
            }*/
        }else{
            float pb = aleatorio(10);
            if(pb <= 2){
                movimiento = actTURN_R;
            }else if(pb <= 4){
                movimiento = actTURN_L;
            }else{
                movimiento = actFORWARD;
            }
        }
    }else{
        movimiento = actTURN_R;
    }

    if(accion_conocida){
        // Dibujamos el mapa.
        // Brújula a coordenadas (x, y).
        int u = (((brujula ^ (brujula >> 1)) & 1) << 1) - 1;
        int di = u * (brujula & 1);
        int dj = u - di;

        // Hacemos visible el mapa.
        for(int j = 0, um = 0; j < 4; ++j){
            for(int i = -j; i <= j; ++i){
                // Matriz de rotación
                int du = di * i + dj * j;
                int dv = di * j - dj * i;
                if(mapaResultado[fil + du][col + dv] == '?'){
                    mapaResultado[fil + du][col + dv] = sensores.terreno[um];
                }
                ++um;
            }
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
