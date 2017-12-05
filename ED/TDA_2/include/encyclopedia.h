#ifndef ENCYCLOPEDIA_H
#define ENCYCLOPEDIA_H
#include <iostream>
#include <fstream>
#include <sstream>
#include <functional>
#include <set>
#include <string>
#include "../include/year_node.h"
#include "../include/century_node.h"

struct cmp_cent{
  bool operator() (const century_iterator& lhs, const century_iterator& rhs )  {
    return lhs->getCentury() < rhs->getCentury();
  }
};

typedef std::pair<int, CenturyNode::iterator> sorting_pair;
typedef std::set<sorting_pair, cmp_cent> sorted_cent;

struct find_century{
  const int& c;
  find_year(int _c):c(_c);
  bool operator()(const sorting_pair&p){ return p.left == _c; };
};

class Encyclopedia{
  private:
    std::list<CenturyNode> centuries;
    sorted_cent sorted_centuries;
    inline int getLowerBound() const{ return centuries.begin()->getCentury(); };
    inline int getUpperBound() const{ return centuries.rbegin()->getCentury(); };
    inline bool isInBound(int c){ return c >= getLowerBound() && c <= getUpperBound(); };
    void copy(const Encyclopedia& e){
      for(Encyclopedia::iterator it = e.begin(); it != e.end(); ++it){
        add_event(*e);
      }
    };
  public:
    typedef sorted_cent::iterator iterator;
    typedef sorted_cent::const_iterator const_iterator;
    Encyclopedia::iterator begin(){ return centuries.begin(); };
    Encyclopedia::iterator end(){ return centuries.end(); };
    Encyclopedia::const_iterator begin() const{ return centuries.cbegin(); };
    Encyclopedia::const_iterator end() const{ return centuries.cend(); };

    /**
     * @brief Crea un nodo vacío
     * @param El número de la centuria
    */
    void create_node(int c);

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
     * @brief Devuelve todos los tags
     * @param Ninguno
     * @return Una cadena con TODOS los tags usados en total.
    */
    std::string showTags();
};

void create_node(const int& cnt){
  centuries.push_back(CenturyNode(cnt));
  century_node = centuries.rbegin();
  sorted_cent.insert(sorting_pair(cnt, century_node));
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

      if(century_node == end()){
        create_node(century);
      }

      int year = events_year % 100;
      while(getline(event, event_info, '#')){
        century_node->edd_event(event_info, year);
      }
    }
    reader.close();
    return true;
  }
  reader.close();
  return false;
};

Encyclopedia::iterator Encyclopedia::getCentury(int _century){
  return std::find_if(sorted_centuries.begin(), sorted_centuries.end(), find_century(_century))->second;
};

/* Find Methods */
CenturyNode::iterator Encyclopedia::getYear(int _year){
  Encyclopedia result;
  Encyclopedia::iterator century = getCentury(_year / 100);
  if(century != centuries.end()){
    YearNode::iterator year = century->getYear(_year % 100);
    if(year != century->end()){
      return result;
    }
  }
  return century->end();
};

Encyclopedia Encyclopedia::findByTag(std::string tag){
  Encyclopedia result;
  Encyclopedia::iterator it;
  for(it = begin(); it != end(); ++it){
    it->copy_by_tag(result, tag);
  }
  return result;
};

std::string Encyclopedia::to_string(){
  std::string ency_txt = "";
  Encyclopedia::iterator it;
  for(it = centuries.begin(); it != centuries.end(); ++it){
    ency_txt += c->to_string() + '\n';
  }
  return ency_txt;
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
