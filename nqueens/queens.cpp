#include <vector>
#include <cstdlib>
#include <iostream>

//using namespace std;
typedef std::vector<std::vector<int>> board2D;

#define N 4
#define STEPS 1
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

int numberOfConflicts(board2D b, int r, int c) {
	//b[r][c] = 0; //	NOT SURE ABOUT THIS
	int numConflicts = 0;

	for(int i=0; i<N; i++) {
		for(int j=0; j<N; j++) {

			if(((i == r) || (j == c) || (abs(i-r) == abs(j-c))) && b[i][j] == 1) {
				if (i==r && j==c) {
					continue;
				}
				numConflicts++;

				//std::cout<< "conflict found at: (" <<i<<","<<j<<"(" << std::endl;
			}
		}
	}
	// std::cout << "returning " << numConflicts << std::endl;
	return numConflicts;
}

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
			std::cout<<"FOUND SOLUTION\n";
			return current;
		} else {
			std::vector<Info> currentMins;

			Info info;
			info.conflicts = INT_MAX;
			info.row = -1;
			info.col = -1;
			std::cout << "hello" << std::endl;
			for(int r=0; r < conflictingVars.size(); r++) {

				current[r][queenLocations[r]]=0; // set current spot to 0

				for(int c = 0; c < queenLocations.size(); c++) {
					// set current spot to 0
					current[r][c]=0;
					std::cout << "testing " << r << " " << c <<std::endl;
					int tmp = numberOfConflicts(current,r,c);

					if(tmp < info.conflicts) {
						std::cout << "updating current min to " << tmp <<std::endl;
						std::cout << "updating current queen to " << r  << " and column " << c<<std::endl;
						info.conflicts = tmp;
						info.row = r;
						info.col = c;
					if (currentMins.empty() == false){
						 std::cout<<"clearing"<<std::endl;
						currentMins.clear();
					}
					currentMins.push_back(info);

					}
					else if (tmp == info.conflicts){
						std::cout << "new min found" << std::endl;
						currentMins.push_back(info);
					}
				}
				current[r][queenLocations[r]] = 1; // return the queen to its current location
			}
			int tieBreaker = rand() % currentMins.size();
			int queenToMove = currentMins[tieBreaker].row;
			current[queenToMove][queenLocations[queenToMove]] = 0; 
			std::cout << "move to make " << currentMins[tieBreaker].row <<std::endl;
			printBoard(current);
			std::cout << std::endl;
			queenLocations[queenToMove] = currentMins[tieBreaker].col;
			current[queenToMove][currentMins[tieBreaker].col] = 1;
			//printBoard(current);
		}
	}
	return current;
}

board2D minConflictsRandom(board2D b, std::vector<int> queenLocations, int maxSteps) {
	std::cout << "MinConflicts. STEPS = "<< maxSteps << std::endl;
	board2D current = b;

	for(int i=0; i<maxSteps; i++) {

		std::vector<int> conflictingVars = isSolution(current, queenLocations);
		if(conflictingVars.size() == 0) {
			// std::cout<<"FOUND SOLUTION\n";
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
		std::vector<int> conflictingVars = isSolution(current,queenLocations);
		if(conflictingVars.empty()) {
			return current;
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

std::vector<int> placeQueensRandom(board2D &b, int n) {
	std::vector<int> randomNumbers;

	for(int i=0; i<n; i++) {
		int r = rand()%n;
		b[i][r] = 1;
		randomNumbers.push_back(r);
	}
	return randomNumbers;
}


int main() {
	srand(time(NULL));
	board2D b = initializeBoard(N);
	std::vector<int> initialSeed = placeQueensRandom(b,N);
	printBoard(b);
	board2D c = minConflictsGreedy(b,initialSeed,STEPS);
	printBoard(c);
	return 0;
}