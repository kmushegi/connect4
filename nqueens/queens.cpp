#include <vector>
#include <cstdlib>
#include <iostream>

//using namespace std;
typedef std::vector<std::vector<int>> board2D;

#define N 8
#define STEPS 500
#define GREEDY 1


struct Info {
	int conflicts;
	int row;
	int col;
};

board2D initializeBoard(int n) {
	board2D chessBoard;
	for(int i=0; i<n; i++) {
		std::vector<int> row = {0};
		row.resize(n);
		chessBoard.push_back(row);
	}
	return chessBoard;
}

void printBoard(board2D b) {
	for(int r=0; r<b.size(); r++) {
		for(int c=0; c<b[0].size(); c++) {
			std::cout<<b[r][c]<<" ";
		}
		std::cout<<std::endl;
	}
	std::cout<<std::endl;
}

//
// numberOfConflicts counts the number of conflicts that a queen in row r and 
// column c has on board b.
// return int - the number of conflicts
//
int numberOfConflicts(board2D b, int r, int c) {
	
	int numConflicts = 0;

	for(int i=0; i<N; i++) { // iterate through board
		for(int j=0; j<N; j++) {

			if(((i == r) || (j == c) || (abs(i-r) == abs(j-c))) && b[i][j] == 1) {
				if (i==r && j==c) { 
					continue; // don't count itself as a conflict
				}
				numConflicts++;

				//std::cout<< "conflict found at: (" <<i<<","<<j<<"(" << std::endl;
			}
		}
	}
	// std::cout << "returning " << numConflicts << std::endl;
	return numConflicts;
}

// do error parameter checking
//if there is a conflict return queen row, else return -1
std::vector<int> isSolution(board2D &b, std::vector<int> queenLocations) {
	std::vector<int> conflictingVariables;
	//std::cout<< "printing current board" << std::endl;
	//printBoard(b);
	for(int r = 0; r < N; r++) {
		int c = queenLocations[r];
		int conflicts = numberOfConflicts(b,r,c);
		// std::cout<< "number of conflicts: " << conflicts << std::endl;
		if(conflicts != 0) {
			conflictingVariables.push_back(r);
		}
	}
	// std::cout << "vector conflicts " << std::endl;
	//for (std::vector<int>::const_iterator i = conflictingVariables.begin(); i != conflictingVariables.end(); ++i)
    //std::cout << *i << ' ';

	return conflictingVariables;
}

board2D minConflictsGreedy(board2D b, std::vector<int> queenLocations, int maxSteps) {
	std::cout << "MinConflicts. Greedy. STEPS = "<< maxSteps << std::endl;
	board2D current = b;

	for(int i=0; i<maxSteps; i++) {

		std::vector<int> conflictingVars = isSolution(current, queenLocations);
		if(conflictingVars.size() == 0) {
			//std::cout<<"FOUND SOLUTION\n";
			std::cout<<"FOUND SOLUTION in steps = \n" << i <<std::endl;
			return current;
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
				current[r][queenLocations[r]]=0; // set current spot to 0
				//std::cout<<"currently working on " <<std::endl;
				//printBoard(current);
				//std::cout<< std::endl;

				for(int c = 0; c < queenLocations.size(); c++) {
					// set current spot to 0
					//current[r][c]=0;
					//std::cout << "testing " << r << " " << c <<std::endl;
					int tmp = numberOfConflicts(current,r,c);

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
				current[r][queenLocations[r]] = 1; // return the queen to its current location
			}
			/*std::cout << std::endl << "hello these are the possibilities" << std::endl;
			for (int q = 0; q<currentMins.size(); q++) {
				std::cout<< currentMins[q].row << " and " << currentMins[q].col << " " << std::endl;
			}*/
			int tieBreaker = rand() % currentMins.size();
			int queenToMove = currentMins[tieBreaker].row;
			current[queenToMove][queenLocations[queenToMove]] = 0; 
			//std::cout << "move to make " << currentMins[tieBreaker].row << " " << currentMins[tieBreaker].col <<std::endl;
			//printBoard(current);
			//std::cout << std::endl;
			queenLocations[queenToMove] = currentMins[tieBreaker].col;
			current[queenToMove][currentMins[tieBreaker].col] = 1;
			//std::cout<< "move made" << std::endl;
			//printBoard(current);
		}
	}
	return current;
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



board2D minConflictsRandom(board2D b, std::vector<int> queenLocations, int maxSteps) {
	std::cout << "MinConflicts. STEPS = "<< maxSteps << std::endl;
	board2D current = b;

	for(int i=0; i<maxSteps; i++) {

		std::vector<int> conflictingVars = isSolution(current, queenLocations);
		if(conflictingVars.size() == 0) {
			std::cout<<"FOUND SOLUTION in steps = \n" << i <<std::endl;
			return current;
		} else {
			std::vector<int> currentMinIndices;
			int randomConflictingVarIndex = rand() % conflictingVars.size();
			int randomConflictingVar = conflictingVars[randomConflictingVarIndex];
			// std::cout << "lets try to change " << randomConflictingVar <<std::endl;
			int currentMin = INT_MAX;
			// int currentMinIndex = INT_MAX;
			current[randomConflictingVar][queenLocations[randomConflictingVar]] = 0; // set equal to 0
			//TODO: randomly choose between tied queen positions
			for(int j=0; j<N; j++) {
				int t = numberOfConflicts(current,randomConflictingVar,j);
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
			int tieBreaker = rand() % currentMinIndices.size();
			// std::cout << "move to make " << currentMinIndices[tieBreaker] <<std::endl;
			//current[randomConflictingVar][queenLocations[randomConflictingVar]] = 0;
			queenLocations[randomConflictingVar] = currentMinIndices[tieBreaker] ;
			current[randomConflictingVar][currentMinIndices[tieBreaker]] = 1;
			//printBoard(current);
		}
	}
	return current;
}

board2D randomOrMinConflicts(board2D b, std::vector<int> queenLocations, int maxSteps) {
	std::cout<<"Random Or MinConficts. STEPS = "<<maxSteps<<std::endl;

	board2D current = b;

	for(int i=0; i<maxSteps; i++) { 
		// search for queens with conflicts
		std::vector<int> conflictingVars = isSolution(current,queenLocations);
		if(conflictingVars.empty()) { // if there are none
			return current; // solution has been found
		} else {
			std::vector<int> currentMinIndices;
			int randomConflictingVarIndex = rand() % conflictingVars.size();
			int randomConflictingVar = conflictingVars[randomConflictingVarIndex];

			int magicNumber = rand() % 10;
			if(magicNumber <= 3) {
				int randomColumnForQueen = rand() % current.size();

				current[randomConflictingVar][queenLocations[randomConflictingVar]] = 0;
				current[randomConflictingVar][randomColumnForQueen] = 1;
				queenLocations[randomConflictingVar] = randomColumnForQueen;
			} else {
				
			}	
		}
	}

}

//
// Takes an initialized board of zeroes and places queens on it randomly 
// board2D is the board on which the queens are to be placed
// int n - the number of queens to be placed
// return vector<int> - the vector of queen locations once they are placed
// 
std::vector<int> placeQueensRandom(board2D &b, int n) {
	std::vector<int> queenLocations;
	
	// iterate through the queens
	for(int r=0; r<n; r++) {
		int c = rand()%n; // choose a random column to put it in
		b[r][c] = 1;
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

std::vector<int> placeQueensSmartStart(board2D &b, int n) {

	std::vector<int> queenLocations; // the queen locations 
	int currentBest = INT_MAX; 
	int currentBestIndex = -1;
	int conflicts;

	// iterate through the board
	for(int r=0; r<n; r++) {
		for (int c = 0; c<n; c++) {

			// if you have found a location with less conflicts, update
			// your current best
			conflicts = numberOfConflicts(b,r,c);
			if (conflicts<currentBest) {
				currentBest = conflicts;
				currentBestIndex = c;
			}
		}
		// Place the queen in the place with the least conflicts
		b[r][currentBestIndex] = 1;
		queenLocations.push_back(currentBestIndex); // save the location
		currentBest = INT_MAX; // reset to move on to next queen
		currentBestIndex = -1;
	}
	std::cout << "this is our board placed smartly" <<std::endl;
	printBoard(b);
	return queenLocations;
}


int main() {
	srand(time(NULL));
	board2D b = initializeBoard(N);
	std::vector<int> initialSeed = placeQueensRandom(b,N);
	printBoard(b);
	board2D c = randomOrMinConflicts(b,initialSeed,STEPS);
	printBoard(c);
	return 0;
}