#include <iostream>
#include <string>
#include <map>
/*
 Function declaration
*/
std::string desencripta(const std::string&, std::map<char, char>);

/*
  El método utilizado para que éste sea reversible, debe ser una función
  f(a)=b, tal que f sea biyectiva, es decir todos los valores de la imagen
  tienes pre-imagen y además no existen dos valores que tengan la misma imagen.
*/
int main(){
  std::string texto = "rovvy hycvn";
  std::map<char, char> caesar;

  // Se asignan valores desplazados a cada uno de los dígitos, de forma opuesta
  // a su encriptación (Si era (e + k mod ds), dónde e' = (e + k), entonces el
  // método de desencriptación es ((e' - k) mod ds).
  // K  -> displacement
  // DS -> distance
  const int ds = 'z' - 'a', k = 10;
  for(int i = 0; i < ds; i++){
    caesar['a' + i] = 'a' + ((((i - k) % ds) + ds) % ds);
  }
  std::cout << "El texto encriptado es" << std::endl;
  std::cout << texto << std::endl;
  std::cout << "El texto encriptado es" << std::endl;
  std::cout << desencripta(texto, caesar) << std::endl;

  return 0;
}

std::string desencripta(const std::string& str, std::map<char, char> map){
  std::string encriptado = "";
  for(int i = 0; i < str.size(); i++){
    encriptado += map[str.at(i)];
  }
  return encriptado;
}
