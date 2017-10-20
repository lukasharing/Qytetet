#include <iostream>
#include <vector>
#include <cassert>
#include <thread>
#include <random> // dispositivos, generadores y distribuciones aleatorias
#include <chrono> // duraciones (duration), unidades de tiempo
#include "Semaphore.h"

using namespace std ;
using namespace SEM ;

const int CONSUMIDORES = 3;
Semaphore estanquero(1);
vector<Semaphore> consumidores;
Semaphore mutex_fuma(1);
//**********************************************************************
// plantilla de función para generar un entero aleatorio uniformemente
// distribuido entre dos valores enteros, ambos incluidos
// (ambos tienen que ser dos constantes, conocidas en tiempo de compilación)
//----------------------------------------------------------------------

template< int min, int max > int aleatorio(){
	static default_random_engine generador( (random_device())() );
	static uniform_int_distribution<int> distribucion_uniforme( min, max ) ;
	return distribucion_uniforme( generador );
}

//----------------------------------------------------------------------
// función que ejecuta la hebra del estanquero
void funcion_hebra_estanquero(){
	while(true){
		estanquero.sem_wait();

		// Mutex nos permite escribir el cout
		int num_fumador = aleatorio<0,CONSUMIDORES-1>();
		mutex_fuma.sem_wait();
		cout << endl << "Produce para el fumador: " << num_fumador << endl;
		mutex_fuma.sem_signal();
		consumidores[num_fumador].sem_signal();
	}
}

//-------------------------------------------------------------------------
// Función que simula la acción de fumar, como un retardo aleatoria de la hebra
void fumar(int num_fumador){

	// calcular milisegundos aleatorios de duración de la acción de fumar)
	chrono::milliseconds duracion_fumar( aleatorio<20,200>() );

	// informa de que comienza a fumar
	mutex_fuma.sem_wait();
	cout << "Fumador " << num_fumador << " empieza a fumar (" << duracion_fumar.count() << " milisegundos)" << endl;
	mutex_fuma.sem_signal();

	// espera bloqueada un tiempo igual a ''duracion_fumar' milisegundos
	this_thread::sleep_for(duracion_fumar);

	// informa de que ha terminado de fumar
	mutex_fuma.sem_wait();
	cout << "Fumador " << num_fumador << "  : termina de fumar, comienza espera de ingrediente." << endl;
	mutex_fuma.sem_signal();

}

//----------------------------------------------------------------------
// función que ejecuta la hebra del fumador
void  funcion_hebra_fumador(int num_fumador){
	while(true){
		consumidores[num_fumador].sem_wait();
		mutex_fuma.sem_wait();
		cout << "Consume";
		mutex_fuma.sem_signal();
		fumar(num_fumador);
		estanquero.sem_signal();
	}
}

//----------------------------------------------------------------------

int main(){
	// declarar hebras y ponerlas en marcha
	thread hebra_productora(funcion_hebra_estanquero);
	vector<thread> hebra_consumidora;

	// Declaramos semaforos en "rojo" para cada consumidor
	// pues éstos no pueden consumir si al principio no hay nada producido.
	for(int f = 0; f < CONSUMIDORES; f++){ consumidores.push_back(Semaphore(0)); }

	// Creamos una hebra para cada consumidor.
	for(int f = 0; f < CONSUMIDORES; f++){ hebra_consumidora.push_back(thread(funcion_hebra_fumador, f)); }

	// Añadimos en la cola de ejecución.
	for (auto& hebra : hebra_consumidora){
		hebra.join();
	}
	hebra_productora.join();

	return 0;
}
