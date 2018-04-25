#include "abb.h"
#include <string>
#include <sstream>
#include <cstdlib>
#include <vector>
using namespace std;

void ListarAbb(ABB<int> &ab_bus){
  ABB<int>::nodo n;
 cout<<endl<<"Elementos ordenados: ";
 
 for (n=ab_bus.begin();n!=ab_bus.end();++n){
	   cout<<*n<<" ";
 }
}
int main(){
 vector<int>v;
 int a;
 while (cin>>a){
	   v.push_back(a);
 }
cin.clear(); 
 ABB<int>ab_bus;
 
 for (int i=0;i<v.size();i++){
	   ab_bus.Insertar(v[i]);
 }
 ListarAbb(ab_bus);
 	   
int x;

cout<<endl<<"Dime un elemento a borrar: ";
while (cin>>x) {
if (ab_bus.Buscar(x)){
	  cout<<endl<<x<<" esta"<<endl;
	  ab_bus.Borrar(x);
}
else{ cout<<"El elemento "<<x<<" no esta"<<endl;
}
ListarAbb(ab_bus);
}

 
}