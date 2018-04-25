SRC = src
INC = include
OBJ = obj
BIN = bin
CXX = g++
CPPFLAGS = -Wall -g  -I$(INC) -c


all: $(BIN)/Elemento_Maximo_Minimo $(BIN)/Elemento_Maximo_Minimo_Matriz $(BIN)/Mayor_Frecuencia $(BIN)/ZapatosYPies

# ************ Compilación de módulos ************
$(BIN)/Elemento_Maximo_Minimo:
	$(CXX) $(SRC)/ElementoMaximoMinimo.cpp -o $(BIN)/Elemento_Maximo_Minimo

$(BIN)/Elemento_Maximo_Minimo_Matriz:
	$(CXX) $(SRC)/ElementoMaximoMinimoMatriz.cpp -o $(BIN)/Elemento_Maximo_Minimo_Matriz

$(BIN)/Mayor_Frecuencia:
	$(CXX) $(SRC)/MayorFrecuencia.cpp -o $(BIN)/Mayor_Frecuencia

$(BIN)/ZapatosYPies:
	$(CXX) $(SRC)/ZapatosYPies.cpp -o $(BIN)/ZapatosYPies

# ************ Limpieza ************
clean :
	-rm $(BIN)/*

mrproper : clean
	-rm $(BIN)/* doc/html/*
