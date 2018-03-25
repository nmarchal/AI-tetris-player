package src;

/**
 * Learner for Tetris.
 * Implement a strategy to improve results over time
 * 
 */
public interface TetrisLearner {
    
	/** 
	 * Learn how to solve a Tetris during a certain duration.
	 * 	It tries man many different ways and improves himself
     * @param solver
     * 		The solver used to resolve Tetris
     * @param startingWeights
     * 		the starting weights used for the very first try of the learner
     * @param duration
     * 		represent the limit/time/iterations that have the learner before finishing
     * @param maxLine
     * 		represent the maximum number of line to remove in the tetris before considering it infinity
     * @return
     */
    public float[] learn(PlayerSkeleton.TetrisSolver solver, int duration, int maxLine);


    public static int solveAvg(PlayerSkeleton.TetrisSolver solver,float[] weights, int maxLine, int n ){
    	int sum = 0;
    	for(int i=0; i<n ; i++){
    		State s = new State();
    		while(!s.hasLost() && s.getRowsCleared()<maxLine){
    			s.makeMove(solver.pickMove(s,s.legalMoves(), weights));
    		}
    		sum += s.getRowsCleared();
    	}
    	return sum/n;
    }

}
