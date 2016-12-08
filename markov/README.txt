Markov Decision Processes
Artificial Intelligence - CSCI2400
Clara Hunnewell
Konstantine Mushegian

Description:
The files in this folder contain the implementation of Markov Decision
Processes on top of the code provided by Professor Majercik.

Files:
ValueIterationMDP.java
Makefile

Usage:
To compile the program: make
To run the program: make run
To delete .class file: make clean

Program can also be run by compiling manually, using javac.

The parameters to the program can be specified in the makefile or when c
compiling manually in the following order:

[DiscountFactor] [MaxError] [KeyLossProb] [positiveTermReward] 
[negativeTermReward] [stepCost]