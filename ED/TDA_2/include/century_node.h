#ifndef CENTURY_H
#define CENTURY_H
#include <string>
#include <sstream>
#include <set>
#include <map>
#include <utility>
#include <algorithm>
#include "../include/year_node.h"

typedef std::multimap<std::string, std::set<YearNode, cmp_year>::iterator> tags_container;
typedef std::pair<tags_container::iterator, tags_container::iterator> tagitpair;
typedef std::list<YearNode> year_container;
typedef std::pair<int, std::list<YearNode>::iterator> sorting_pair;
typedef std::set<sorting_pair, cmp_year> sorted_years;

// Custom Functor
struct cmp_year{
  bool operator() const(const sorting_pair& lhs, const sorting_pair& rhs){ return lhs.left < rhs.left; };
};

struct find_year{
  const int& y;
  find_year(int _y):y(_y);
  bool operator()(const sorting_pair&p){ return p.left == _y; };
};

class CenturyNode{
  private:
    int century;
    year_container years;
    sorted_years sorted;
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
    typedef sorted_years::iterator iterator;
    typedef sorted_years::const_iterator const_iterator;
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
    void copy_by_tag(const Encyclopedia&, std::string);

    /**
     * @brief Busca según uno/varios tags
     * @param Una cadena con uno/varios tags
     * @return Devuelve una cadena con la información de TODOS los años en los que
     se utilizó dicho tags en dicha centuria.
    */
    tagitpair findByTag(std::string);

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

void CenturyNode::copy_by_tag(const Encyclopedia& e, std::string t){
  tagitpair tags = tags.equal_range(t);
  tags_container::iterator it;
  for(it = t.first; it != t.second; ++it){
    
  }
};

void CenturyNode::add_event(std::string event_info, int _year){
  /* Find year if not, create it, then add the event */
  sorted_years::iterator it = std::find_if(sorted_years.begin(), sorted_years.end(), find_year(_year));
  year_container::iterator found;
  if(it == sorted_years.end()){
    years.push_back(new YearNode(year));
    found = --years.end();
    sorted_years.insert(sorting_pair(year, last));
  }else{
    found = it->second();
  }

  /* Add tags to tagset */
  std::istringstream event(event_info);
  std::string word;
  while(event >> word){
    // Cleaning non-ascii
    word.erase(std::remove_if(word.begin(), word.end(), [](char c){ return !std::isalpha(c); }), word.end());
    if(word.size() >= 4){
      tags.insert(std::pair<std::string, std::set<YearNode, cmp_year>::iterator>(word, found));
    }
  }
  found.add_event(event_info);
};

tagitpair CenturyNode::findByTag(std::string tag){ return tags.equal_range(tag); };

std::string CenturyNode::to_string(){
  std::string text = "In the " + std::to_string(century) + " th century happened: \n";
  sorted_years::iterator it = years.begin();
  for(it = years.begin(); it != years.end(); ++it){
    text += "\tYear " + std::to_string(it->first) + ": \n" + it->second->to_string();
  }
  return text;
};

#endif
