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
     * @brief
     * @param
     * @return
    */
    void load_content(std::string);

    /**
     * @brief
     * @param
     * @return
    */
    CenturyNode* getCentury(int);

    /**
     * @brief
     * @param
     * @return
    */
    int findYear(int);

    /**
     * @brief
     * @param
     * @return
    */
    std::string findByYear(int);

    /**
     * @brief
     * @param
     * @return
    */
    std::string findByCentury(int);

    /**
     * @brief
     * @param
     * @return
    */
    std::string findByTags(std::string);

    /**
     * @brief
     * @param
     * @return
    */
    std::string toString();

    /**
     * @brief
     * @param
     * @return
    */
    std::string showTags();
};
#endif
