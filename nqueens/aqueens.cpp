/*

Grace Handler, Clara Hunnewell, Kote Mushegiani
Professor Majercik
Artificial Intelligence
November 13, 2016

This program was adapted from pseudocode in our textbook and instructions from
Professor Majercik. There are 5 variations of the min-conflicts local search
algorithm. You can call the appropriate one from the command line as explained in
the README and also set the maximum number of steps and the size of the problem
(number of queens).

The 5 algorithms are: BASIC, GREEDY, RANDOM, SMART-START, and FIRST-BETTER.
Both SMART-START and FIRST-BETTER are run on the fastest of the former 3 algorithms,
which is BASIC. There are global booleans for SMART-START and FIRST-BETTER which
can be set from the command line and used respectively.

*/

#include <vector>
#include <cstdlib>
#include <iostream>
#include <time.h>
#include <stdlib.h> 
#include <thread>         // std::this_thread::sleep_for
#include <chrono>         // std::chrono::seconds

//
// struct Info is used for the greedy function.
// It holds a potential move of queen row to column col, 
// and the number of conflicts that this move will cause.
//
struct Info {
	int conflicts;
	int row;
	int col;
};

int N; 						// the number of queens for the board
int STEPS;  				// the number of steps to take before giving up
int actualSteps;

bool firstBetter = false;   // set by user from commandline; if true: run FIRST-BETTER algorithm
bool smartStart = false;    // set by user from commandline; if true: run SMART-START algorithm
bool foundSolution = false;

//
// printBoard: takes a vector of queen locations and prints a board with 
// queens in those locations
//
void printBoard(std::vector<int> queenLocations) {

	for(int r=0; r<queenLocations.size(); r++) {
		for(int c=0; c<queenLocations.size(); c++) {

			// if you are in a column where a queen is
			if (queenLocations[r] == c) { 
				std::cout<<"1 ";
			}
			else
				std::cout<<"0 ";
		}
		std::cout<<std::endl;
	}
	std::cout<<std::endl;
} // end printBoard


//
// numberOfConflicts counts the number of conflicts that a queen in row r and 
// column c has on board.
// Parameters: int r (the queen that you are considering), int c (the column that
// that queen is in), vector<int> queenLocations (vector that holds the current 
// locations/columns of the queens on the board)
// return int - the number of conflicts
//
int numberOfConflicts(std::vector<int> queenLocations, int r, int c) {
	
	if (r < 0 || c < 0) { // check parameters
		std::cout << "Error. Invalid location on board." << std::endl;
		abort();
	}
	int numConflicts = 0;

	// interate throough the queens that have been already placed
	for(int i=0; i<queenLocations.size(); i++) { 

		/* Do not count as conflict if the queen you are considering is in the 
		same row as r because you will only ever have one queen in each row 
		and that queen will be moved if you find a better location for it. */
		if (i != r) { 
			if (queenLocations[i] == c || (abs(i-r) == abs(queenLocations[i]-c))) 
				numConflicts++;
		}
	}
	return numConflicts;
} // end numberOfConflicts

// 
// isSolution takes queenLocations and then will check to see if putting 
// n queens in those n columns will be a solution.
// returns: std::vector<int> - a vector that holds all of the queens that currently
// have conflicts.
//
std::vector<int> isSolution(std::vector<int> queenLocations) {

	if (queenLocations.empty()) { // check parameters
		std::cout << "Error. No queens on board." << std::endl;
		abort();
	}
	// vector to hold all queens with conflicts
	std::vector<int> conflictingVariables; 

	for(int r = 0; r < N; r++) { 		// iterate through all queens
		int c = queenLocations[r]; 		// get column of queen r
	
		// if you have conflicts, add the queen to your vector
		if(numberOfConflicts(queenLocations,r,c) != 0) 
			conflictingVariables.push_back(r);
	}

	return conflictingVariables;
} // end isSolution

// 
// minConflictsGreedy uses a greedy approach to the n queens problem.  It calculates the 
// minconflicts heuristic for every queen and then picks the queen and the move that
// leads to the fewest conflicts.
// Parameters: vector<int> queenLocations (the locations of the queens on the board), 
// int maxSteps (the number of tries that the computer gets to find a solution)
// returns: the queenLocations that solve the n queens problem or, in the case that no 
// solution is found, the queenLocations after the last step
//  
std::vector<int> minConflictsGreedy(std::vector<int> queenLocations, int maxSteps) {

	if (maxSteps<0 || queenLocations.empty()) {
		std::cout << "Error. No board given or max steps incorrectly defined." << std::endl;
		abort();
	}

	for(int i=0; i<maxSteps; i++) {

		// vector of conflicting queens
		std::vector<int> conflictingVars = isSolution(queenLocations);
		if(conflictingVars.size() == 0) {		// if empty
			foundSolution = true;
			actualSteps = i;
			return queenLocations;
		} 
		else {

			/* vector of current bests which helps in the process of randomly
			choosing between two possible columns to put the queen in if 
			there is a tie */
			std::vector<Info> currentMins;

			// struct to hold information about current best
			Info info;
			info.conflicts = INT_MAX;
			info.row = -1;
			info.col = -1;

			// for each conflicting queen
			for(int m=0; m < conflictingVars.size(); m++) {
				int r = conflictingVars[m]; // get row of conflicting queen

				// for each possible column you can put it in
				for(int c = 0; c < queenLocations.size(); c++) {

					// calculate the number of conflicts
					int tmp = numberOfConflicts(queenLocations,r,c); 	

					// if number of conflicts less than current best, update information
					// in info and empty your vector of current bests. Add the new current
					// best to your vector.
					if(tmp < info.conflicts) {
						info.conflicts = tmp;
						info.row = r;
						info.col = c;
						if (currentMins.empty() == false){
							currentMins.clear();
						}
						currentMins.push_back(info);
					}
					// if you have a tie, add the move to your vector of current bests.
					else if (tmp == info.conflicts){
						info.row = r;
						info.col = c;
						currentMins.push_back(info);
					}
				}
			}

			// choose randomly among the current best moves and perform the move.
			int tieBreaker = rand() % currentMins.size(); 
			int queenToMove = currentMins[tieBreaker].row;
			queenLocations[queenToMove] = currentMins[tieBreaker].col;
	
		}
	} // end step for loop
	return queenLocations;
} // end minConflictsGreedy


// 
// minConflictsRandom uses the heuristic of min conflicts to solve the n queens problem.  
// It randomly chooses a queen and then, for that queen, it calculates the 
// minconflicts heuristic for each potential column that that queen could be placed in.  
// It then moves the queen to the column that leads to the fewest conflicts.
// Parameters: vector<int> queenLocations (the locations of the queens on the board), 
// int maxSteps (the number of tries that the computer gets to find a solution)
// returns: the queenLocations that solve the n queens problem or, in the case that no 
// solution is found, the queenLocations after the last step
// 
std::vector<int> minConflictsRandom(std::vector<int> queenLocations, int maxSteps) {

	if (maxSteps<0 || queenLocations.empty()) {
		std::cout << "Error. No board given or max steps incorrectly defined." << std::endl;
		abort();
	}
	

	for(int i=0; i<maxSteps; i++) {

		// vector of conflicting queens
		std::vector<int> conflictingVars = isSolution(queenLocations);
		if(conflictingVars.size() == 0) { // if empty
			foundSolution = true;
			actualSteps = i;
			return queenLocations;
		} else {

			/* vector of current bests which helps in the process of randomly
			choosing between two possible columns to put the queen in if 
			there is a tie */
			std::vector<int> currentMinIndices;

			// choose a random queen to move
			int randomConflictingVarIndex = rand() % conflictingVars.size();
			int randomConflictingVar = conflictingVars[randomConflictingVarIndex];

			int initialConflicts=-1; // number of conflicts your randomly chosen queen has
			if (firstBetter) {
				initialConflicts = numberOfConflicts(queenLocations, randomConflictingVar, queenLocations[randomConflictingVar]);
			}

			int currentMin = INT_MAX;
			bool moveOn = false; 

			for(int j=0; j<N; j++) { // for each potential queen location
				// calculate number of conflicts
				int t = numberOfConflicts(queenLocations, randomConflictingVar,j);

				// if you are using the first-better technique, and you have found 
				// a move that is "better" (results in less conflicts), make the move.
				if (firstBetter && t<initialConflicts) {
					queenLocations[randomConflictingVar] = j;
					moveOn = true;
					break;
				}

				// if number of conflicts less than current best, your current best
				// and empty your vector of current bests. Add the new current
				// best to your vector.				
				if(t < currentMin) {
					currentMin = t; // update current best
					if (currentMinIndices.empty() == false){
						currentMinIndices.clear();
					}
					currentMinIndices.push_back(j);
				}
				// if you have a tie, add the column to your vector of current bests.
				else if (t==currentMin) {
					currentMinIndices.push_back(j);
				}
			} // end for

			if (!moveOn){ // if you have already updated using the first-better technique

				// choose randomly among the current best moves and perform the move.
				int tieBreaker = rand() % currentMinIndices.size();
				queenLocations[randomConflictingVar] = currentMinIndices[tieBreaker] ;
			}
		} 
	} // end step for loop
	return queenLocations;
} // end minConflictsRandom

//
// Picks a random queen and then either moves it to a random square in the queens column
// or uses the min conflicts heuristic to place the queen. The probabilities are 0.4 for 
// random placenemt and 0.6 for choosing based on the min conflicts heuristic
//
std::vector<int> randomOrMinConflicts(std::vector<int> queenLocations, int maxSteps) {

	if (maxSteps<0 || queenLocations.empty()) {
		std::cout << "Error. No board given or max steps incorrectly defined." << std::endl;
		abort();
	}
	
	for(int i=0; i<maxSteps; i++) { 
		// search for queens with conflicts
		std::vector<int> conflictingVars = isSolution(queenLocations);
		if(conflictingVars.empty()) { // if there are none
				foundSolution = true;
				actualSteps = i;
			return queenLocations; // solution has been found
		} else {
			std::vector<int> currentMinIndices;
			int randomConflictingVarIndex = rand() % conflictingVars.size(); //pick a random queen
			int randomConflictingVar = conflictingVars[randomConflictingVarIndex];

			int magicNumber = rand() % 10; //generate magic number
			if(magicNumber <= 3) { //with prob. 0.4 place queen randomly
				int randomColumnForQueen = rand() % queenLocations.size();

				queenLocations[randomConflictingVar] = randomColumnForQueen;
			} else { //otherwise run the min conflicts heuristic as usual
				std::vector<int> currentMinIndices;
				int currentMin = INT_MAX;

				for(int j=0; j<N; j++) {
					int t = numberOfConflicts(queenLocations,randomConflictingVar,j);
					if(t < currentMin) {
						currentMin = t;
						if(currentMinIndices.empty() == false) {
							currentMinIndices.clear();
						} 
						currentMinIndices.push_back(j);
					} else if(t == currentMin) {
						currentMinIndices.push_back(j);
					}
				}

				int tieBreaker = rand() % currentMinIndices.size();
				queenLocations[randomConflictingVar] = currentMinIndices[tieBreaker];
			}	
		}
	}
	return queenLocations;
} // end randomOrMinConflicts

//
// Takes an interger n representing the number of queens to be placed on
// a nxn board and then randomly chooses column locations for these queens.
// return vector<int> - the vector of queen locations once they are placed
// 
std::vector<int> placeQueensRandom(int n) {

	if (n<0) { // check parameters
		std::cout<< "Error. Invalid number of queens." << std::endl;
		abort();
	}

	std::vector<int> queenLocations;
	
	for(int r=0; r<n; r++) {			// iterate through all queens
		int c = rand()%n; 				// choose a random column to put it in
		queenLocations.push_back(c);	// add it to your vector of queen locations
	}
	return queenLocations;
} // end placeQueensRandom

//
// placeQueensSmartStart takes an initialized board of zeroes and places queens
// so as to minimize the conflicts between the queen to be placed and all of the 
// queens already placed on the board.  
// int n - the number of queens to be placed
// return vector<int> - the vector of queen locations once they are placed
// 
std::vector<int> placeQueensSmartStart(int n) {

	std::vector<int> queenLocations; // the queen locations 
	int currentBest = INT_MAX; 
	int currentBestIndex = -1;
	int conflicts;
	int noSolution;

	// iterate through the board
	for(int r=0; r<n; r++) {
		noSolution = 0;
		for (int c = 0; c<n; c++) {

			// if you have found a location with less conflicts, update
			// your current best to that location
			conflicts = numberOfConflicts(queenLocations,r,c);
			if (conflicts<currentBest) {
				currentBest = conflicts;
				currentBestIndex = c;
			}
		}

		// Place the queen in the place with the least conflicts
		queenLocations.push_back(currentBestIndex); 

		// reset to move on to next queen
		currentBest = INT_MAX; 
		currentBestIndex = -1;
	}
	//std::cout << "This is our board placed smartly" <<std::endl;
	return queenLocations;
	
} // end placeQueensSmartStart

//
// main() function calls the appropriate algorithm above based upon 
// what the user inputs.
//
int main(int argc, char* argv[]) {

	std::vector<clock_t> timeForRun;
	std::vector<int> stepsForRun;
	if(argc < 2 || argc > 4) {
		std::cout<<"Incorrect Usage. Use -help for usage info\n";
		exit(1);
	} else if(argc == 2) {
		if(strcmp(argv[1],"-help") == 0) {
			std::cout<<"Usage: ./aqueens NUM_STEPS NUM_QUEENS ALGO_TYPE\n";
			std::cout<<"ALGO_TYPE is one of the following five: BASIC, GREEDY, RANDOM, SMART-START, FIRST-BETTER\n";
		}
		exit(0);
	} else if(argc == 4) {
		srand(time(NULL));

		int noSolution;

		for (int j=4;j<1000;j++) {
			N=j;
			noSolution = 0;
		
		for (int k = 0; k<10; k++) {
			foundSolution = false;

			STEPS = atoi(argv[1]);
			//N = atoi(argv[2]);

			std::vector<int> initialSeed; 

			clock_t t1, t2;

			if(strcmp(argv[3],"SMART-START") == 0) {
				smartStart = true;
				initialSeed = placeQueensSmartStart(N);
			} else {
				initialSeed = placeQueensRandom(N);
			}

			std::cout << "\nINITIAL BOARD: \n\n";
			printBoard(initialSeed);

			t1 = clock();

			if((strcmp(argv[3],"BASIC") == 0) || (strcmp(argv[3],"FIRST-BETTER") == 0) || (strcmp(argv[3],"SMART-START") == 0)) {
				initialSeed = minConflictsRandom(initialSeed, STEPS);
				if (firstBetter) {
					//std::cout<<"\nBOARD AFTER RUNNING FIRST-BETTER ALGO: \n\n";
					firstBetter = true;
				} else if (smartStart) {
					//std::cout<<"\nBOARD AFTER RUNNING SMART-START ALGO: \n\n";
				} else {
					//std::cout<<"\nBOARD AFTER RUNNING BASIC ALGO: \n\n";
				}
			} else if(strcmp(argv[3],"GREEDY") == 0) {
				//std::cout<<"\nBOARD AFTER RUNNING GREEDY ALGO: \n\n";
				initialSeed = minConflictsGreedy(initialSeed, STEPS);
			} else if(strcmp(argv[3],"RANDOM") == 0) {
				//std::cout<<"\nBOARD AFTER RUNNING RANDOM ALGO: \n\n";
				initialSeed = randomOrMinConflicts(initialSeed, STEPS);
			} 

			t2 = clock();
			std::cout<<"\nFINAL BOARD: \n\n";
			printBoard(initialSeed);

			//std::cout << "\nMAXIMUM ALLOWED STEPS IS: " << STEPS; 
			if (foundSolution) {
				//std::cout << "\nSolution found! :)\n";
				//std::cout << "\n--> Run time is " << (t2-t1) << " milliseconds.";
				timeForRun.push_back(t2-t1);
				stepsForRun.push_back(actualSteps);
				//std::cout << "\n--> Number of steps to find solution is " << actualSteps << ".\n\n";

			} else {
				noSolution++;
				//std::cout<<"\nNo solution was found. :(\n\n";
			}

			if (noSolution > 1) {
				std::cout<<"UNRELIABLE. ABORTING.\n";
				abort();
			}
			//sleep(10);
			initialSeed = std::vector<int>(); 
			std::this_thread::sleep_for (std::chrono::seconds(1));
		



		}

			int sumSteps = 0;
			int sumTime = 0;
			for (int k=0; k<stepsForRun.size(); k++) {
				sumSteps = sumSteps + stepsForRun[k];
				//std::cout << stepsForRun[k] << " ";
			}
			std::cout << std::endl;
				for (int k=0; k<stepsForRun.size(); k++) {
				//std::cout << timeForRun[k] << " ";
				sumTime = sumTime + timeForRun[k];
			}
			int averageTime = sumTime/timeForRun.size();
			int averageSteps = sumSteps/stepsForRun.size();
			std::cout<< "\nFor algorithm " << argv[3] << " and number of queens " << N;
			std::cout << "\nAverage Time: " 
			<< averageTime << "\nAverage Steps: " << averageSteps <<std::endl;
}
		
	}
	/*int sumSteps = 0;
	int sumTime = 0;
	for (int k=0; k<stepsForRun.size(); k++) {
		sumSteps = sumSteps + stepsForRun[k];
		std::cout << stepsForRun[k] << " ";
	}
	std::cout << std::endl;
		for (int k=0; k<stepsForRun.size(); k++) {
		std::cout << timeForRun[k] << " ";
		sumTime = sumTime + timeForRun[k];
	}
	int averageTime = sumTime/timeForRun.size();
	int averageSteps = sumSteps/stepsForRun.size();
	std::cout << "\n\nAt the end of ten runs the averages are for time " 
	<< averageTime << " and for steps " << averageSteps <<std::endl;*/

	return 0;
}