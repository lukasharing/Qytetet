#include <iostream>
#include <sstream>
#include <map>

std::multimap<std::string, std::string> parameters(const std::string&);

int main(){
  std::string command = "tar -z -x -f fichero";
  std::cout << command << std::endl;
  std::multimap<std::string, std::string> params = parameters(command);

  std::string rs[2] = {"args", "flags"};
  std::pair <std::multimap<std::string, std::string>::iterator, std::multimap<std::string, std::string>::iterator> ret;
  for(int i = 0; i < 2; ++i){
    ret = params.equal_range(rs[i]);
    std::cout << rs[i] << " is/are:";
    std::multimap<std::string, std::string>::iterator ti;
    for (ti = ret.first; ti != ret.second; ++ti){
      std::cout << ' ' << ti->second;
    }
    std::cout << std::endl;
  }
  return 0;
};

// Suponiendo que se ha escrito un comando correctamente, de la forma:
// <nombre comando> <flags/args>
// Dónde flags y args pueden intercalarse.
std::multimap<std::string, std::string> parameters(const std::string& str){
  std::multimap<std::string, std::string> result;
  std::stringstream stream(str);
  std::string buff;
  stream >> buff; // Ignore first word:
  while(stream >> buff){
    std::string type;
    // Vemos si comienza con un guión, si es así, es un argumento.
    if(buff[0] == '-'){
      type = "flags";
      buff.erase(0, 1);
    }else{
      type = "args";
    }
    result.insert(std::pair<std::string, std::string>(type, buff));
  }
  return result;
};
