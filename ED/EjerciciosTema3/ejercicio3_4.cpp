#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <cassert>

/*
 Function declaration
*/
std::map<std::string, int> word_map(const std::string&);
std::multimap<int, std::string> multi_word(const std::map<std::string, int>&);

/*
  El método utilizado para que éste sea reversible, debe ser una función
  f(a)=b, tal que f sea biyectiva, es decir todos los valores de la imagen
  tienes pre-imagen y además no existen dos valores que tengan la misma imagen.
*/
int main(){
  std::string path_name = "test.txt";
  std::map<std::string, int> word_counter = word_map(path_name);
  std::map<std::string, int>::iterator it;
  for(it = word_counter.begin(); it != word_counter.end(); ++it){
    std::cout << (*it).first << " -> " << (*it).second << std::endl;
  }

  std::multimap<int, std::string> multi_counter = multi_word(word_counter);

  std::pair <std::multimap<int, std::string>::iterator, std::multimap<int, std::string>::iterator> ret;

  const int size = 2;
  ret = multi_counter.equal_range(size);
  std::cout << "Words shown " << size << " time/s is/are: " << std::endl;
  std::multimap<int, std::string>::iterator ti;
  for (ti = ret.first; ti != ret.second; ++ti){
    std::cout << ' ' << ti->second;
  }
  return 0;
}

std::map<std::string, int> word_map(const std::string& path){
  std::ifstream file(path.c_str());
  std::map<std::string, int> result;
  assert(file.is_open());
  std::string word;
  while(file >> word){ result[word]++; };
  file.close();
  return result;
}


std::multimap<int, std::string> multi_word(const std::map<std::string, int>& map){
  std::multimap<int, std::string> result;
  std::map<std::string, int>::const_iterator it;
  for(it = map.begin(); it != map.end(); ++it){
    result.insert(std::pair<int, std::string>((*it).second, (*it).first));
  }
  return result;
}
