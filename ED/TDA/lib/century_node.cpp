#include "../include/century_node.h"

CenturyNode::CenturyNode(int c){
  century = c;
};

inline int CenturyNode::getSize() const{ return years.size(); };
inline int CenturyNode::getCentury() const{ return century; };
YearNode* CenturyNode::getYear(int _year){
  YearNode* found = NULL;
  if(!years.empty() && isInBound(_year)){
    std::set<YearNode*>::iterator it;
    for(it = years.begin(); it != years.end() && found == NULL; it++){
      if((*it)->getYear() == _year){
        found = *it;
      }
    }
  }
  return found;
};

Tag* CenturyNode::getTag(std::string word){
  Tag* found = NULL;
  std::set<Tag*>::iterator iter;
  for(iter = tags.begin(); iter != tags.end() && found == NULL; iter++){
    if((*iter)->value == word){
      found = *iter;
    }
  }
  return found;
};

void CenturyNode::addEvent(std::string event_info, int _year){
  /* Find year if not, create it, then add the event */
  YearNode* year = getYear(_year);
  if(year == NULL){
    year = new YearNode(_year);
    years.insert(year);
  }

  /* Add tags to tagset */
  std::istringstream event(event_info);
  std::string word;
  while(event >> word){
    // Cleaning non-ascii
    word.erase(std::remove_if(word.begin(), word.end(), [](char c) { return !std::isalpha(c); } ), word.end());
    if(word.size() >= 4){
      Tag* found = getTag(word);
      if(found == NULL){
        tags.insert(found = new Tag(word));
      }
      found->years.insert(year);
    }
  }
  year->addEvent(event_info);
};

std::string CenturyNode::findByTags(std::string all_tags){
  std::string tagy = "";

  std::istringstream iss(all_tags);
  std::string word;

  bool exist = true;

  std::set<YearNode*, cmp_year> found_years;
  while(exist && (iss >> word)){
    Tag* found = getTag(word);

    if(found == NULL){
      exist = false;
    }else{
      // Union of two sets
      found_years.insert(found->years.begin(), found->years.end());
    }
  }

  if(!found_years.empty()){
    for(std::set<YearNode*>::iterator i = found_years.begin(); i != found_years.end(); i++){
      int year = (*i)->getYear();
      std::string found = (*i)->findByTags(all_tags);
      if(found != ""){
        tagy += "Found in the year " + std::to_string(century) + (year < 10 ? "0" : "") + std::to_string(year) + "\n";
        tagy += found;
      }
    }
  }
  return tagy;
};

std::string CenturyNode::getTags() const{
  std::string all_tags = "";
  for(std::set<Tag*>::iterator i = tags.begin(); i != tags.end(); i++){
    all_tags += (*i)->value + " = " + std::to_string((*i)->years.size()) + "\n";
  }
  return all_tags;
};

std::string CenturyNode::toString(){
  std::string text = "In the " + std::to_string(century) + " th century happened: \n";
  for(std::set<YearNode*>::iterator i = years.begin(); i != years.end(); i++){
    text += "\tYear " + std::to_string((*i)->getYear()) + ": \n";
    text += (*i)->toString();
  }
  return text;
};
