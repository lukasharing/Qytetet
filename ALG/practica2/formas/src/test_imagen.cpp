#include "imagen.h"
#include<fstream>
#include<iostream>


int Contar_Figuras(const Imagen& im, const Imagen& f, int x, int y, int iw, int ih, double k){
  std::cout << iw << " ," << ih << std::endl;
  if(iw * ih < 68 * 68){
    return 0;
  }

  double kn = 0;

  for(int j = y; j < y + ih; ++j){
    for(int i = x; i < x + iw; ++i){
      kn += im(i, j);
    }
  }
  kn /= iw * ih;

  std::cout << k << " => " << kn << std::endl;

  iw /= 2;
  ih /= 2;

  int total = 0;
  // Arriba Izq
  total += Contar_Figuras(im, f,      x,      y, iw, ih, k);
  // Arriba Der
  total += Contar_Figuras(im, f, x + iw,      y, iw, ih, k);
  // Abajo Izq
  total += Contar_Figuras(im, f,      x, y + ih, iw, ih, k);
  // Abajo Der
  total += Contar_Figuras(im, f, x + iw, y + ih, iw, ih, k);

  return total;
}

int main(int argc, char * argv[]){
  if (argc != 3){
    cout << "Los parametros son: ";
    cout << "1.-Dime el nombre de una imagen" << endl;
    cout << "2.-Dime el nombre de una forma" << endl;
    return 0;
  }

  Imagen I, forma;
  I.LeerImagen(argv[1]);
  forma.LeerImagen(argv[2]);

  double k = 0.0;
  for(int j = 0; j < forma.num_filas(); ++j){
    for(int i = 0; i < forma.num_cols(); ++i){
      k += forma(i, j);
    }
  }
  k /= forma.num_filas() * forma.num_cols();

  int total = Contar_Figuras(I, forma, 0, 0, I.num_filas(), I.num_cols(), k);
  std::cout << "Total: " << total << std::endl;
}
