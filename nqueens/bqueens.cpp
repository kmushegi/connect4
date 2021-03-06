#include <vector>
#include <cstdlib>
#include <iostream>
#include <time.h>

//using namespace std;
//typedef std::vector<std::vector<int>> board2D;

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

int N; 		// the number of queens for the board
int STEPS;  // the number of steps to take before giving up

bool firstBetter = false;

/*board2D initializeBoard(int n) {
	board2D chessBoard;
	for(int i=0; i<n; i++) {
		std::vector<int> row = {0};
		row.resize(n);
		chessBoard.push_back(row);
	}
	return chessBoard;
}*/

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
}


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
			if (queenLocations[i] == c || (abs(i-r) == abs(queenLocations[i]-c))) {
				numConflicts++;
				//std::cout<< "conflict found at: (" <<i<<","<<queenLocations[i]<<"(" << std::endl;
			}
		}
	}
	//std::cout << "returning " << numConflicts << std::endl;
	return numConflicts;
}

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
	
		// std::cout<< "number of conflicts: " << conflicts << std::endl;
		if(numberOfConflicts(queenLocations,r,c) != 0) {
			conflictingVariables.push_back(r);
		}
	}
	//std::cout << "vector conflicts " << std::endl;
	/*
	for (std::vector<int>::const_iterator i = conflictingVariables.begin(); i != conflictingVariables.end(); ++i)
    std::cout << *i << ' ';*/

	return conflictingVariables;
}

std::vector<int> minConflictsGreedy(std::vector<int> queenLocations, int maxSteps) {
	std::cout << "MinConflicts. Greedy. STEPS = "<< maxSteps << std::endl;
	//board2D current = b;

	for(int i=0; i<maxSteps; i++) {

		std::vector<int> conflictingVars = isSolution(queenLocations);
		if(conflictingVars.size() == 0) {
			//std::cout<<"FOUND SOLUTION\n";
			std::cout<<"FOUND SOLUTION in steps = \n" << i <<std::endl;
			return queenLocations;
		} else {
			std::vector<Info> currentMins;
			Info info;
			info.conflicts = INT_MAX;
			info.row = -1;
			info.col = -1;
			//currentMins.push_back(info);
			//std::cout << "LOOK HERE" << currentMins[0].row << std::endl;
			/*std::cout << "hello these are the conflicting vars" << std::endl;
			for (int q = 0; q<conflictingVars.size(); q++) {
				std::cout<< conflictingVars[q] << " " ;
			}
			std::cout << std::endl << "hello these are the queen locations" << std::endl;
			for (int q = 0; q<queenLocations.size(); q++) {
				std::cout<< queenLocations[q] << " " << std::endl;
			}*/

			for(int m=0; m < conflictingVars.size(); m++) {
				int r = conflictingVars[m];
				//current[r][queenLocations[r]]=0; // set current spot to 0
				//std::cout<<"currently working on " <<std::endl;
				//printBoard(current);
				//std::cout<< std::endl;

				for(int c = 0; c < queenLocations.size(); c++) {
					// set current spot to 0
					//current[r][c]=0;
					//std::cout << "testing " << r << " " << c <<std::endl;
					int tmp = numberOfConflicts(queenLocations,r,c);

					if(tmp < info.conflicts) {
						//std::cout << "updating current min to " << tmp <<std::endl;
						//std::cout << "updating current queen to " << r  << " and column " << c<<std::endl;
						info.conflicts = tmp;
						info.row = r;
						info.col = c;
					if (currentMins.empty() == false){
						 //std::cout<<"clearing"<<std::endl;
						currentMins.clear();
					}
					currentMins.push_back(info);

					}
					else if (tmp == info.conflicts){
						//std::cout << "new min found" << std::endl;
						info.row = r;
						info.col = c;
						currentMins.push_back(info);
					}

				}
			}
			/*std::cout << std::endl << "hello these are the possibilities" << std::endl;
			for (int q = 0; q<currentMins.size(); q++) {
				std::cout<< currentMins[q].row << " and " << currentMins[q].col << " " << std::endl;
			}*/
				
			int tieBreaker = rand() % currentMins.size();
			int queenToMove = currentMins[tieBreaker].row;
			//std::cout << "move to make " << currentMins[tieBreaker].row << " " << currentMins[tieBreaker].col <<std::endl;
			//printBoard(current);
			//std::cout << std::endl;
			queenLocations[queenToMove] = currentMins[tieBreaker].col;
			//std::cout<< "move made" << std::endl;
			//printBoard(current);
		}
	}
	return queenLocations;
}

/*
void firstBetter (board2D b, std::vector<int> queenLocations, std::std::vector<int> conflictingVars) {
	if (queenLocations.empty()) {
		std::cout << "Error."  << std::endl;
		return; 
	}
	std::vector<int> currentMinIndices;
	int randomConflictingVarIndex = rand() % conflictingVars.size();
	int randomConflictingVar = conflictingVars[randomConflictingVarIndex];
	int conflicts = numberOfConflicts(b, randomConflictingVar, queenLocations[randomConflictingVar]);

	// set equal to zero so doesn't count itself
	board2D[randomConflictingVar][queenLocations[randomConflictingVar]] = 0;
	for (int c=0; c<queenLocations.size(); c++) {
		// if you have found one that's better
		if (numberOfConflicts(b, randomConflictingVar, c) < conflicts) {
			board2D[randomConflictingVar][c] = 1;

		}
	}
}*/



std::vector<int> minConflictsRandom(std::vector<int> queenLocations, int maxSteps) {
	std::cout << "MinConflicts. STEPS = "<< maxSteps << std::endl;
	//board2D current = b;

	for(int i=0; i<maxSteps; i++) {

		std::vector<int> conflictingVars = isSolution(queenLocations);
		if(conflictingVars.size() == 0) {
			std::cout<<"FOUND SOLUTION in steps = \n" << i <<std::endl;
			return queenLocations;
		} else {
			std::vector<int> currentMinIndices;
			int randomConflictingVarIndex = rand() % conflictingVars.size();
			int randomConflictingVar = conflictingVars[randomConflictingVarIndex];

			int initialConflicts; 
			if (firstBetter) {
				initialConflicts = numberOfConflicts(queenLocations, randomConflictingVar, queenLocations[randomConflictingVar]);
			}
			// std::cout << "lets try to change " << randomConflictingVar <<std::endl;
			int currentMin = INT_MAX;
			// int currentMinIndex = INT_MAX;
			//current[randomConflictingVar][queenLocations[randomConflictingVar]] = 0; // set equal to 0
			//TODO: randomly choose between tied queen positions
			bool moveOn = false;
			for(int j=0; j<N; j++) {
				int t = numberOfConflicts(queenLocations, randomConflictingVar,j);
				if (firstBetter && t<initialConflicts) {
					queenLocations[randomConflictingVar] = j;
					moveOn = true;
				}
				if(t < currentMin) {
					// std::cout << "updating current min " <<std::endl;
					currentMin = t;
					if (currentMinIndices.empty() == false){
						// std::cout<<"clearing"<<std::endl;
						currentMinIndices.clear();
					}
					currentMinIndices.push_back(j);
					//currentMinIndex = j;
				}
				else if (t==currentMin) {
					currentMinIndices.push_back(j);
				}
			}
			if (!moveOn){
				int tieBreaker = rand() % currentMinIndices.size();
				// std::cout << "move to make " << currentMinIndices[tieBreaker] <<std::endl;
				//current[randomConflictingVar][queenLocations[randomConflictingVar]] = 0;
				queenLocations[randomConflictingVar] = currentMinIndices[tieBreaker] ;
				//current[randomConflictingVar][currentMinIndices[tieBreaker]] = 1;
				//printBoard(current);
			}
		}
	}
	return queenLocations;
}

std::vector<int> randomOrMinConflicts(std::vector<int> queenLocations, int maxSteps) {
	std::cout<<"Random Or MinConficts. STEPS = "<<maxSteps<<std::endl;

	for(int i=0; i<maxSteps; i++) { 
		// search for queens with conflicts
		std::vector<int> conflictingVars = isSolution(queenLocations);
		if(conflictingVars.empty()) { // if there are none
				std::cout<<"FOUND SOLUTION in steps = \n" << i <<std::endl;
			return queenLocations; // solution has been found
		} else {
			std::vector<int> currentMinIndices;
			int randomConflictingVarIndex = rand() % conflictingVars.size();
			int randomConflictingVar = conflictingVars[randomConflictingVarIndex];

			int magicNumber = rand() % 10;
			if(magicNumber <= 3) {
				int randomColumnForQueen = rand() % queenLocations.size();

				// current[randomConflictingVar][queenLocations[randomConflictingVar]] = 0;
				// current[randomConflictingVar][randomColumnForQueen] = 1;
				queenLocations[randomConflictingVar] = randomColumnForQueen;
			} else {
				std::vector<int> currentMinIndices;
				int currentMin = INT_MAX;
				// currentMinIndices[randomConflictingVar][queenLocations[randomConflictingVar]] = 0;

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
				// current[randomConflictingVar][currentMinIndices[tieBreaker]] = 1;
			}	
		}
	}
	return queenLocations;
}

//
// Takes an initialized board of zeroes and places queens on it randomly 
// board2D is the board on which the queens are to be placed
// int n - the number of queens to be placed
// return vector<int> - the vector of queen locations once they are placed
// 
std::vector<int> placeQueensRandom(int n) {
	std::vector<int> queenLocations;
	
	// iterate through the queens
	for(int r=0; r<n; r++) {
		int c = rand()%n; // choose a random column to put it in
		//b[r][c] = 1;
		queenLocations.push_back(c);
	}
		/*
	b[0][0] = 1;
	b[1][3] = 1;
	b[2][3] = 1;
	b[3][2] = 1;
	randomNumbers.push_back(0);
	randomNumbers.push_back(3);
	randomNumbers.push_back(3);
	randomNumbers.push_back(2);*/
	
	return queenLocations;
}


// needs to be updated to reflect randomness??

//
// Takes an initialized board of zeroes and places queens so as to minimize
// the conflicts between the queen to be placed and all of the queens already
// placed on the board.  
// board2D is the board on which the queens are to be placed
// int n - the number of queens to be placed
// return vector<int> - the vector of queen locations once they are placed
// 

std::vector<int> placeQueensSmartStart(int n) {

	std::vector<int> queenLocations; // the queen locations 
	int currentBest = INT_MAX; 
	int currentBestIndex = -1;
	int conflicts;

	// iterate through the board
	for(int r=0; r<n; r++) {
		for (int c = 0; c<n; c++) {

			// if you have found a location with less conflicts, update
			// your current best
			conflicts = numberOfConflicts(queenLocations,r,c);
			if (conflicts<currentBest) {
				currentBest = conflicts;
				currentBestIndex = c;
			}
		}
		// Place the queen in the place with the least conflicts
		//b[r][currentBestIndex] = 1;
		queenLocations.push_back(currentBestIndex); // save the location
		currentBest = INT_MAX; // reset to move on to next queen
		currentBestIndex = -1;
	}
	std::cout << "this is our board placed smartly" <<std::endl;
	//printBoard(queenLocations);
	return queenLocations;
}


int main(int argc, char* argv[]) {
	if(argc < 2 || argc > 4) {
		std::cout<<"Incorrect Usage. Use -help for usage info\n";
		exit(1);
	} else if(argc == 2) {
		if(strcmp(argv[1],"-help") == 0) {
			std::cout<<"Usage: ./aqueens NUM_STEPS NUM_QUEENS ALGORITHM-NAME / START STRATEGY\n";
			std::cout<<"ALGORITHM-NAME / START STRATEGY: BASIC/GREEDY/RANDOM/SMART-START/FIRST-BETTER\n";
		}
	} else if(argc == 4) {
		srand(time(NULL));

		STEPS = atoi(argv[1]);
		N = atoi(argv[2]);

		std::vector<int> initialSeed = placeQueensRandom(N);
		//= placeQueensSmartStart(N);
	//printBoard(initialSeed);

	clock_t t1, t2;
	t1 = clock();

		if(strcmp(argv[3],"BASIC") == 0) {
			std::cout<<"RUNNING BASIC\n";
			//run basic
			//std::vector<int> initialSeed = placeQueensRandom(N);

			initialSeed = minConflictsRandom(initialSeed, STEPS);
			


		} else if(strcmp(argv[3],"GREEDY") == 0) {
			std::cout<<"RUNNING GREEDY\n";
			//std::vector<int> initialSeed = placeQueensRandom(N);
			t1=clock();
			minConflictsGreedy(initialSeed, STEPS);
			t2=clock();

			std::cout<<"RUN TIME: " << (t2-t1) << "\n";
			//run greedy
		} else if(strcmp(argv[3],"RANDOM") == 0) {
			std::cout<<"RUNNING RANDOM\n";
			//std::vector<int> initialSeed = placeQueensRandom(N);
			t1=clock();
			randomOrMinConflicts(initialSeed, STEPS);
			t2=clock();

			std::cout<<"RUN TIME: " << (t2-t1) << "\n";
			//run random
		} else if(strcmp(argv[3],"SMART-START") == 0) {
			//std::cout<<"RUNNING SMART-START\n";
			//std::vector<int> initialSeed = placeQuee(N);

		} else if(strcmp(argv[3],"FIRST-BETTER") == 0) {
			firstBetter = true;
			std::cout<<"RUNNING FIRST-BETTER\n";
			initialSeed = minConflictsRandom(initialSeed, STEPS);

			//run first better with relevant method
		}
		

		isSolution(initialSeed);
		printBoard(initialSeed);
	}

	return 0;
}