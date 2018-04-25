SRC = src
INC = include
OBJ = obj
BIN = bin
CXX = g++
CPPFLAGS = -Wall -g  -I$(INC) -c


all: $(BIN)/test_imagen

# ************ Generación de documentación ******************
documentacion:
	doxygen doc/doxys/Doxyfile

# ************ Compilación de módulos ************
$(BIN)/test_imagen: $(OBJ)/test_imagen.o $(OBJ)/imagen.o $(OBJ)/imagenES.o 
	$(CXX) -o $(BIN)/test_imagen  $(OBJ)/test_imagen.o $(OBJ)/imagen.o $(OBJ)/imagenES.o 

$(OBJ)/test_imagen.o: $(SRC)/test_imagen.cpp $(INC)/imagenES.h $(INC)/imagen.h 
	$(CXX) $(CPPFLAGS) $(SRC)/test_imagen.cpp -o $(OBJ)/test_imagen.o
$(OBJ)/imagen.o: $(SRC)/imagen.cpp $(INC)/imagen.h $(INC)/imagenES.h
	$(CXX) $(CPPFLAGS) $(SRC)/imagen.cpp -o $(OBJ)/imagen.o
		
$(OBJ)/imagenES.o: $(SRC)/imagenES.cpp $(INC)/imagenES.h 
	$(CXX) $(CPPFLAGS) $(SRC)/imagenES.cpp -o $(OBJ)/imagenES.o	


# ************ Limpieza ************
clean :
	-rm $(OBJ)/* $(SRC)/*~ $(INC)/*~ ./*~

mrproper : clean
	-rm $(BIN)/* doc/html/*
