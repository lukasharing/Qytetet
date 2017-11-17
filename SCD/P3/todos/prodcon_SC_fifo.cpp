#include <iostream>
#include <iomanip>
#include <cassert>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <random>

using namespace std;

constexpr int num_items  = 40;
mutex mtx;
unsigned cont_prod[num_items];
unsigned cont_cons[num_items];

template<int min, int max> int aleatorio(){
  static default_random_engine generador((random_device())());
  static uniform_int_distribution<int> distribucion_uniforme(min, max) ;
  return distribucion_uniforme(generador);
}

int producir_dato(){
  static int contador = 0 ;
  this_thread::sleep_for( chrono::milliseconds(aleatorio<20,100>()));
  mtx.lock();
  cout << "producido: " << contador << endl << flush ;
  mtx.unlock();
  cont_prod[contador]++;
  return contador++;
}

void consumir_dato(unsigned dato){
  if (num_items <= dato){
    cout << " dato === " << dato << ", num_items == " << num_items << endl;
    assert(dato < num_items);
  }
  cont_cons[dato]++;
  this_thread::sleep_for(chrono::milliseconds(aleatorio<20,100>()));
  mtx.lock();
  cout << "\t\tconsumido: " << dato << endl;
  mtx.unlock();
}

void ini_contadores(){
  for(unsigned i = 0; i < num_items; i++){
    cont_prod[i] = 0;
    cont_cons[i] = 0;
  }
}

void test_contadores(){
  bool ok = true ;
  cout << "comprobando contadores ...." << flush ;

  for(unsigned i = 0; i < num_items; i++){
    if (cont_prod[i] != 1){
      cout << "error: valor " << i << " producido " << cont_prod[i] << " veces." << endl ;
      ok = false ;
    }
    if (cont_cons[i] != 1){
      cout << "error: valor " << i << " consumido " << cont_cons[i] << " veces" << endl ;
      ok = false ;
    }
  }
  if (ok){
    cout << endl << flush << "solución (aparentemente) correcta." << endl << flush;
  }
}

// *****************************************************************************
// clase para monitor buffer, version FIFO, semántica SC, un prod. y un cons.

class ProdCons1SC{
  private:
    static const int num_celdas_total = 10;
    int buffer[num_celdas_total];

    int primera_libre;
    int ultima_libre;

    mutex cerrojo_monitor;
    condition_variable ocupadas;
    condition_variable libres;

  public:
    ProdCons1SC();
    int leer();
    void escribir(int valor);
};

ProdCons1SC::ProdCons1SC(){
  primera_libre = 0;
  ultima_libre = 0;
};

int ProdCons1SC::leer(){
  unique_lock<mutex> guarda(cerrojo_monitor);

  if(primera_libre == ultima_libre){
    ocupadas.wait(guarda);
  }

  const int valor = buffer[ultima_libre % num_celdas_total];
  ultima_libre++;
  libres.notify_one();
  return valor;
};

void ProdCons1SC::escribir(int valor){
  unique_lock<mutex> guarda(cerrojo_monitor);

  if ((primera_libre - ultima_libre) >= num_celdas_total){
    libres.wait(guarda);
  }

  buffer[primera_libre % num_celdas_total] = valor;
  primera_libre++;

  ocupadas.notify_one();
}

void funcion_hebra_productora(ProdCons1SC* monitor){
  for(unsigned i = 0; i < num_items; i++){
    int valor = producir_dato() ;
    monitor->escribir(valor);
  }
}
// -----------------------------------------------------------------------------

void funcion_hebra_consumidora(ProdCons1SC * monitor){
  for(unsigned i = 0 ; i < num_items; i++){
    int valor = monitor->leer();
    consumir_dato(valor) ;
  }
}
// -----------------------------------------------------------------------------

int main(){
  cout << "-------------------------------------------------------------------------------" << endl;
  cout << "Problema del productor-consumidor único (1 prod/con, Monitor SC, buffer LIFO). " << endl;
  cout << "-------------------------------------------------------------------------------" << endl;
  cout << flush;

  ini_contadores();
  ProdCons1SC monitor;

  thread hebra_productora(funcion_hebra_productora, &monitor);
  thread hebra_consumidora( funcion_hebra_consumidora, &monitor);

  hebra_productora.join();
  hebra_consumidora.join();

  test_contadores();
}
