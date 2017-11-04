#ifndef CENTURY_H
#define CENTURY_H
#include <string>
#include <sstream>
#include <set>
#include <algorithm>
#include "../include/year_node.h"

// Custom Functor
struct cmp_year  {
  bool operator() ( YearNode* lhs, YearNode* rhs )  {
    return lhs->getYear() < rhs->getYear();
  }
};


struct Tag{
  std::string value;
  std::set<YearNode*, cmp_year> years;
  Tag(std::string _v){ value = _v; };

  bool operator < (const Tag& rhs) const{ return 1 < 2; } // OVERLOADING JUNK
  bool operator==(const Tag& rhs) const{ return value == rhs.value; }
};

class CenturyNode{
  private:
    int century = 0;
    std::set<Tag*> tags;
    std::set<YearNode*, cmp_year> years;

    inline int getLowerBound() const{ return (*years.begin())->getYear(); };
    inline int getUpperBound() const{ return (*years.rbegin())->getYear(); };
    inline bool isInBound(int a){  return a >= getLowerBound() && a <= getUpperBound(); };
  public:
    /**
     * @brief Contructor de una centuria
     * @param Un entero que indica la centuria del nodo.
    */
    CenturyNode(int);

    /**
     * @brief Años en los que han habido sucesos
     * @param Ninguno
     * @return Devuelve el número de años en los que ha ocurrido algo.
    */
    inline int getSize() const;

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
    YearNode* getYear(int);

    /**
     * @brief Añadir Eventos
     * @param Una cadena que nos da información del evento y será dividido en partes
     más importantes llamados "tags" de dónde se realizará la búsqueda y otro argumento,
     un entero, 0 - 99, para indicarle cuándo sucedió.
    */
    void addEvent(std::string, int);

    /**
     * @brief Obtención de tags
     * @param Ninguno
     * @return Devuelve una cadena con todos los tags NO REPETIDOS ocurridos en dicha
     centuria
    */
    std::string getTags() const;

    /**
     * @brief Obtención de un Tag
     * @param La cadena del Tag
     * @return Devuelve un struct tag que contiene el valor (La palabra) y en qué año/s
     se utilizó dicha palabra.
    */
    Tag* getTag(std::string);

    /**
     * @brief Busca según uno/varios tags
     * @param Una cadena con uno/varios tags
     * @return Devuelve una cadena con la información de TODOS los años en los que
     se utilizó dicho tags en dicha centuria.
    */
    std::string findByTags(std::string);

    /**
     * @brief Convierte el nodo en texto plano.
     * @param Ninguno
     * @return Una cadena que devuelve TODOS los acontecimientos en dichas centurias
    */
    std::string toString();
};
#endif
