#include <iostream>
#include <string>
#include <utility>
#include <vector>
#include <map>

struct Piloto{
  std::string nombre;
  std::string escuderia;
  int posicion;
  Piloto(std::string _nombre, std::string _escuderia, int _posicion){
    nombre = _nombre;
    escuderia = _escuderia;
    posicion = _posicion;
  };
  std::string to_string(){
    std::string result = nombre + " con la escuderia " + escuderia + " gano " + std::to_string(posicion);
    return result;
  }
};

class Carrera{
private:
  std::multimap<std::string, Piloto> pilots;
public:
  void add_pilot(std::string s, const Piloto& pilot){
    pilots.insert(std::pair<std::string, Piloto>(s, pilot));
  };

  std::vector<Piloto> getByLastName(std::string a){
    std::pair <std::multimap<std::string, Piloto>::iterator, std::multimap<std::string, Piloto>::iterator> ret;
    ret = pilots.equal_range(a);

    std::multimap<std::string, Piloto>::iterator ti;
    std::vector<Piloto> result;
    for (ti = ret.first; ti != ret.second; ++ti){
      result.push_back(ti->second);
    }
    return result;
  };

  std::vector<Piloto> getByName(std::string a){
    std::vector<Piloto> result;
    std::multimap<std::string, Piloto>::iterator it;
    for(it = pilots.begin(); it != pilots.end(); ++it){
      if(it->second.nombre == a){
        result.push_back(it->second);
      }
    }
    return result;
  };

  std::vector<Piloto> getByEscuderia(std::string a){
    std::vector<Piloto> result;
    std::multimap<std::string, Piloto>::iterator it;
    for(it = pilots.begin(); it != pilots.end(); ++it){
      if(it->second.escuderia == a){
        result.push_back(it->second);
      }
    }
    return result;
  };

  Piloto getByPosicion(int a){
    Piloto* p = NULL;
    std::multimap<std::string, Piloto>::iterator it;
    for(it = pilots.begin(); it != pilots.end() && p == NULL; ++it){
      if(it->second.posicion == a){
        p = &it->second;
      }
    }
    return *p;
  };
};


int main(){
  Carrera f1;
  std::string last, name, escd;
  last = "Schumacher";
  name = "Michael";
  escd = "Ferrari";
  f1.add_pilot(last, Piloto(name, escd, 91));
  last = "Hamilton";
  name = "Lewis";
  escd = "McLaren";
  f1.add_pilot(last, Piloto(name, escd, 62));
  last = "Prost";
  name = "Alain";
  escd = "Prost GP";
  f1.add_pilot(last, Piloto(name, escd, 51));
  last = "Vettel";
  name = "Sebastian";
  escd = "BMW";
  f1.add_pilot(last, Piloto(name, escd, 47));
  last = "Senna";
  name = "Ayrton";
  escd = "Woking";
  f1.add_pilot(last, Piloto(name, escd, 41));

  std::vector<Piloto> escuderias = f1.getByEscuderia("BMW");
  std::vector<Piloto>::iterator it = escuderias.begin();
  for(it = escuderias.begin(); it != escuderias.end(); ++it){
    std::cout << it->to_string() << std::endl;
  }

  std::vector<Piloto> nombres = f1.getByName("Alain");
  it = nombres.begin();
  for(it = nombres.begin(); it != nombres.end(); ++it){
    std::cout << it->to_string() << std::endl;
  }

  Piloto piloto = f1.getByPosicion(91);
  std::cout << piloto.to_string() << std::endl;
}
