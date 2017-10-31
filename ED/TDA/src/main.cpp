#include <iostream>
#include <vector>
#include <string>
#include <ctime>
using namespace std;

int main(int nargs, char* args[]){
	Encyclopedia enciclopedia;

	string path1("../data/timeline_algorithms.txt");
	enciclopedia.load_content(path1);
	string path2("../data/timeline_movies.txt");
	enciclopedia.load_content(path2);
	string path3("../data/timeline_science.txt");
	enciclopedia.load_content(path3);
	string path4("../data/timeline_worldhistory.txt");
	enciclopedia.load_content(path4);

	cout << "--------------------------------" << endl;
	cout << "RESULT OF THE SEARCHES" << endl;

	cout << "Find elements from 1997: " << endl;
	cout << enciclopedia.findByYear(1997);

	cout << "Find elements from 20th century: " << endl;
	cout << enciclopedia.findByCentury(20);

	cout << "--------------------------------" << endl;
	string tags;
	cout << "Which tags are you looking for?: ";
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
