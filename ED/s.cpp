#include<iostream>
#include<string>
#include<queue>
#include<stack>
#include<cmath>
using namespace std;

struct compare_vocals{
  bool operator()(const std::string& left, const std::string& right) const{
    if(left.size() < right.size()){
      return true;
    }else if(left.size() == right.size()){
      return left.compare(right) == 1;
    }else{
      return false;
    }
  };
};

int main(){
  priority_queue<std::string, vector<std::string>, compare_vocals> cola;
  cola.push("abcdefg");
  cola.push("abeci");
  cola.push("aeiou");
  cola.push("int main(int, char**)");

  while(!cola.empty()){
    cout << cola.top() << endl;
    cola.pop();
  }
}
