#include<iostream>
#include<string>
#include<stack>
#include<cmath>
using namespace std;


int toNum(char a){ return (a - '0'); }
bool isMathOperator(char c){ return (c == '^' || c == '*' || c == '/' || c == '+' || c == '-'); }
int operate(int a, char c, int b){ return(c=='^'?pow(a,b):c=='*'?(a*b):c=='/'?(a/b):c=='+'?(a+b):(a-b)); }
int postfijo(std::string str){
  std::stack<int> op;
  for(int  i = 0; i < str.size() - 2; i++){
    char c0 = str.at(i + 0);
    char c1 = str.at(i + 1);
    char c2 = str.at(i + 2);
    bool n0 = isMathOperator(c0);
    bool n1 = isMathOperator(c1);
    int id =  n1 | n0<<1;
    if(isMathOperator(c2)){
      int a, b;
      if(id == 0){
        a = toNum(c0);
        b = toNum(c1);
      }else if(id == 1){
        a = op.top();
        op.pop();
        if(op.empty()){
          b = toNum(c0);
        }else{
          b = op.top();
          op.pop();
        }
      }else if(id == 2){
        a = op.top();
        b = toNum(c1);
        op.pop();
      }
      int operation = operate(a, c2, b);
      op.push(operation);
      cout << a << c2 << b << " = " << operation << endl;
    }
  }
}

int main(){
  postfijo("3344+*+");
}
