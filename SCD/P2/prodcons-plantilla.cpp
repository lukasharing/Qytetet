#include <iostream>
#include <cassert>
#include <thread>
#include <mutex>
#include <random>
#include "Semaphore.h"

using namespace std;
using namespace SEM;

//**********************************************************************
// variables compartidas

const int num_items = 40, buffer_size = 10;   // tamaño del buffer
unsigned int buffer[buffer_size] = {0};
unsigned int cont_prod[num_items] = {0}; // contadores de verificación: producidos
unsigned int cont_cons[num_items] = {0}; // contadores de verificación: consumidos
Semaphore hay_productos(0);
Semaphore producto_completo(buffer_size);
Semaphore mutex_fuma(1);
unsigned int contador = 0, libre = 0;

//**********************************************************************
// plantilla de función para generar un entero aleatorio uniformemente
// distribuido entre dos valores enteros, ambos incluidos
// (ambos tienen que ser dos constantes, conocidas en tiempo de compilación)
//----------------------------------------------------------------------
template<int min, int max> int aleatorio(){
	static default_random_engine generador((random_device())());
	static uniform_int_distribution<int> distribucion_uniforme(min, max) ;
	return distribucion_uniforme(generador);
}


//**********************************************************************
// funciones comunes a las dos soluciones (fifo y lifo)
//----------------------------------------------------------------------

int producir_dato(){
	this_thread::sleep_for(chrono::milliseconds(aleatorio<20, 100>()));
	mutex_fuma.sem_wait();
	cout << "Producido: " << contador << endl << flush;
	mutex_fuma.sem_signal();
	cont_prod[contador]++;
	return (contador = (contador+1) % num_items);
}
//----------------------------------------------------------------------

void consumir_dato(unsigned dato){
	assert(dato < num_items);
	cont_cons[dato]++;
	this_thread::sleep_for(chrono::milliseconds(aleatorio<20, 100>()));
	mutex_fuma.sem_wait();
	cout << "Consumido: " << dato << endl ;
	mutex_fuma.sem_signal();
}


//----------------------------------------------------------------------

void test_contadores(){
	bool ok = true ;
	cout << "Comprobando contadores ...." << endl;
	for(unsigned i = 0; i < num_items; i++){
		if (cont_prod[i] != 1){
			cout << "error: Valor " << i << " producido " << cont_prod[i] << " veces." << endl;
			ok = false ;
		}
	}
	if (ok){
		cout << "Solución (aparentemente) correcta." << endl << flush;
	}
}

//----------------------------------------------------------------------

void  funcion_hebra_productora(){
	for(unsigned i = 0 ; i < num_items; i++){
		int dato = producir_dato();
		producto_completo.sem_wait();
		mutex_fuma.sem_wait();
		buffer[libre++] = dato;
		mutex_fuma.sem_signal();
		hay_productos.sem_signal();
	}
}

//----------------------------------------------------------------------

void funcion_hebra_consumidora(){
	for(unsigned i = 0 ; i < num_items; i++){
		hay_productos.sem_wait();
		mutex_fuma.sem_wait();
		int dato = buffer[--libre];
		mutex_fuma.sem_signal();
		producto_completo.sem_signal();
		consumir_dato(dato);
	}
}
//----------------------------------------------------------------------

int main(){
	cout << "--------------------------------------------------------" << endl;
	cout << "Problema de los productores-consumidores (solución LIFO)." << endl;
	cout << "--------------------------------------------------------" << endl;
	cout << flush;

	thread hebra_productora(funcion_hebra_productora);
	thread hebra_consumidora(funcion_hebra_consumidora);

	hebra_productora.join();
	hebra_consumidora.join();

	test_contadores();
	return 1;
}
