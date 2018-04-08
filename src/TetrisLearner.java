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
	 * @param duration
     * 		represent the limit/time/iterations that have the learner before finishing
	 * @param maxLine
     * 		represent the maximum number of line to remove in the tetris before considering it infinity
	 * @param averageGamePlayed
     * 		The number of games taken to do an average
	 * @param startingWeights TODO
	 * @param startingWeights
     * 		the starting weights used for the very first try of the learner
	 * @param lastValue
     * 		the previous value of lines
     * @return
     */
    public float[] learn(PlayerSkeleton.TetrisSolver solver, int duration, int maxLine, int averageGamePlayed, float[] startingWeights);
    
    public default float[] learn(PlayerSkeleton.TetrisSolver solver, int duration, int maxLine, int averageGamePlayed){
    	return learn(solver,duration,maxLine,averageGamePlayed, new float[solver.weightsLength()]);
    }


    public static int solveAvg(PlayerSkeleton.TetrisSolver solver,float[] weights, int maxLine, int n, int lastValue){
    	int sum = 0;
    	for(int i=0; i<n ; i++){
    		if(i==4 &&lastValue>50 && sum*4<i*lastValue*3){
    			return sum /i;
    		}
    		State s = new State();
    		while(!s.hasLost() && s.getRowsCleared()<maxLine){
    			s.makeMove(solver.pickMove(s,s.legalMoves(), weights));
    		}
    		sum += s.getRowsCleared();
    	}
    	return sum/n;
    }
    
    public static int solveMin(PlayerSkeleton.TetrisSolver solver,float[] weights, int maxLine, int n, int lastValue){
    	int min = maxLine;
    	for(int i=0; i<n ; i++){
    		State s = new State();
    		while(!s.hasLost() && s.getRowsCleared()<maxLine){
    			s.makeMove(solver.pickMove(s,s.legalMoves(), weights));
    		}
    		if(s.getRowsCleared()<min){
    			min = s.getRowsCleared();
    			if(min<lastValue){
    				return min;
    			}
    		}
    	}
    	return min;
    }

}
