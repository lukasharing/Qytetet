#ifndef CENTURY_H
#define CENTURY_H
#include <string>
#include <sstream>
#include <set>
#include <map>
#include <utility>
#include <algorithm>
#include "../include/year_node.h"

// Custom Functor
struct cmp_year  {
  bool operator() (const YearNode& lhs, const YearNode& rhs )  {
    return lhs.getYear() < rhs.getYear();
  }
};

typedef std::multimap<std::string, std::set<YearNode, cmp_year>::iterator> tags_container;

class CenturyNode{
  private:
    int century;
    std::set<YearNode, cmp_year> years;
    tags_container tags;

    inline int getLowerBound() const{ return years.cbegin()->getYear(); };
    inline int getUpperBound() const{ return years.crbegin()->getYear(); };
    inline bool isInBound(int a) const{  return (a >= getLowerBound() && a < getUpperBound()); };
    void copy(const CenturyNode& c){
      century = c.century;
      tags = y.tags;
      years = y.years;
    };
  public:
    /** Iterators **/
    typedef std::set<YearNode, cmp_year>::iterator iterator;
    typedef std::set<YearNode, cmp_year>::const_iterator const_iterator;
    CenturyNode::iterator begin(){ return years.begin(); };
    CenturyNode::iterator end(){ return years.end(); };
    CenturyNode::const_iterator begin() const{ return years.cbegin(); };
    CenturyNode::const_iterator end() const{ return years.cend(); };


    /**
     * @brief Contructor de copy
     * @param add_event constructor
    */
    CenturyNode(const CenturyNode&);

    /**
     * @brief Contructor de una centuria
     * @param Un entero que indica la centuria del nodo.
    */
    CenturyNode(int);

    /**
     * @brief Años en los que han habido sucesos
     * @return Devuelve el número de años en los que ha ocurrido algo.
    */
    inline int size() const;

    /**
     * @brief Está vacío
     * @return Devuelve true o false según si está vacío o no
    */
    inline bool empty() const;

    /**
     * @brief Centuria del nodo (Negativo si queremos que sea A.C)
     * @param Ninguno
     * @return Devuelve la centuria del nodo
    */
    inline int getCentury() const;

    /**
     * @brief Obtención de años en los que ocurrieron sucesos
     * @param Entero de 0 - 99
     * @return Devuelve el puntero nodo de ese año, si no existe un NULL.
    */
    YearNode::iterator getYear(int);

    /**
     * @brief Añadir Eventos
     * @param Una cadena que nos da información del evento y será dividido en partes
     más importantes llamados "tags" de dónde se realizará la búsqueda y otro argumento,
     un entero, 0 - 99, para indicarle cuándo sucedió.
    */
    void add_event(std::string, int);

    /**
     * @brief Obtención de un Tag
     * @param La cadena del Tag
     * @return Devuelve un struct tag que contiene el valor (La palabra) y en qué año/s
     se utilizó dicha palabra.
    */
    std::pair<tags_container::iterator, tags_container::iterator> getTag(std::string);

    /**
     * @brief Busca según uno/varios tags
     * @param Una cadena con uno/varios tags
     * @return Devuelve una cadena con la información de TODOS los años en los que
     se utilizó dicho tags en dicha centuria.
    */
    CenturyNode findByTags(std::string);

    /**
     * @brief Convierte el nodo en texto plano.
     * @param Ninguno
     * @return Una cadena que devuelve TODOS los acontecimientos en dichas centurias
    */
    std::string to_string();

    /** Overloading **/
    bool operator < (const CenturyNode& other) const{ return year < other.year; }
    bool operator == (const CenturyNode& other) const{ return year == other.year; }
    bool operator = (const CenturyNode& other){
      if(&other != this){
        copy(other);
      }
      return this;
    };
};

CenturyNode::CenturyNode(const CenturyNode& c){
  copy(c);
};

CenturyNode::CenturyNode(int c){
  century = c;
};

inline int CenturyNode::size() const{ return years.size(); };
inline bool CenturyNode::empy() const{ return (years.size() == 0); };
inline int CenturyNode::getCentury() const{ return century; };
YearNode::iterator CenturyNode::getYear(int _year){
  if(!years.empty() && isInBound(_year)){
    iterator ti = years.find(_year);
    if(ti != end()){
      return ti;
    }
  }
  return end();
};

std::pair<tags_container::iterator, tags_container::iterator> CenturyNode::getTag(std::string tag){ return tags.equal_range(tag); };

void CenturyNode::add_event(std::string event_info, int _year){
  /* Find year if not, create it, then add the event */
  YearNode year = getYear(_year);
  if(year.empty()){
    years.insert(year);
  }

  /* Add tags to tagset */
  std::istringstream event(event_info);
  std::string word;
  while(event >> word){
    // Cleaning non-ascii
    word.erase(std::remove_if(word.begin(), word.end(), [](char c) { return !std::isalpha(c); } ), word.end());
    if(word.size() >= 4){
      Tag* found = getTag(word);
      if(found == NULL){
        tags.insert(found = new Tag(word));
      }
      found->years.insert(year);
    }
  }
  year->add_event(event_info);
};

std::string CenturyNode::findByTags(std::string all_tags){
  std::string tagy = "";

  std::istringstream iss(all_tags);
  std::string word;

  bool exist = true;

  std::set<YearNode*, cmp_year> found_years;
  while(exist && (iss >> word)){
    Tag* found = getTag(word);

    if(found == NULL){
      exist = false;
    }else{
      // Union of two sets
      found_years.insert(found->years.begin(), found->years.end());
    }
  }

  if(!found_years.empty()){
    for(std::set<YearNode*>::iterator i = found_years.begin(); i != found_years.end(); i++){
      int year = (*i)->getYear();
      std::string found = (*i)->findByTags(all_tags);
      if(found != ""){
        tagy += "Found in the year " + std::to_string(century) + (year < 10 ? "0" : "") + std::to_string(year) + "\n";
        tagy += found;
      }
    }
  }
  return tagy;
};

std::string CenturyNode::getTags() const{
  std::string all_tags = "";
  for(std::set<Tag*>::iterator i = tags.begin(); i != tags.end(); i++){
    all_tags += (*i)->value + " = " + std::to_string((*i)->years.size()) + "\n";
  }
  return all_tags;
};

std::string CenturyNode::toString(){
  std::string text = "In the " + std::to_string(century) + " th century happened: \n";
  for(std::set<YearNode*>::iterator i = years.begin(); i != years.end(); i++){
    text += "\tYear " + std::to_string((*i)->getYear()) + ": \n";
    text += (*i)->toString();
  }
  return text;
};

#endif
