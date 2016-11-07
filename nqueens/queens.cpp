#include <vector>
#include <cstdlib>
#include <iostream>

typedef std::vector<std::vector<int>> board2D;

#define N 4
#define STEPS 500

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

int numberOfConflicts(board2D &b, int r, int c) {
	int numConflicts = 0;
	// for(int i=0; i<N; i++) {
	// 	if(i == c) {
	// 		continue;
	// 	}
	// 	if(b[i] == r || abs(i-c) == abs(b[i] - r)) {
	// 		numConflicts++;
	// 	}
	// }
	// return numConflicts;
	for(int i=0; i<N; i++) {
		for(int j=0; j<N; j++) {
			if(((i == r) || (j == c) || (abs(i-r) == abs(j-c))) && b[i][j] == 1) {
				numConflicts++;
			}
		}
	}
	return numConflicts;
}

//if there is a conflict return queen row, else return -1
std::vector<int> isSolution(board2D &b, std::vector<int> queenLocations) {
	std::vector<int> conflictingVariables;

	for(int r = 0; r < N; r++) {
		int c = queenLocations[r];
		int conflicts = numberOfConflicts(b,r,c);
		if(conflicts != 0) {
			conflictingVariables.push_back(r);
		}
	}
	return conflictingVariables;
}

board2D minConflicts(board2D b, std::vector<int> queenLocations, int maxSteps = 500) {
	
	board2D current = b;
	for(int i=0; i<maxSteps; i++) {

		std::vector<int> conflictingVars = isSolution(current, queenLocations);
		if(conflictingVars.size() == 0) {
			std::cout<<"FOUND SOLUTION\n";
			return current;
		} else {
			int randomConflictingVar = rand() % conflictingVars.size();
			int currentMin = numberOfConflicts(b,randomConflictingVar,queenLocations[randomConflictingVar]); //was INT_MAX

			//TODO: randomly choose between tied queen positions
			for(int j=0; j<N; j++) {
				int t = numberOfConflicts(b,randomConflictingVar,j);
				if(t < currentMin) {
					currentMin = t;
				}
			}
			current[randomConflictingVar][queenLocations[randomConflictingVar]] = 0;
			queenLocations[randomConflictingVar] = currentMin;
			current[randomConflictingVar][currentMin] = 1;
			//printBoard(current);
		}
	}
	return current;
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
	board2D c = minConflicts(b,initialSeed,STEPS);
	printBoard(c);
	return 0;
}