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
     * @brief Constructor
     * @param un entero para indicar el año.
    */
    YearNode(int);

    /**
     * @brief Devuelve la cantidad de eventos en dicho año.
     * @param ninguno
     * @return Un entero con la cantidad de eventos.
    */
    inline int getSize() const;

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
    void addEvent(std::string);

    /**
     * @brief Busca un evento que contenga una palabra/varias.
     * @param Una cadena con diferentes palabras separadas entre espacios
     * @return devuelve la información del evento que contiene esas palabras
    */
    std::string findByTags(std::string);

    /**
     * @brief Convierte un/unos eventos en texto plano
     * @param ninguno
     * @return devuelve una cadena con todos los eventos en dicho año
    */
    std::string toString();

    /**
     * @brief Comparación de 2 eventos
     * @param Otro evento a comparar
     * @return Devuelve true o false según si un evento ocurrió antes o no.
    */
    bool operator<(const YearNode& other) const{ return year < other.year; }
};
#endif
