#include <iostream>
#include <iterator>
#include <map>
#include <utility>
#include <cassert>

void showpannel(){
  std::cout << "---------------------" << std::endl;
  std::cout << "Add float pair (a)" << std::endl;
  std::cout << "Remove float pair (r)" << std::endl;
  std::cout << "Show float pair(s)" << std::endl;
  std::cout << "Show all (l)" << std::endl;
  std::cout << "Exit (anything else)" << std::endl;
  std::cout << "Write your option: ";
}

void show_pair(std::map<std::pair<float, float>, int>::iterator it){
  std::cout << "Pair (" << (it->first).first << ", " << (it->first).second << ") added " << it->second << " times." << std::endl;
}

int main(){
  std::map<std::pair<float, float>, int> pairs;

  bool exit = true;
  do{
    showpannel();
    char c;
    std::cin >> c;
    if(c == 'a'){
      float x, y;
      std::cout << "Add the pair (x, y): ";
      std::cin >> x;
      std::cin >> y;
      std::pair<float, float> p(x, y);
      pairs[p]++;
    }else if(c == 's'){
      std::cout << "Write the index of the pair you want to show: ";
      int k;
      std::cin >> k;
      std::map<std::pair<float, float>, int>::iterator it = pairs.begin();
      for(int i = 0; i < k && it != pairs.end(); ++i, ++it);
      if(it == pairs.end()){
        std::cout << "Pair nº " << k << " doesn't exist" << std::endl;
      }else{
        show_pair(it);
      }
    }else if(c == 'r'){
      int k;
      std::cout << "Write the index of the pair you want to remove: ";
      std::cin >> k;
      assert(k >= 0 && k < pairs.size());
      std::map<std::pair<float, float>, int>::iterator it = pairs.begin();
      for(int i = 0; i < k; ++i, ++it);
      pairs.erase(it);
    }else if(c == 'l'){
      std::map<std::pair<float, float>, int>::iterator it;
      std::cout << "-----------------" << std::endl;
      std::cout << "All the pairs are (" << pairs.size() << "): " << std::endl;
      for(it = pairs.begin(); it != pairs.end(); ++it){
        show_pair(it);
      }
    }else{
      exit = false;
    }
  } while(exit);
  return 0;
}
