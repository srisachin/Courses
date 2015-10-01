/**
 * This is the template of a class that should run value iteration on
 * a given MDP to compute the optimal policy which is returned in the
 * public <tt>policy</tt> field.  The computed optimal utility is also
 * returned in the public <tt>utility</tt> field.  You need to fill in
 * the constructor.  You may wish to add other fields with other
 * useful information that you want this class to return (for
 * instance, number of iterations before convergence).  You also may
 * add other constructors or methods, provided that these are in
 * addition to the one given below (which is the one that will be
 * automatically tested).  In particular, your code must work properly
 * when run with the <tt>main</tt> provided in <tt>RunCatMouse.java</tt>.
 */
public class ValueIteration {

    /** the computed optimal policy for the given MDP **/
    public int policy[];

    /** the computed optimal utility for the given MDP **/
    public double utility[];

    /** New Parameters **/
    public double maxError = 1/10^8;
    public double utilityPrime[];

    /**
     * The constructor for this class.  Computes the optimal policy
     * for the given <tt>mdp</tt> with given <tt>discount</tt> factor,
     * and stores the answer in <tt>policy</tt>.  Also stores the
     * optimal utility in <tt>utility</tt>.
     */
    public ValueIteration(Mdp mdp, double discount) {

	// your code here
		policy = new int[mdp.numStates];
		utility = new double[mdp.numStates];
		double utilityPrime[] = utility;
		double delta = 0;

		do{
			utility = utilityPrime;
			delta = 0;

			for(int s = 0; s < mdp.numStates; s++){
				double maxA = Double.NEGATIVE_INFINITY;

				int currMaxA = policy[s];
				for (int a = 0; a < mdp.numActions; a++){
					double tempSum = 0;

					for (int sPrime=0; sPrime < mdp.nextState[s][a].length; sPrime++){
						tempSum += mdp.transProb[s][a][sPrime] * mdp.reward[mdp.nextState[s][a][sPrime]];
					}
					if(tempSum > maxA){
						maxA = tempSum;
						currMaxA = a;
					}
				}
				policy[s] = currMaxA;
				utilityPrime[s] = mdp.reward[s]+(discount*maxA);

				if(Math.abs(utilityPrime[s] - utility[s]) > delta)
					delta = Math.abs(utilityPrime[s] - utility[s]);
			}

		}while(delta >= maxError*(1-discount)/discount );

    }

}
