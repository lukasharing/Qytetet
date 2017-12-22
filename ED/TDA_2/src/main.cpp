#include <iostream>
#include <vector>
#include <string>
#include <ctime>
#include "../include/encyclopedia.h"
#include "../include/century_node.h"
#include "../include/year_node.h"

using namespace std;
int main(int nargs, char* args[]){
	Encyclopedia enciclopedia_casual0;
	Encyclopedia enciclopedia_casual1;
	string path1("../data/timeline_algorithms.txt");
	string path2("../data/timeline_movies.txt");
	enciclopedia_casual0.load_content(path1);
	enciclopedia_casual1.load_content(path2);
	//Encyclopedia casual = encyclopedia_union(enciclopedia_casual0, enciclopedia_casual1);

	//cout << enciclopedia_casual0.to_string() << endl;
	//cout << enciclopedia_casual1.to_string() << endl;
	//cout << casual.to_string() << endl;

	//cout << enciclopedia_casual0.statistics() << endl;

	cout << "--------------------------------" << endl;
	cout << "RESULT OF THE SEARCHES" << endl;

	cout << "Find elements from 2002: " << endl;
	//CenturyNode::iterator year = casual.getYear(2002);
	//std::cout << year->second->to_string() << std::endl;

	//Encyclopedia::iterator century = enciclopedia_casual0.getCentury(20);
	//std::cout << century->second->to_string() << std::endl;

	cout << "--------------------------------" << endl;
	string tags;
	cout << "Which tags are you looking for?: ";
	getline(cin, tags);
	while(tags != " "){
		cout << "Find elements that contains "<< tags <<": " << endl;
		clock_t tini = clock();
		//cout << enciclopedia.findByTags(tags);
		clock_t tfin = clock();
		cout << "Tiempo que ha tardado  : " << (tfin - tini) / (double)CLOCKS_PER_SEC << "s" << endl;
		cout << "Which tags are you looking for?: ";
		getline(cin, tags);
	}
	return 0;
}
