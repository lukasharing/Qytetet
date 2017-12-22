#ifndef YEARNODE_H
#define YEARNODE_H
#include <string>
#include <vector>
class YearNode{
  private:
    int year = 0;
    std::vector<std::string> events;
    void copy(const YearNode& c){
      year = c.year;
      events = c.events;
    };
  public:
    /** Iterators **/
    typedef std::vector<std::string>::iterator iterator;
    typedef std::vector<std::string>::const_iterator const_iterator;
    YearNode::iterator begin(){ return events.begin(); };
    YearNode::iterator end(){ return events.end(); };
    YearNode::const_iterator begin() const{ return events.cbegin(); };
    YearNode::const_iterator end() const{ return events.cend(); };

    /**
     * @brief Copy Constructor
     * @param YearNode a copyr
    */
    YearNode(const YearNode& y);

    /**
     * @brief Constructor
     * @param un entero para indicar el año.
    */
    YearNode(int);

    /**
     * @brief Devuelve la cantidad de eventos en dicho año.
     * @param ninguno
     * @return Un entero con la cantidad de eventos.
    */
    inline int size() const;

    /**
     * @brief Devuelve la cantidad de eventos en dicho año.
     * @param ninguno
     * @return Un entero con la cantidad de eventos.
    */
    inline bool empty() const;

    /**
     * @brief Devuelve el año del evento/eventos
     * @param ninguno
     * @return Un entero como año.
    */
    inline int getYear() const;

    /**
     * @brief Método de obtención de eventos
     * @param Número del evento
     * @return Devuelve una cadena con la información del evento.
    */
    std::string getEvent(int);

    /**
     * @brief Añadir eventos
     * @param Una cadena que cuenta un poco acerca del evento
    */
    void add_event(const std::string&);

    /**
     * @brief Busca un evento que contenga una palabra/varias.
     * @param Una cadena con diferentes palabras separadas entre espacios
     * @return devuelve la información del evento que contiene esas palabras
    */
    YearNode findByTags(std::string);

    /**
     * @brief Convierte un/unos eventos en texto plano
     * @param ninguno
     * @return devuelve una cadena con todos los eventos en dicho año
    */
    std::string to_string();

    /**
     * @brief Comparación de 2 eventos
     * @param Otro evento a comparar
     * @return Devuelve true o false según si un evento ocurrió antes o no.
    */
    bool operator < (const YearNode& other) const{ return year < other.year; }
    bool operator == (const YearNode& other) const{ return year == other.year; }
    bool operator = (const YearNode& other){
      if(&other != this){
        copy(other);
      }
      return this;
    };
};

/* Constructors */
YearNode::YearNode(const YearNode& y){
  copy(y);
};

YearNode::YearNode(int y){
  year = y;
};

inline int YearNode::size() const{ return events.size(); };
inline bool YearNode::empty() const{ return (events.size() == 0); };
inline int YearNode::getYear() const{ return year; };
std::string YearNode::getEvent(int i){ return events.at(i); };

void YearNode::add_event(const std::string& event_info){
  events.push_back(event_info);
};

YearNode YearNode::findByTags(std::string tags){
  YearNode result(year);
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
      result.add_event(*it);
    }
  }
  return result;
};

std::string YearNode::to_string(){
  std::string text = "";
  std::vector<std::string>::iterator it;
  for(it = events.begin(); it != events.end(); it++){
    text += "\t\t*" + (*it) + '\n';
  }
  return text;
}
#endif
