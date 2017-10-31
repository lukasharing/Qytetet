#include "../include/encyclopedia.h"
void Encyclopedia::load_content(std::string file_path){
  std::ifstream reader;
  reader.open(file_path.c_str());
  if(reader.is_open()){
    std::string events = "";
    while(getline(reader, events)){
      std::istringstream event(events);
      int events_year;
      event >> events_year;
      event.ignore(1); // Bc next char is #, jump it over.
      std::string event_info;

      // Lets look if the element exist if not, add it
      int century = events_year / 100;

      CenturyNode* century_node = getCentury(century);

      if(century_node == NULL){
        std::set<CenturyNode*>::iterator it = centuries.begin();
        century_node = new CenturyNode(century);
        centuries.insert(it, century_node);
      }

      int year = events_year % 100;
      while(getline(event, event_info, '#')){
        century_node->addEvent(event_info, year);
      }
    }
  }else{
    std::cout << "Encyclopedia ERROR: The path file " << file_path << " couldn't be opened.";
  }
  reader.close();
};

CenturyNode* Encyclopedia::getCentury(int _century){
  int cent = _century > 100 ? (_century / 10) : _century;
  CenturyNode* found = NULL;
  if(!centuries.empty() && isInBound(cent)){
    std::set<CenturyNode*>::iterator it;
    for(it = centuries.begin(); it != centuries.end() && found == NULL; it++){
      if((*it)->getCentury() == _century){
        found = *it;
      }
    }
  }
  return found;
};

/* Find Methods */
std::string Encyclopedia::findByYear(int _year){
  CenturyNode* century = getCentury(_year / 100);
  if(century != NULL){
    YearNode* year = century->getYear(_year % 100);
    if(year != NULL){
      return year->toString();
    }
  }
  return "Coudln't find any results";
};

std::string Encyclopedia::findByCentury(int cent){
  CenturyNode* century = getCentury(cent);
  if(century != NULL){
    return century->toString();
  }
  return "Coudln't find any results";
};

std::string Encyclopedia::findByTags(std::string tags){
  std::string result = "";
  for(std::set<CenturyNode*>::iterator c = centuries.begin(); c != centuries.end(); c++){
    result += (*c)->findByTags(tags);
  }
  return result;
};

std::string Encyclopedia::toString(){
  std::string ency_txt = "";
  for(std::set<CenturyNode*>::iterator c = centuries.begin(); c != centuries.end(); c++){
    ency_txt += (*c)->toString() + '\n';
  }
  return ency_txt;
};

std::string Encyclopedia::showTags(){
  std::string tags = "";
  for(std::set<CenturyNode*>::iterator c = centuries.begin(); c != centuries.end(); c++){
    tags += (*c)->getTags() + '\n';
  }
  return tags;
};
