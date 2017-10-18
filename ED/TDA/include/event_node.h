#ifndef EVENTNODE_H
#define EVENTNODE_H
#include <string>
#include "year_node.h"
class EventNode{
  private:
    std::string info;

  public:
    bool operator==(const EventNode&) const;
    std::string getInfo() const;
    EventNode(std::string);
    EventNode();
};
bool EventNode::operator==(const EventNode& rhs) const{ return this->info == rhs.info;}

std::string EventNode::getInfo() const{ return info; };
EventNode::EventNode(std::string str){
  info = str;
};
EventNode::EventNode(){
  info = "";
};

#endif
