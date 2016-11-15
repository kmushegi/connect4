Grace Handler, Clara Hunnewell, Kote Mushegian
Professor Majercik
Artificial Intelligence 
November 14, 2016

In the nqueens folder, our code is in aqueens.cpp and the executable is aqueens.

To run via Terminal, navigate to the nqueens folder, then in the command line type:

./aqueens NUM_STEPS NUM_QUEENS ALGO_TYPE

where:
—-> NUM_STEPS is the maximum number of steps allowed
—-> NUM_QUEENS is the problem size, or number of queens n, also size of board nxn
—-> ALGO_TYPE is one of the following five: BASIC, GREEDY, RANDOM, SMART-START*, FIRST-BETTER*

*Note: SMART-START and FIRST-BETTER are variations on the BASIC algorithm.

The output will print the initial board, the final board, whether or not a solution was found, and if so, the run time and number of steps it took to find the solution.

In the aqueens.cpp file, here are the line numbers where the respective variations can be found:
1) BASIC: line 216 @ minConflictsRandom()**
2) GREEDY: line 135 @ minConflictsGreedy()**
3) RANDOM: line 216 @ randomOrMinConflicts()**
4) SMART-START: [running basic algorithm but board initialized at line 367 placeQueensSmartStart()]
5) FIRST-BETTER: Boolean variable firstBetter set based on command line input and used at line 244 & line 257 in minConflictsRandom() function**

**All algorithms besides smart-start use line 343 placeQueensRandom() to initialize board.

NOTE: In the main() function beginning at line 403, user input is taken and the aforementioned functions are called accordingly.