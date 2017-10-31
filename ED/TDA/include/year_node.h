#ifndef YEARNODE_H
#define YEARNODE_H
#include <string>
#include <vector>
class YearNode{
  private:
    int year = 0;
    std::vector<std::string> events;
  public:
    /**
     * @brief 
     * @param
     * @return
    */
    YearNode(int);

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
    inline int getYear() const;

    /**
     * @brief
     * @param
     * @return
    */
    std::string getEvent(int);

    /**
     * @brief
     * @param
     * @return
    */
    void addEvent(std::string);

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
    bool operator<(const YearNode& other) const{ return year < other.year; }
};
#endif
