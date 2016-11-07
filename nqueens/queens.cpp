#include <vector>
#include <cstdlib>
#include <iostream>

typedef std::vector<std::vector<int>> board2D;

board2D chessBoard;

board2D generateBoard(int n) {
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

int main() {
	board2D b = generateBoard(10);
	printBoard(b);
	return 0;
}