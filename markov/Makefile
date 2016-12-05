JC = javac
JV = java

MDP = ValueIterationMDP.java

default: 
	$(JC) $(MDP)

run:
	@$(JV) ValueIterationMDP 0.99 1e-6 0.5 1 -1 -0.04

clean:
	rm *.class