#ifndef ENCYCLOPEDIA_H
#define ENCYCLOPEDIA_H
#include <iostream>
#include <fstream>
#include <sstream>
#include <functional>
#include <set>
#include <string>
#include "../include/century_node.h"

struct cmp_cent{
  bool operator() (const century_iterator& lhs, const century_iterator& rhs )  {
    return lhs->getCentury() < rhs->getCentury();
  }
};

class Encyclopedia{
  private:
    std::set<CenturyNode, cmp_cent> centuries;
    inline int getLowerBound() const{ return centuries.begin()->getCentury(); };
    inline int getUpperBound() const{ return centuries.rbegin()->getCentury(); };
    inline bool isInBound(int c){ return c >= getLowerBound() && c <= getUpperBound(); };
  public:

    /**
     * @brief Carga el contenido en la enciclopedia, dicho contenido del archivo
     debe contener una estructura fija, AÑO#ACONTECIMIENTO#ACONTECIMIENTO y es otro año,
     éste tiene que tener un salto de línea, creando así los nodos de centurias necesarios y
     acontinuación los nodos de los años (0 - 99);
     * @param Una URL Existente si es posible.
    */
    void load_content(std::string);

    /**
     * @brief Obtención de un nodo de centuria.
     * @param El entero de la centuria
     * @return El nodo de la centuria
    */
    CenturyNode::iterator getCentury(int);

    /**
     * @brief Eventos ocurridos en dicha centuria
     * @param El entero que indica la centuria, positivo si D.C, negativo si A.C.
     * @return Una cadena con la información de cada evento.
    */
    std::string findByCentury(int);

    /**
     * @brief Eventos ocurridos en dicho año
     * @param El entero que indica la fecha completa
     * @return Una cadena con la información de cada evento.
    */
    std::string findByYear(int);

    /**
     * @brief Busca según unos tags característicos en todas las centurias
     * @param Cadena de texto de dichos tags
     * @return Toda la información en la cual se usó dichos tags.
    */
    std::string findByTags(std::string);

    /**
     * @brief Devuelve toda la información de la enciclopedia.
     * @param Ninguno
     * @return Una cadena con toda la información ocurrida.
    */
    std::string toString();

    /**
     * @brief Devuelve todos los tags
     * @param Ninguno
     * @return Una cadena con TODOS los tags usados en total.
    */
    std::string showTags();
};

void Encyclopedia::load_content(std::string file_path){
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

      CenturyNode century_node = getCentury(century);

      if(century_node == NULL){
        std::set<CenturyNode>::iterator it = centuries.begin();
        century_node = new CenturyNode(century);
        centuries.insert(it, century_node);
      }

      int year = events_year % 100;
      while(getline(event, event_info, '#')){
        century_node->edd_event(event_info, year);
      }
    }
  }else{
    std::cout << "Encyclopedia ERROR: The path file " << file_path << " couldn't be opened.";
  }
  reader.close();
};

CenturyNode::iterator Encyclopedia::getCentury(int _century){
  return centuries.find(_century);
};

/* Find Methods */
std::string Encyclopedia::findByYear(int _year){
  CenturyNode::iterator century = getCentury(_year / 100);
  if(century != centuries.end()){
    YearNode* year = century->getYear(_year % 100);
    if(year != NULL){
      return year->toString();
    }
  }
  return "Coudln't find any results";
};

std::string Encyclopedia::findByCentury(int cent){
  CenturyNode century = getCentury(cent);
  if(century != NULL){
    return century->toString();
  }
  return "Coudln't find any results";
};

std::string Encyclopedia::findByTags(std::string tags){
  std::string result = "";
  for(std::set<CenturyNode>::iterator c = centuries.begin(); c != centuries.end(); c++){
    result += c->findByTags(tags);
  }
  return result;
};

std::string Encyclopedia::toString(){
  std::string ency_txt = "";
  for(std::set<CenturyNode*>::iterator c = centuries.begin(); c != centuries.end(); c++){
    ency_txt += c->toString() + '\n';
  }
  return ency_txt;
};

std::string Encyclopedia::showTags(){
  std::string tags = "";
  for(std::set<CenturyNode>::iterator c = centuries.begin(); c != centuries.end(); c++){
    tags += c->getTags() + '\n';
  }
  return tags;
};

Encyclopedia union_encyclopedia(const Encyclopedia& e1, const Encyclopedia& e2){
  Encyclopedia result;
  Encyclopedia::century_iterator cent;
  for(cent = e1.begin(); cent != e1.end(); ++e1){
    result.add_event(*e1);
  }
  return result;
}
#endif
