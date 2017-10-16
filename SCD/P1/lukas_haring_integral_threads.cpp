// This code is written by Lukas Haring García from the university of Granada.
#include <iostream>
#include <vector>
#include <thread>
#include <future>
#include <cmath>
using namespace std;
#define PI 3.141592653589793238462


const double a = 0.f, b = 1.f;
const int SLICES = 1000000;
double f(double x){ return 4.f/(x * x + 1.f); }

// A method that calculates the integral by segments.
double integral_calculation_portion(int p, int total){
	double heights 	= 0.f;
	double distance 	= (b - a) / total;
	double step 		= distance / SLICES;
	double region 	= a + distance * p;

	for(double i = 0.f; i < distance; i+= step){
		heights += f(region + i);
	}
	double PI_Sliced = heights / SLICES;
	cout << "Thread Portion PI: " << PI_Sliced << endl;
	return PI_Sliced;
}

int main(){
	vector<future<double>> futures;

	int num_threads = 0;

	do{cout << "How many threads do you want to use?: "; cin >> num_threads;}while(num_threads < 0);


	double calculating_PI = 0.f;
	for(int i = 0; i < num_threads; i++){
		futures.push_back(async(integral_calculation_portion, i, num_threads));
	}
	for( auto &e : futures){
		calculating_PI += e.get();
	}
	calculating_PI /= num_threads;
	cout << "<--- PI RESULTS --->" << endl;
	cout << "Real PI: " << PI << endl;
	cout << "PI: " << calculating_PI << endl;
	cout << "Error: " << (PI - calculating_PI) << endl;
	return 0;
}
