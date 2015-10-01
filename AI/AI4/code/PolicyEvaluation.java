/**
 * This is the template of a class that evaluates a given policy,
 * i.e., computes the utility of each state when actions are chosen
 * according to it.  The utility is returned in the public
 * <tt>utility</tt> field.  You need to fill in the constructor.  You
 * may wish to add other fields with other useful information that you
 * want this class to return (for instance, number of iterations
 * before convergence).  You also may add other constructors or
 * methods, provided that these are in addition to the one given below
 * (which is the one that will be automatically tested).  In
 * particular, your code must work properly when run with the
 * <tt>main</tt> provided in <tt>RunCatMouse.java</tt>.
 */
public class PolicyEvaluation {

    /** the computed utility of each state under the given policy */
    public double utility[];

    /** New Parameters **/
	public double maxError = 1/10^8;
    public double utilityPrime[];

    /**
     * The constructor for this class.  Computes the utility of policy
     * <tt>pi</tt> for the given <tt>mdp</tt> with given
     * <tt>discount</tt> factor, and stores the answer in
     * <tt>utility</tt>.
     */
    public PolicyEvaluation(Mdp mdp, double discount, int pi[]) {

	// your code here

	utility = new double[mdp.numStates];
			double utilityPrime[] = utility;
			double delta = 0;

			do{
				utility = utilityPrime;
				delta = 0;

				for(int s = 0; s < mdp.numStates; s++){
					double maxA = Double.NEGATIVE_INFINITY;

					double sumEU = 0;

					for (int sPrime=0; sPrime < mdp.nextState[s][pi[s]].length; sPrime++){
						sumEU += mdp.transProb[s][pi[s]][sPrime] * mdp.reward[mdp.nextState[s][pi[s]][sPrime]];
					}

					utilityPrime[s] = mdp.reward[s]+(discount*sumEU);

					if(Math.abs(utilityPrime[s] - utility[s]) > delta)
						delta = Math.abs(utilityPrime[s] - utility[s]);
				}

		}while(delta >= maxError*(1-discount)/discount );

    }

}
