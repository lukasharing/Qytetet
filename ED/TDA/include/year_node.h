#ifndef YEARNODE_H
#define YEARNODE_H
#include <string>
#include <set>
#include <iostream>
#include <sstream>
#include "event_node.h"
#include <algorithm>
class YearNode{
  private:
    int year;
    std::set<std::string> tags;
    std::vector<EventNode*> nodes;
  public:
    int getSize() const;
    int getYear() const;
    EventNode* getEvent(int);
    void addEvent(std::string);
    std::string getTags() const;

    EventNode* findByTag(std::string);
    YearNode(int);
};
int YearNode::getSize() const{ return nodes.size(); };
int YearNode::getYear() const{ return year; };
EventNode* YearNode::getEvent(int _i){ return nodes[_i]; };

void YearNode::addEvent(std::string event_info){
  std::istringstream event(event_info);
  std::string word;
  while(event >> word){
    if(word.size() >= 4){
      tags.insert(word);
    }
  }
  nodes.push_back(new EventNode(event_info));
};

EventNode* YearNode::findByTag(std::string tag){
  if(tags.find(tag) != tags.end()){
    // Found, lets look for the pointer
    EventNode* found = NULL;
    for(int i = 0; i < nodes.size() && found == NULL; i++){
      EventNode* element = nodes[i];
      if(element->getInfo().find(tag) != element->getInfo().size()){
        found = element;
      }
    }
    return found;
  }
  return NULL;
};

YearNode::YearNode(int _y){
  year = _y;
};

std::string YearNode::getTags() const{
  std::string all_tags = "";
  for(std::set<std::string>::iterator i = tags.begin(); i != tags.end(); i++){
    all_tags += *i + "\t";
  }
  return all_tags;
};
#endif
