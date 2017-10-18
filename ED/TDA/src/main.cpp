#include <iostream>
#include <sstream>
#include <fstream>
#include <vector>
#include <string>

#include "../include/year_node.h"
#include "../include/event_node.h"

using namespace std;

int main(int nargs, char* args[]){
	ifstream reader;
	string path = "../data/timeline_algorithms.txt";
	reader.open(path.c_str());
	if(reader.is_open()){
		string events = "";
		while(getline(reader, events)){
			istringstream event(events);
			int events_year;
			event >> events_year;
			event.ignore(1); // Bc next char is #, jump it over.
			string event_info;
			YearNode* year_node = new YearNode(events_year);
			while(getline(event, event_info, '#')){
				year_node->addEvent(event_info);
			}
			cout << "Tags: " << year_node->getTags() << endl;
			cout << "Events Added: " << year_node->getSize() << endl;
		}
	}else{
		cout << "The file " << path << " couldn't be opened";
	}
	reader.close();
	return 0;
}
