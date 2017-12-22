#ifndef ENCYCLOPEDIA_H
#define ENCYCLOPEDIA_H
#include <iostream>
#include <fstream>
#include <sstream>
#include <cassert>
#include <list>
#include <utility>
#include <functional>
#include <set>
#include <string>
#include "../include/century_node.h"

typedef std::list<CenturyNode> cent_list;
typedef std::pair<int, cent_list::iterator> sorting_cent;
struct cmp_cent{
  bool operator()(const sorting_cent& lhs, const sorting_cent& rhs){
    return lhs.first < rhs.first;
  }
};

typedef std::set<sorting_cent, cmp_cent> sorted_cent;

struct find_century{
  int c;
  find_century(int _c){ c = _c; };
  bool operator()(const sorting_cent&p){ return p.first == c; };
};

class Encyclopedia{
  private:
    cent_list centuries;
    sorted_cent sorted_centuries;
    inline int getLowerBound() const{ return sorted_centuries.cbegin()->first; };
    inline int getUpperBound() const{ return sorted_centuries.crbegin()->first; };
    inline bool isInBound(int c){ return c >= getLowerBound() && c <= getUpperBound(); };
    void copy(const Encyclopedia& e){
      sorted_cent::iterator it;
      for(it = e.begin(); it != e.end(); ++it){
        centuries.push_back(*(it->second));
        cent_list::iterator last = --centuries.end();
        sorted_centuries.insert(sorting_cent(it->first, last));
      }
    };
  public:
    Encyclopedia();
    Encyclopedia(const Encyclopedia&);
    typedef sorted_cent::iterator iterator;
    typedef sorted_cent::const_iterator const_iterator;
    sorted_cent::iterator begin(){ return sorted_centuries.begin(); };
    sorted_cent::iterator end(){ return sorted_centuries.end(); };
    sorted_cent::const_iterator begin() const{ return sorted_centuries.cbegin(); };
    sorted_cent::const_iterator end() const{ return sorted_centuries.cend(); };

    /**
     * @brief Crea un nodo vacío
     * @param El número de la centuria
    */
    cent_list::iterator create_node(int c);

    /**
     * @brief Filtra por intervalo
     * @param Primero-> Cota inferior, Segundo-> Cota superior
    */
    Encyclopedia interval(int, int);

    /**
     * @brief Carga el contenido en la enciclopedia, dicho contenido del archivo
     debe contener una estructura fija, AÑO#ACONTECIMIENTO#ACONTECIMIENTO y es otro año,
     éste tiene que tener un salto de línea, creando así los nodos de centurias necesarios y
     acontinuación los nodos de los años (0 - 99);
     * @param Una URL Existente si es posible.
     * @return boolean true or false if could or not be opened
    */
    bool load_content(std::string);

    /**
     * @brief Obtención de un nodo de centuria.
     * @param El entero de la centuria
     * @return El nodo de la centuria
    */
    iterator getCentury(int);

    /**
     * @brief Eventos ocurridos en dicho año
     * @param El entero que indica la fecha completa
     * @return Una cadena con la información de cada evento.
    */
    CenturyNode::iterator getYear(int);

    /**
     * @brief Busca según un tag
     * @param Cadena de texto de dichos tags
     * @return Toda la información en la cual se usó dichos tags.
    */
    Encyclopedia findByTag(std::string);

    /**
     * @brief Devuelve toda la información de la enciclopedia.
     * @param Ninguno
     * @return Una cadena con toda la información ocurrida.
    */
    std::string to_string();

    /**
     * @brief Devuelve el número de eventos de cada año
    */
    std::string statistics();

    void operator = (const Encyclopedia& p){ if(&p != this) copy(p); };
};
Encyclopedia::Encyclopedia(){};
Encyclopedia::Encyclopedia(const Encyclopedia& p){ copy(p); };
cent_list::iterator Encyclopedia::create_node(int cnt){
  centuries.push_back(CenturyNode(cnt));
  cent_list::iterator century_node = --centuries.end();
  sorted_centuries.insert(sorting_cent(cnt, century_node));
  return century_node;
};

bool Encyclopedia::load_content(std::string file_path){
  std::ifstream reader;
  reader.open(file_path.c_str());
  if(reader.is_open()){
    std::string events = "";
    while(getline(reader, events)){
      std::istringstream event(events);
      int events_year;
      event >> events_year;
      event.ignore(1); // Bc next char is #, jump it over.
      std::string event_info;

      // Lets look if the element exist if not, add it
      int century = events_year / 100;
      iterator century_node = getCentury(century);
      cent_list::iterator centi = centuries.end();
      if(century_node != end()){
        centi = century_node->second;
      }else{
        centi = create_node(century);
      }

      int year = events_year % 100;
      while(getline(event, event_info, '#')){
        centi->add_event(event_info, year);
      }
    }
    reader.close();
    return true;
  }
  reader.close();
  return false;
};
Encyclopedia::iterator Encyclopedia::getCentury(int _century){
  return std::find_if(begin(), end(), find_century(_century));
};

/* Find Methods */
CenturyNode::iterator Encyclopedia::getYear(int _year){
  Encyclopedia::iterator century = getCentury(_year / 100);
  assert(century != end());
  CenturyNode::iterator year = century->second->getYear(_year % 100);
  assert(year != century->second->end());
  return year;
};

Encyclopedia Encyclopedia::findByTag(std::string tag){
  Encyclopedia result;
  Encyclopedia::iterator it;
  for(it = begin(); it != end(); ++it){
    //it->copy_by_tag(result, tag);
  }
  return result;
};

std::string Encyclopedia::statistics(){
  std::string result;
  Encyclopedia::iterator it;
  for(it = begin(); it != end(); ++it){
    CenturyNode::iterator ti;
    for(ti = it->second->begin(); ti != it->second->end(); ++ti){
      result += "In the " + std::to_string(it->first * 100 + ti->first) + " -> " + std::to_string(ti->second->size()) + " total events \n";
    }
  }
  return result;
};

std::string Encyclopedia::to_string(){
  std::string ency_txt = "";
  Encyclopedia::iterator it;
  for(it = sorted_centuries.begin(); it != sorted_centuries.end(); ++it){
    ency_txt += it->second->to_string() + '\n';
  }
  return ency_txt;
};

Encyclopedia Encyclopedia::interval(int a, int b){
  Encyclopedia result();
  int c = a / 100;
  cent_list::iterator ti = result.create_node(c);
  for(int i = a; i <= b; ++i){
    if(i / 100 != c){
      c = i / 100;
      ti = result.create_node(c);
    }
    ti->century_union(getYear(i)->second);
  }
  return result;
};

Encyclopedia encyclopedia_union(const Encyclopedia& e1, const Encyclopedia& e2){
  Encyclopedia result(e1);
  Encyclopedia::iterator it;
  for(it = e2.begin(); it != e2.end(); ++it){
    Encyclopedia::iterator century_node = result.getCentury(it->first);
    cent_list::iterator centi = century_node.end();
    if(century_node != result.end()){
      centi = century_node->second;
    }else{
      centi = result.create_node(it->first);
    }
    centi->century_union(*(it->second));
  }
  return result;
};
#endif
