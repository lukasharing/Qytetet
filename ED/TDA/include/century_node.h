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
     * @brief
     * @param
     * @return
    */
    CenturyNode(int);

    /**
     * @brief
     * @param
     * @return
    */
    inline int getSize() const;

    /**
     * @brief
     * @param
     * @return
    */
    inline int getCentury() const;

    /**
     * @brief
     * @param
     * @return
    */
    YearNode* getYear(int);

    /**
     * @brief
     * @param
     * @return
    */
    void addEvent(std::string, int);

    /**
     * @brief
     * @param
     * @return
    */
    std::string getTags() const;

    /**
     * @brief
     * @param
     * @return
    */
    Tag* getTag(std::string);

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
};
#endif
