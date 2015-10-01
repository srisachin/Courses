import java.io.*;
import java.util.*;

/**
 * This class provides a simple main for running on cat and mouse
 * data.
 */
public class RunHPMdp {

    /** This is a simple main.  When invoked, it does the following:
     * (1) loads an MDP from a file named in the command line; (2)
     * runs value iteration and policy iteration; (3) runs policy
     * evaluation on the policy returned by policy iteration; (4)
     * prints out the optimal policy and utilities returned by both
     * value iteration and policy iteration; and (5) animates the cat
     * chasing the mouse, depending on the command-line arguments.
     * (Note that this animation will almost certainly crash if run on
     * MDP's other than those for a cat chasing a mouse.)
     *
     * <p>The command-line arguments should consist of a possible list
     * of options, followed by the name of a data file containing a
     * description of the MDP.  By default, a GUI (graphical user
     * interface) based animation will be invoked.  However, this can
     * be changed with the appropriate command-line options: Using the
     * "<tt>-b</tt> option will run the GUI while simultaneously
     * printing a transcript of all states visited.  Using the <tt>-p
     * &lt;num&gt;</tt> option will not invoke the GUI at all but will
     * instead run the MDP for <tt>&lt;num&gt;</tt> steps, while printing
     * the results.  Finally, using the <tt>-n</tt> option will
     * neither invoke the GUI nor print any results.
     *
     * <p>It is okay to change this main as you wish.  However, your
     * code should still work properly when using this one.
     */
    public static void main(String argv[])
	throws FileNotFoundException, IOException {

	double discount = 0.95;

	// parse options
	Options options = null;
	try {
	    options = new Options(argv);
	} catch (Exception e) {
	    System.out.println("Specify the arguments");
	    return;
	}

	// Create MDP

	String str= new String();

	//Start State
	str = str + 0;
	str = str + '\n';
	
	//Rewards
	str = str + 0;
	str = str + ' ';
	str = str + 0;
	str = str + '\n';
	Random ran = new Random();
	for (int i=1; i < 10; i++)
	{
		int x = ran.nextInt(10) - 2;
		str = str + i;
		str = str + ' ';
		str = str + x;
		str = str + '\n';
	}
	str = str + 10;
	str = str + ' ';
	str = str + 20;
	str = str + '\n';
	
	//State Transitions
	//State 0
	str = str + 0;
	str = str + ' ';
	str = str + 'T';
	str = str + ' ';
	str = str + 1;
	str = str + ' ';
	str = str + "0.9";
	str = str + ' ';
	str = str + 0;
	str = str + ' ';
	str = str + "0.1";
	str = str + '\n';
		
	str = str + 0;
	str = str + ' ';
	str = str + 'B';
	str = str + ' ';
	str = str + 0;
	str = str + ' ';
	str = str + "1";
	str = str + '\n';
	
	str = str + 0;
	str = str + ' ';
	str = str + 'J';
	str = str + ' ';
	str = str + 2;
	str = str + ' ';
	str = str + "0.9";
	str = str + ' ';
	str = str + 1;
	str = str + ' ';
	str = str + "0.1";
	str = str + '\n';
	
	str = str + 0;
	str = str + ' ';
	str = str + 'R';
	str = str + ' ';
	str = str + 0;
	str = str + ' ';
	str = str + "1";
	str = str + '\n';
	
	//State 1
	str = str + 1;
	str = str + ' ';
	str = str + 'T';
	str = str + ' ';
	str = str + 2;
	str = str + ' ';
	str = str + "0.9";
	str = str + ' ';
	str = str + 1;
	str = str + ' ';
	str = str + "0.1";
	str = str + '\n';
	

	str = str + 1;
	str = str + ' ';
	str = str + 'B';
	str = str + ' ';
	str = str + 0;
	str = str + ' ';
	str = str + "0.9";
	str = str + ' ';
	str = str + 1;
	str = str + ' ';
	str = str + "0.1";
	str = str + '\n';
	
	str = str + 1;
	str = str + ' ';
	str = str + 'J';
	str = str + ' ';
	str = str + 3;
	str = str + ' ';
	str = str + "0.9";
	str = str + ' ';
	str = str + 2;
	str = str + ' ';
	str = str + "0.1";
	str = str + '\n';
	
	str = str + 1;
	str = str + ' ';
	str = str + 'R';
	str = str + ' ';
	str = str + 0;
	str = str + ' ';
	str = str + "1";
	str = str + '\n';

	
	for (int i=2; i < 9; i++)
	{	
		str = str + i;
		str = str + ' ';
		str = str + 'T';
		str = str + ' ';
		str = str + (i+1);
		str = str + ' ';
		str = str + "0.9";
		str = str + ' ';
		str = str + i;
		str = str + ' ';
		str = str + "0.1";
		str = str + '\n';
		
		
		str = str + i;
		str = str + ' ';
		str = str + 'B';
		str = str + ' ';
		str = str + (i-1);
		str = str + ' ';
		str = str + "0.9";
		str = str + ' ';
		str = str + i;
		str = str + ' ';
		str = str + "0.1";
		str = str + '\n';
		
		str = str + i;
		str = str + ' ';
		str = str + 'J';
		str = str + ' ';
		str = str + (i+2);
		str = str + ' ';
		str = str + "0.9";
		str = str + ' ';
		str = str + (i+1);
		str = str + ' ';
		str = str + "0.1";
		str = str + '\n';
		
		str = str + i;
		str = str + ' ';
		str = str + 'R';
		str = str + ' ';
		str = str + (i-2);
		str = str + ' ';
		str = str + "0.9";
		str = str + ' ';
		str = str + (i-1);
		str = str + ' ';
		str = str + "0.1";
		str = str + '\n';
	}
	//state 9
		str = str + 9;
		str = str + ' ';
		str = str + 'T';
		str = str + ' ';
		str = str + 10;
		str = str + ' ';
		str = str + "0.9";
		str = str + ' ';
		str = str + 9;
		str = str + ' ';
		str = str + "0.1";
		str = str + '\n';
		
		str = str + 9;
		str = str + ' ';
		str = str + 'B';
		str = str + ' ';
		str = str + 8;
		str = str + ' ';
		str = str + "0.9";
		str = str + ' ';
		str = str + 9;
		str = str + ' ';
		str = str + "0.1";
		str = str + '\n';
		
		str = str + 9;
		str = str + ' ';
		str = str + 'J';
		str = str + ' ';
		str = str + 10;
		str = str + ' ';
		str = str + "1";
		str = str + '\n';
		
		str = str + 9;
		str = str + ' ';
		str = str + 'R';
		str = str + ' ';
		str = str + 7;
		str = str + ' ';
		str = str + "0.9";
		str = str + ' ';
		str = str + 8;
		str = str + ' ';
		str = str + "0.1";
		str = str + '\n';
		
		//State 10
		str = str + 10;
		str = str + ' ';
		str = str + 'T';
		str = str + ' ';
		str = str + 10;
		str = str + ' ';
		str = str + "1";
		str = str + '\n';
		
		
		str = str + 10;
		str = str + ' ';
		str = str + 'B';
		str = str + ' ';
		str = str + 9;
		str = str + ' ';
		str = str + "0.9";
		str = str + ' ';
		str = str + 10;
		str = str + ' ';
		str = str + "0.1";
		str = str + '\n';
		
		str = str + 10;
		str = str + ' ';
		str = str + 'J';
		str = str + ' ';
		str = str + 10;
		str = str + ' ';
		str = str + "1";
		str = str + '\n';
		
		str = str + 10;
		str = str + ' ';
		str = str + 'R';
		str = str + ' ';
		str = str + 8;
		str = str + ' ';
		str = str + "0.9";
		str = str + ' ';
		str = str + 9;
		str = str + ' ';
		str = str + "0.1";
		str = str + '\n';
	try {
		 
		File file = new File("filename.txt");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(str);
		bw.close();

		System.out.println("Done");

	} catch (IOException e) {
		e.printStackTrace();
	}

	
	// build MDP
	Mdp mdp = new Mdp(options.filename);
//	Mdp mdp = new Mdp("C:\\Users\\sachinsri12345\\Desktop\\AI4\\code\\data\\filename.txt");

	// run value iteration
	ValueIteration vpi = new ValueIteration(mdp, discount);

	// run policy iteration
	PolicyIteration ppi = new PolicyIteration(mdp, discount);

	// evaluate returned policy
	double[] util =
	    (new PolicyEvaluation(mdp, discount, ppi.policy)).utility;

	// print results
	System.out.println("Optimal policies:");
	for(int s = 0; s < mdp.numStates; s++)
	    System.out.printf(" %-12s  %-12s %-4s  %-4s  %17.12f  %17.12f\n",
			      mdp.stateName[s],
			      mdp.reward[s],
			      mdp.actionName[vpi.policy[s]],
			      mdp.actionName[ppi.policy[s]],
			      vpi.utility[s],
			      util[s]);
	System.out.println();
	System.out.println();
    }

    // private stuff for parsing command line options and printing
    // error messages

    private static class Options {
	private String filename = null;
	
	private Options(String argv[]) {
	    filename = argv[0];
		
	    if (filename == null)
		throw new RuntimeException("no filename specified");
	}
    }
}
