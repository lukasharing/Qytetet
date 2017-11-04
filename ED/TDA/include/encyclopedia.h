#ifndef ENCYCLOPEDIA_H
#define ENCYCLOPEDIA_H
#include <iostream>
#include <fstream>
#include <sstream>
#include <functional>
#include <set>
#include <string>
#include "../include/century_node.h"


// Custom Functor
struct cmp_cent  {
  bool operator() ( CenturyNode* lhs, CenturyNode* rhs )  {
    return lhs->getCentury() < rhs->getCentury();
  }
};

class Encyclopedia{
  private:
    std::set<CenturyNode*, cmp_cent> centuries;
    inline int getLowerBound() const{ return (*centuries.begin())->getCentury(); };
    inline int getUpperBound() const{ return (*centuries.rbegin())->getCentury(); };
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
    CenturyNode* getCentury(int);

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
#endif
