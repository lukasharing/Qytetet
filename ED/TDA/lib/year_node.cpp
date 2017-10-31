#include "../include/year_node.h"
/* Constructors */
YearNode::YearNode(int y){
  year = y;
};

inline int YearNode::getSize() const{ return events.size(); };
inline int YearNode::getYear() const{ return year; };
std::string YearNode::getEvent(int i){ return events.at(i); };

void YearNode::addEvent(std::string event_info){ events.push_back(event_info); };

std::string YearNode::findByTags(std::string tags){
  std::string text = "";
  std::vector<std::string>::iterator it;
  for(it = events.begin(); it != events.end(); it++){
    bool exist = true;
    std::istringstream iss(tags);
    std::string word;
    while(exist && (iss >> word)){
      std::string::size_type pos = (*it).find(word);
      if(pos == std::string::npos){
        exist = false;
      }
    }

    if(exist){
      text += "\t\t*" + (*it) + '\n';
    }
  }
  return text;
};

std::string YearNode::toString(){
  std::string text = "";
  std::vector<std::string>::iterator it;
  for(it = events.begin(); it != events.end(); it++){
    text += "\t\t*" + (*it) + '\n';
  }
  return text;
}
