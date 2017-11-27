#include <iostream>
#include <string>
#include <map>

struct Paciente{
  std::string nombre;
  std::string apellido;
  int gravedad;
  std::string to_string(){
    std::string result = nombre + " " + apellido + " with illness importance of " + std::to_string(gravedad);
    return result;
  }
};

void showpannel(){
  std::cout << "---------------------" << std::endl;
  std::cout << "Add patient (a)" << std::endl;
  std::cout << "Remove patient (r)" << std::endl;
  std::cout << "Show patient (s)" << std::endl;
  std::cout << "Show all patients (l)" << std::endl;
  std::cout << "Exit (anything else)" << std::endl;
  std::cout << "Write your option: ";
}

class Urgencias{
private:
  std::map<std::string, Paciente> pacientes;
public:
  void add_paciente(std::string dni, const Paciente& patient){
    std::map<std::string, Paciente>::iterator it = pacientes.find(dni);
    if(it == pacientes.end()){
      pacientes[dni] = patient;
    }else{
      std::cout << "Two person can't have the same DNI" << std::endl;
    }
  };
  Paciente* get_paciente(std::string dni){
    std::map<std::string, Paciente>::iterator it = pacientes.find(dni);
    return (it != pacientes.end()) ? &(it->second) : NULL;
  };
  void remove_paciente(std::string dni){
    std::map<std::string, Paciente>::iterator it = pacientes.find(dni);
    if(it != pacientes.end()){
      pacientes.erase(it);
    }else{
      std::cout << "Coudln't remove inexistent person" << std::endl;
    }
  };

  std::string to_string(){
    std::string result;
    std::map<std::string, Paciente>::iterator it;
    for(it = pacientes.begin(); it != pacientes.end(); ++it){
      result += "DNI " + it->first + " has the following information " + it->second.to_string() + '\n';
    }
    return result;
  }
};


int main(){
  Urgencias urgencias;
  int exit = true;
  do{
      char c;
      showpannel();
      std::cin >> c;
      if(c == 'a'){
        std::string n, l, d;
        int g;
        std::cout << "Introduze the DNI of the pacient to add: ";
        std::cin >> d;
        std::cout << "Introduze the name of the pacient to add: ";
        std::cin >> n;
        std::cout << "Introduze the last name of the pacient to add: ";
        std::cin >> l;
        std::cout << "What is the importance of his/her illness: ";
        std::cin >> g;
        Paciente nuevo;
        nuevo.nombre = n;
        nuevo.apellido = l;
        nuevo.gravedad = g;
        urgencias.add_paciente(d, nuevo);
      }else if(c == 's'){
        std::string d;
        std::cout << "Write the DNI of the pacient to display: ";
        std::cin >> d;
        Paciente* p = urgencias.get_paciente(d);
        if(p == NULL){
          std::cout << "Coudln't find the person with that DNI" << std::endl;
        }else{
          std::cout << p->to_string() << std::endl;
        }
      }else if(c == 'r'){
        std::string d;
        std::cout << "Write the DNI of the pacient to remove: ";
        std::cin >> d;
        urgencias.remove_paciente(d);
      }else if(c == 'l'){
        std::cout << urgencias.to_string() << std::endl;
      }else{
        exit = false;
      }
  }while(exit);
}
