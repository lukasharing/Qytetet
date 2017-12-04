#include <iostream>
#include <vector>
#include <string>
#include <ctime>
#include "../include/encyclopedia.h"

using namespace std;
int main(int nargs, char* args[]){
	Encyclopedia enciclopedia_casual;
	string path1("../data/timeline_algorithms.txt");
	string path2("../data/timeline_movies.txt");
	enciclopedia_casual.load_content(path1);
	enciclopedia_casual.load_content(path2);

	Encyclopedia enciclopedia_history;
	string path3("../data/timeline_science.txt");
	string path4("../data/timeline_worldhistory.txt");
	enciclopedia_history.load_content(path3);
	enciclopedia_history.load_content(path4);

	cout << "--------------------------------" << endl;
	cout << "RESULT OF THE SEARCHES" << endl;

	cout << "Find elements from 1997: " << endl;
	cout << enciclopedia_casual.findByYear(1997);

	cout << "Find elements from 20th century: " << endl;
	cout << enciclopedia_history.findByCentury(20);

	cout << "--------------------------------" << endl;
	string tags;
	cout << "Which tags are you looking for?: ";

	Encyclopedia union = union_encyclopedia(enciclopedia_casual, enciclopedia_history);

	getline(cin, tags);
	while(tags != " "){
		cout << "Find elements that contains "<< tags <<": " << endl;
		clock_t tini = clock();
		cout << enciclopedia.findByTags(tags);
		clock_t tfin = clock();
		cout << "Tiempo que ha tardado  : " << (tfin - tini) / (double)CLOCKS_PER_SEC << "s" << endl;
		cout << "Which tags are you looking for?: ";
		getline(cin, tags);
	}
	return 0;
}
