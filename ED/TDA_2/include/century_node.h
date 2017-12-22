#ifndef CENTURY_H
#define CENTURY_H
#include <string>
#include <sstream>
#include <set>
#include <list>
#include <map>
#include <utility>
#include <algorithm>
#include "../include/encyclopedia.h"
#include "../include/year_node.h"


// Custom Functor
typedef std::list<YearNode> year_container;
typedef std::pair<int, year_container::iterator> sorting_pair;
struct cmp_year{
  bool operator()(const sorting_pair& lhs, const sorting_pair& rhs){
    return lhs.first < rhs.first;
  };
};
typedef std::set<sorting_pair, cmp_year> sorted_years;
// TAGS
typedef std::multimap<std::string, year_container::iterator> tags_container;
typedef std::pair<tags_container::iterator, tags_container::iterator> tagitpair;

struct find_year{
  int y;
  find_year(int _y){ y = _y; };
  bool operator()(const sorting_pair&p){ return p.first == y; };
};

class CenturyNode{
  private:
    int century;
    year_container years;
    sorted_years sorted;
    tags_container tags;

    inline int getLowerBound() const{ return sorted.cbegin()->first; };
    inline int getUpperBound() const{ return sorted.crbegin()->first; };
    inline bool isInBound(int a) const{  return (a >= getLowerBound() && a < getUpperBound()); };
    void copy(const CenturyNode& c){
      century = c.century;
      tags = c.tags;
      sorted = c.sorted;
      years = c.years;
    };
  public:
    /** Iterators **/
    typedef sorted_years::iterator iterator;
    typedef sorted_years::const_iterator const_iterator;
    CenturyNode::iterator begin(){ return sorted.begin(); };
    CenturyNode::iterator end(){ return sorted.end(); };
    CenturyNode::const_iterator begin() const{ return sorted.cbegin(); };
    CenturyNode::const_iterator end() const{ return sorted.cend(); };


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
    iterator getYear(int);

    /**
     * @brief Añadir Eventos
     * @param Una cadena que nos da información del evento y será dividido en partes
     más importantes llamados "tags" de dónde se realizará la búsqueda y otro argumento,
     un entero, 0 - 99, para indicarle cuándo sucedió.
    */
    void add_event(std::string, int);

    /**
     * @brief Union dos centurias
     * @param Centuria a unir
    */
    void century_union(const CenturyNode&);

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
    bool operator < (const CenturyNode& other) const{ return century < other.century; }
    bool operator == (const CenturyNode& other) const{ return century == other.century; }
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
inline bool CenturyNode::empty() const{ return (years.size() == 0); };
inline int CenturyNode::getCentury() const{ return century; };
CenturyNode::iterator CenturyNode::getYear(int _year){
  if(!years.empty() && isInBound(_year)){
    return std::find_if(begin(), end(), find_year(_year));
  }
  return end();
};

void CenturyNode::century_union(const CenturyNode& c){
  CenturyNode::iterator it;
  for(it = c.begin(); it != c.end(); ++it){
    YearNode::iterator ti;
    for(ti = it->second->begin(); ti != it->second->end(); ++ti){
      add_event(*ti, it->first);
    }
  }
};

void CenturyNode::add_event(std::string event_info, int _year){
  /* Find year if not, create it, then add the event */
  sorted_years::iterator it = std::find_if(begin(), end(), find_year(_year));
  year_container::iterator found;
  if(it == end()){
    years.push_back(YearNode(_year));
    found = --years.end();
    sorted.insert(sorting_pair(_year, found));
  }else{
    found = it->second;
  }

  /* Add tags to tagset */
  std::istringstream event(event_info);
  std::string word;
  while(event >> word){
    // Cleaning non-ascii
    word.erase(std::remove_if(word.begin(), word.end(), [](char c){ return !std::isalpha(c); }), word.end());
    if(word.size() >= 4){
      tags.insert(std::pair<std::string, year_container::iterator>(word, found));
    }
  }
  found->add_event(event_info);
};

tagitpair CenturyNode::findByTag(std::string tag){ return tags.equal_range(tag); };

std::string CenturyNode::to_string(){
  std::string text = "In the " + std::to_string(century) + " th century happened: \n";
  sorted_years::iterator it;
  for(it = begin(); it != end(); ++it){
    text += "\tYear " + std::to_string(it->first) + ": \n" + it->second->to_string();
  }
  return text;
};

#endif
