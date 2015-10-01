import java.util.Random;
/**
 * This is the template of a class that should run policy iteration on
 * a given MDP to compute the optimal policy which is returned in the
 * public <tt>policy</tt> field.  You need to fill in the constructor.
 * You may wish to add other fields with other useful information that
 * you want this class to return (for instance, number of iterations
 * before convergence).  You also may add other constructors or
 * methods, provided that these are in addition to the one given below
 * (which is the one that will be automatically tested).  In
 * particular, your code must work properly when run with the
 * <tt>main</tt> provided in <tt>RunCatMouse.java</tt>.
 */
public class PolicyIteration {

    /** the computed optimal policy for the given MDP **/
    public int policy[];

    /** New Parameters **/
    public double utility[];

    /**
     * The constructor for this class.  Computes the optimal policy
     * for the given <tt>mdp</tt> with given <tt>discount</tt> factor,
     * and stores the answer in <tt>policy</tt>.
     */
    public PolicyIteration(Mdp mdp, double discount) {

	// your code here
	utility = new double[mdp.numStates];
	policy = new int[mdp.numStates];
	Random randomAction = new Random();
	for(int i=0; i<mdp.numStates; i++){
		policy[i] = randomAction.nextInt(mdp.numActions);
	}
	boolean unchanged = true;
	do{
		utility = new PolicyEvaluation(mdp,discount,policy).utility;
		unchanged = true;

		for(int s=0; s<mdp.numStates ; s++){
			double maxA = Double.NEGATIVE_INFINITY;
			double policyVal = 0;

			int currMaxA = policy[s];
			for (int a = 0; a < mdp.numActions; a++){
				double tempSum = 0;

				for (int sPrime=0; sPrime < mdp.nextState[s][a].length; sPrime++){
					double t1 = mdp.transProb[s][a][sPrime];
					t1 = utility[mdp.nextState[s][a][sPrime]];
					tempSum += mdp.transProb[s][a][sPrime] * utility[mdp.nextState[s][a][sPrime]];
				}
				if(tempSum > maxA){
					maxA = tempSum;
					currMaxA = a;
				}
				if(a == policy[s]){
					policyVal = tempSum;
				}

			}

			if(maxA > policyVal){
				policy[s] = currMaxA;
				unchanged = false;
			}


		}
	}while(!unchanged);

    }

}
