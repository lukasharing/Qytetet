#include <iostream>
#include <iomanip>
#include <cassert>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <random>

using namespace std;

constexpr int num_prods = 5;
constexpr int num_cons = 2;

constexpr int num_items  = 40;
mutex mtx;
unsigned cont_prod[num_items];
unsigned cont_cons[num_items];

template<int min, int max> int aleatorio(){
  static default_random_engine generador((random_device())());
  static uniform_int_distribution<int> distribucion_uniforme(min, max);
  return distribucion_uniforme(generador);
}

int producir_dato(int k){
  static int n_producidos = 0;
  this_thread::sleep_for(chrono::milliseconds(aleatorio<20,100>()));
  mtx.lock();
  cout << "Productor " << k << " -> producido: " << n_producidos << endl << flush;
  mtx.unlock();
  ++cont_prod[n_producidos];
  return n_producidos++;
}

void consumir_dato(unsigned dato, int k){
  this_thread::sleep_for(chrono::milliseconds(aleatorio<20,100>()));
  mtx.lock();
  cout << "\t\t Consumidor " << k << " -> consumido: " << dato << endl;
  mtx.unlock();
  ++cont_cons[dato];
}

void ini_contadores(){
  for(unsigned i = 0; i < num_items; i++){
    cont_prod[i] = 0;
    cont_cons[i] = 0;
  }
}

void test_contadores(){
  bool ok = true;
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


const int buffer_size = 10;
class ProdsConsSC{
  private:
    int buffer[buffer_size];

    int primera_libre;

    mutex cerrojo_monitor;
    condition_variable ocupadas;
    condition_variable libres;

    int total_consumidos;
    int total_producidos;
  public:
    ProdsConsSC();
    int leer();
    void escribir(int valor);
    bool deboProducir();
    bool deboConsumir();
};
ProdsConsSC::ProdsConsSC(){
  primera_libre = 0;
  total_producidos = 0;
  total_consumidos = 0;
};


bool ProdsConsSC::deboProducir(){ return (++total_producidos <= num_items); };
bool ProdsConsSC::deboConsumir(){ return (++total_consumidos <= num_items); };

int ProdsConsSC::leer(){
  unique_lock<mutex> guarda(cerrojo_monitor);
  if(primera_libre <= 0){
    ocupadas.wait(guarda);
  }

  const int valor = buffer[--primera_libre];
  libres.notify_one();
  return valor;
};

void ProdsConsSC::escribir(int valor){
  unique_lock<mutex> guarda(cerrojo_monitor);
  if (primera_libre == buffer_size){
    libres.wait(guarda);
  }

  buffer[primera_libre++] = valor;
  ocupadas.notify_one();
};

void funcion_hebra_productora(ProdsConsSC* monitor, int number){
  while(monitor->deboProducir()){
    int dato = producir_dato(number);
    monitor->escribir(dato);
  }
}

void funcion_hebra_consumidora(ProdsConsSC* monitor, int number){
  while(monitor->deboConsumir()){
    int valor = monitor->leer();
    consumir_dato(valor, number);
  }
}
// -----------------------------------------------------------------------------

int main(){
  cout << "-------------------------------------------------------------------------------" << endl;
  cout << "Problema de los productores-consumidores múltiple (prods/cons, Monitor SC, buffer LIFO). " << endl;
  cout << "-------------------------------------------------------------------------------" << endl;
  cout << flush;

  ini_contadores();
  ProdsConsSC monitor;

  // Initialize all prods
  thread hebra_productora[num_prods];
  for(int i = 0; i < num_prods; i++){
    hebra_productora[i] = thread(funcion_hebra_productora, &monitor, i);
  }

  // Initialize all cons
  thread hebra_consumidora[num_cons];
  for(int i = 0; i < num_cons; i++){
    hebra_consumidora[i] = thread(funcion_hebra_consumidora, &monitor, i);
  }

  for(int i = 0; i < num_cons; i++){
    hebra_productora[i].join();
  }
  for(int i = 0; i < num_cons; i++){
    hebra_consumidora[i].join();
  }
  test_contadores();
}
