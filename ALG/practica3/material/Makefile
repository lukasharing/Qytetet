SRC = src
INC = include
OBJ = obj
BIN = bin
CXX = g++
CPPFLAGS = -Wall -g  -I$(INC) -c -std=c++11


all: $(BIN)/cifras

# ************ Generación de documentación ******************
documentacion:
	doxygen doc/doxys/Doxyfile

# ************ Compilación de módulos ************
$(BIN)/cifras: $(OBJ)/cifras.o
	$(CXX) -o $(BIN)/cifras  $(OBJ)/cifras.o

$(OBJ)/cifras.o: $(SRC)/cifras.cpp $(INC)/matriz_vector.h 
	$(CXX) $(CPPFLAGS) $(SRC)/cifras.cpp -o $(OBJ)/cifras.o


# ************ Limpieza ************
clean :
	-rm $(OBJ)/* $(SRC)/*~ $(INC)/*~ ./*~

mrproper : clean
	-rm $(BIN)/* doc/html/*
