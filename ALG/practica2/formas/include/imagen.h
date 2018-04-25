/**
 * @file imagen.h
 */
#ifndef __IMAGEN_H
#define __IMAGEN_H
#include "imagenES.h"

#include <cassert>
#include <iostream>
#include <string>

using namespace std;

/** @brief TDA Imagen. Es una matriz de nxm filas, columnas de niveles de gris
 * 
 * */
 
class Imagen{
  private:
    unsigned char ** data;//< espacio para almacenar los pixeles de la imagen
    int nf,nc; //< numero de filas y columnas
    /** 
     * @brief Elimina la memoria asignada a la matriz
     */
    void Borrar();
    /**
     * @brief Copia una imagen en otra.
     */
    void Copiar(const Imagen &I);
  public:
    /**
     * @brief Constructor por defecto
     */
   Imagen();
    /**
     * @brief Constructor por parametros. Reserva espacio para una imageen con un determinado nuemro de filas 
     * y columnas. Todos los pixeles se inician a negro.
     * @param f: numero de filas
     * @param c: numero de columnas
     */
   Imagen(int f,int c);
   /**
     * @brief Constructor de copia
     * @param I: imagen a ser copiada
    */
   Imagen(const Imagen & I);
    /**
     * @brief Asignacion
     * @param I: imagen que se asigna
    */
   Imagen & operator=(const Imagen & I);
    /**
     * @brief Destructor
    */
   ~Imagen();
   
   //set y get
   /** @brief Permite acceder al pixel en la fila i columna j
    *  @param i: fila
    *  @param j: columna
    *  @return el pixel en la posicion i,j
    */
   unsigned char & operator ()(int i,int j);
   
   const unsigned char & operator ()(int i,int j)const;
   
   
   /** @brief Escribe una image a disco
    *  @param nombre: nombre del fichero en disco
    */
   void EscribirImagen(const char * nombre);
   
   /** @brief Lee una image de disco y la carga en memoria
    *  @param nombre: nombre del fichero en disco
    */
   
   void LeerImagen (const char *nombre);
   
   /** @brief Devuelve el numero de filas de la imagen
    *  
    */
   
   int num_filas()const{return nf;}
   
   /** @brief Devuelve el numero de columnas de la imagen
    *  
    */
   int num_cols()const{return nc;}
   
   
   /** @brief Pega una imagen a partir de una posicion
    * @param posi: fila  donde pegar la imagen
    * @param posj: columna donde pegar la imagen
    * @param I: imagen a pegar
    * @exception: si no cabe toda la imagen a partir de la posicion pega lo que pueda
    * sin salirse de los limites de la imagen
    * */
   void PutImagen(int posi,int posj, const Imagen &I);
   
};   
   
#endif