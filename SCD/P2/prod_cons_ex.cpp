#include <iostream>
#include <cassert>
#include <thread>
#include <vector>
#include <mutex>
#include <random>
#include "Semaphore.h"

using namespace std;
using namespace SEM;

//**********************************************************************
// variables compartidas

const int num_items = 40, buffer_size = 10, consumidores = 2;   // tamaño del buffer
unsigned int buffer[buffer_size] = {0};
unsigned int cont_prod[num_items] = {0}; // contadores de verificación: producidos
unsigned int cont_cons[num_items] = {0}; // contadores de verificación: consumidos
Semaphore hay_productos(0);
Semaphore producto_completo(buffer_size);
Semaphore mutex_fuma(1);
Semaphore imprimir_valor(0);
unsigned int contador = 0, libre = 0, valor_consumido = 0;

vector<Semaphore> intercala;

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

int producir_dato(int hebra){
	this_thread::sleep_for(chrono::milliseconds(aleatorio<20, 100>()));
	mutex_fuma.sem_wait();
	cout << "Producido: " << contador << " por la hebra " << hebra << endl << flush;
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
			ok = false;
		}
	}
	if (ok){
		cout << "Solución (aparentemente) correcta." << endl << flush;
	}
}

//----------------------------------------------------------------------

void  funcion_hebra_productora(int num_hebra){
	for(unsigned i = 0 ; i < num_items/2; i++){
		intercala[num_hebra].sem_wait();
		int dato = producir_dato(num_hebra);
		producto_completo.sem_wait();
		mutex_fuma.sem_wait();
		buffer[libre++] = dato;
		mutex_fuma.sem_signal();
		hay_productos.sem_signal();
		intercala[(num_hebra + 1) % consumidores].sem_signal();
	}
}

//----------------------------------------------------------------------

void funcion_hebra_consumidora(){
	for(unsigned i = 0 ; i < num_items; i++){
		hay_productos.sem_wait();
		mutex_fuma.sem_wait();
		int dato = buffer[--libre];
		mutex_fuma.sem_signal();
		if(dato % 5 == 0){
			imprimir_valor.sem_signal();
			mutex_fuma.sem_wait();
			valor_consumido = dato;
			mutex_fuma.sem_signal();
		}
		producto_completo.sem_signal();
		consumir_dato(dato);
	}
}
//----------------------------------------------------------------------
// Imprimir Ejercicio 3
void funcion_imprimir(){
	imprimir_valor.sem_wait();
	mutex_fuma.sem_wait();
	cout << "Valor consumido " << valor_consumido << ", múltiplo de 5" << endl;
	mutex_fuma.sem_signal();
}


int main(){
	cout << "--------------------------------------------------------" << endl;
	cout << "Problema de los productores-consumidores (solución LIFO)." << endl;
	cout << "--------------------------------------------------------" << endl;
	cout << flush;

	vector<thread> hebra_productora;

	int random = aleatorio<0, consumidores - 1>();
	for(int i = 0; i < consumidores; i++){
		hebra_productora.push_back(thread(funcion_hebra_productora, i));
		intercala.push_back(Semaphore(0));
	}
	intercala[random].sem_signal();

	thread hebra_consumidora(funcion_hebra_consumidora);
	//thread hebra_imprimir(funcion_imprimir);
	// Join all
	for (auto& hebra : hebra_productora){
		hebra.join();
	}
	hebra_consumidora.join();
	//hebra_imprimir.join();

	test_contadores();
	return 1;
}
