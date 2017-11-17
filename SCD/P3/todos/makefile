all: barberia_SU fumadores_SU prodcons_SU_fifo prodcons_SU_lifo prodcons_SC_fifo

barberia_SU: barberia_SU.cpp HoareMonitor.cpp HoareMonitor.hpp
	g++ -std=c++11 -I./ barberia_SU.cpp HoareMonitor.cpp -o barberia_SU -pthread

fumadores_SU: fumadores_SU.cpp HoareMonitor.cpp HoareMonitor.hpp
	g++ -std=c++11 -I./ fumadores_SU.cpp HoareMonitor.cpp -o fumadores_SU -pthread

prodcons_SU_fifo: prodcons_SU_fifo.cpp HoareMonitor.cpp HoareMonitor.hpp
	g++ -std=c++11 -I./ prodcons_SU_fifo.cpp HoareMonitor.cpp -o prodcons_SU_fifo -pthread

prodcons_SU_lifo: prodcons_SU_lifo.cpp HoareMonitor.cpp HoareMonitor.hpp
	g++ -std=c++11 -I./ prodcons_SU_lifo.cpp HoareMonitor.cpp -o prodcons_SU_lifo -pthread

prodcons_SC_fifo: prodcons_SC_fifo.cpp HoareMonitor.cpp HoareMonitor.hpp
	g++ -std=c++11 -I./ prodcons_SC_fifo.cpp HoareMonitor.cpp -o prodcons_SC_fifo -pthread
