#include "imagen.h"
void Imagen::Borrar(){
  for (int i=0;i<nf;i++)
    delete[]data[i];
  delete []data;
}
/*********************************/
void Imagen::Copiar(const Imagen &I){
    
  nf = I.nf;
  nc = I.nc;
  data = new unsigned char*[nf];
  for (int i=0;i<nf;i++){
    data[i]=new unsigned char [nc];
    for (int j=0;j<nc;j++){
	data[i][j]=I.data[i][j];
    }
  }  
	
}  
/*********************************/
Imagen::Imagen(){
  data=0;
  nf=nc=0;
}

/*********************************/
Imagen::Imagen(int f,int c){
  nf = f;
  nc = c;
  data = new unsigned char*[nf];
  for (int i=0;i<nf;i++){
    data[i]=new unsigned char[nc];
    for (int j=0;j<nc;j++){
	data[i][j]=0;;
	
    }
  }  


}
/*********************************/
Imagen & Imagen::operator=(const Imagen & I){
   if (this!=&I){
	Borrar();
	Copiar(I);
   }
   return * this;
}   
/*********************************/
Imagen::~Imagen(){
  Borrar();
}

/*********************************/
unsigned char & Imagen::operator()(int i,int j){
  assert(i>=0 && i<nf && j>=0 && j<nc*3);
  return data[i][j];
}
/*********************************/
const unsigned char  & Imagen::operator()(int i,int j)const{
  assert(i>=0 && i<nf && j>=0 && j<nc);
  return data[i][j];
}

/*********************************/
void Imagen::EscribirImagen(const char * nombre){
      unsigned char * aux = new unsigned char [nf*nc];
      
      int total = nf*nc;
      for (int i=0;i<total;i++){
	   int posi = i /(nc);
	   int posj = (i%(nc));
	
	    aux[i]=data[posi][posj];
	    
	     
	 }    
	
      if (!EscribirImagenPGM (nombre, aux,nf,nc)){
	  cerr<<"Ha habido un problema en la escritura de "<<nombre<<endl;
      }	  
      delete[]aux;
      
}  
/*********************************/
void Imagen::LeerImagen(const char * nombre){
      int f,c;
      unsigned char * aux;
      
      //vemos el tipo de imagen y iniciamos f y c;
      LeerTipoImagen(nombre, f, c);
      
      Imagen I(f,c);
      int total = f*c;
      aux = new unsigned char [f*c];
      if (!LeerImagenPGM ( nombre, f,c,aux)){
	 cout<<"No puedo leer la imagen "<<nombre<<endl;
	 delete[]aux;
	 return ;
      }

      for (int i=0;i<total;i++){
	   int posi = i /(c);
	   int posj = (i%c);
	
	     I.data[posi][posj]=aux[i];
	     
	 }    
	  
      *this = I;   
      
      delete []aux;
      
}	

  
/*********************************/
void Imagen::PutImagen(int posi,int posj, const Imagen &I){
    
    for (int i=0;i<I.nf;i++)
      for (int j=0;j<I.nc;j++)
	if (i+posi>=0 && i+posi<nf && j+posj>=0 && j+posj<nc){
	   data[i+posi][j+posj]=I.data[i][j];
	 
	  
	}  
	
	    
}
