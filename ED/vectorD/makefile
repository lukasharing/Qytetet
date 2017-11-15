INCLUDE = include
LIB     = lib
OBJ     = obj
SRC = src
BIN = bin
CXX = g++
CPPFLAGS = -Wall -g -c
AR = ar rvs
AR_MACOS = libtool -static -o


all: $(BIN)/main $(BIN)/frequency

# Binario: main
$(BIN)/main : $(OBJ)/main.o
	$(CXX) -o $(BIN)/main $(OBJ)/main.o -I$(INCLUDE)

$(OBJ)/main.o : $(SRC)/main.cpp
	$(CXX) $(CPPFLAGS)  -o $(OBJ)/main.o $(SRC)/main.cpp -I$(INCLUDE)

# Binario: frequency
$(BIN)/frequency : $(OBJ)/frequency.o
	$(CXX) -o $(BIN)/frequency $(OBJ)/frequency.o -I$(INCLUDE)

$(OBJ)/frequency.o : $(SRC)/frequency.cpp
	$(CXX) $(CPPFLAGS)  -o $(OBJ)/frequency.o $(SRC)/frequency.cpp -I$(INCLUDE)

#************ Generación de documentación ******************
documentacion:
	doxygen doc/doxys/Doxyfile


# ************ Limpieza ************
clean :
	-rm $(OBJ)/* $(SRC)/*~ $(INC)/*~ ./*~

mrproper : clean
	-rm $(BIN)/* doc/html/*
