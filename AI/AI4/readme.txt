INTRODUCTION
------------
The problem is a basic version of a mario like game in which the possible actions in each stage are Top, Bottom, Jump and 
Reverse Jump. There are different reward points in each state which can be visualized as a pit, an opponent or gold points, 
etc.
DESIGN
--------------
To create the MDP each state is numbered from 1 to 10. Note that the number of states can be easily increased but have been
kept at 10 for clear understanding. The rewards range from -2 to 8 and are generated using a random function. The reward of the final state is equal to 20.  The start state is 0. Reverse or bottom at this stage results in no action and the state remains the same. Similarly at the end state the same thing happens. The jump and reverse jump behave differently in
the penultimate states as well as expected. 

IMPLEMENTATION
--------------
The mdp is created by first making a string and then writing in the file. The MDP follows the same format as given in the assignment description. The corner states are encoded seperately as they have different transitions for the actions. The rest
of the states are encoded iteratively. The iteration is done for each state and different actions are encoded differently.
After the file is created we are using the same version of value and policy iteration as created for the cat mouse example.
The output is giving the policies for each state using value and policy iteration and also the utlitiy of each state in both 
cases.