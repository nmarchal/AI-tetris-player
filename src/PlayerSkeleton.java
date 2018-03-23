package src;

import java.util.Arrays;

public class PlayerSkeleton {

	/*
	 * Constants used as parameters for the AI
	 */
	private static final float[] BASIC_W_COLS = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	private static final float[] BASIC_W_DIFF = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
	
	
	/*
	 * Solvers: different AI with different parameters
	 * 
	 */
	public static final StartingSolver DUMB_SOLVER = new StartingSolver();
	public static final RandomSolver RANDOM_SOLVER = new RandomSolver();
	public static final StartingSolver BASIC_SOLVER = new StartingSolver(1, BASIC_W_COLS,BASIC_W_DIFF, -1, -50);

	
	/**
	 * Interface that represent an AI for Tetris
	 */
	public static interface TetrisSolver {
		
	    /**
	     * Return the best move given the current state of the Tetris board
	     * @param s
	     * @param legalMoves
	     * @return
	     */
	    public int pickMove(State s, int[][] legalMoves);
	    
	    /**
	     * Create a new State of the game after the selected move
	     * This does not affect the given state,
	     * thus we can call this function on the current state without changing it.
	     * @param s
	     * 		Current state of the game
	     * @param move
	     * 		move played
	     * @return
	     * 		the state of the game after playing the selected move
	     */
	    public static State nextState(State s, int[] move){
	    	State next = new State();
	    	next.lost = s.lost;
	    	next.nextPiece = s.nextPiece;
	    	int[][] field = s.getField();
	    	int[][] copyField = next.getField();
	    	for(int i=0;i<State.ROWS;i++){
	    		for(int j=0; j<State.COLS; j++){
	    			copyField[i][j] = field[i][j];
	    		}
	    	}
	    	for(int i=0;i<State.COLS;i++){
	    		next.getTop()[i] = s.getTop()[i];
	    	}
	    	
	    	next.makeMove(move);
	    	
	    	return next;
	    }
	}
	
	
	
	/**
	 * Solver that return a random move
	 *
	 */
	public static class RandomSolver implements TetrisSolver {

		@Override
		public int pickMove(State s, int[][] legalMoves) {
			return (int) (Math.random()*legalMoves.length);
		}

	}
	
	
	
	/**
	 * Solver using the heuristic function at depth 1
	 *
	 */
	public static class StartingSolver implements TetrisSolver{

		private final float weight0;
		private final float[] weightsColumnsHeights;
		private final float[] weightsDifferencesColumns;
		private final float weightMaximumHeight;
		private final float weightHoles;
		
		/**
		 * Default constructor, initialize all weights to 0
		 */
		public StartingSolver() {
			this.weight0 =0;
			this.weightsColumnsHeights =  new float[State.COLS];
			this.weightsDifferencesColumns = new float[State.COLS-1];
			this.weightHoles =0;
			this.weightMaximumHeight =0;
		}
		
		/**
		 * 	Constructor initializing all weights
		 * @param weight0
		 * 		constant added to the heuristic
		 * @param weigthsColumnsHeights
		 * 		weights for every column height
		 * @param weightsDifferencesColumns
		 * 		weights of differences of heights between column
		 * @param weightMaximumHeight
		 * 		weights 
		 * @param weightHoles
		 */
		public StartingSolver(float weight0 ,float[] weigthsColumnsHeights, float[] weightsDifferencesColumns,float weightMaximumHeight,float weightHoles){
			this.weight0 = weight0;
			this.weightsColumnsHeights = Arrays.copyOf(weigthsColumnsHeights, State.COLS);
			this.weightsDifferencesColumns = Arrays.copyOf(weightsDifferencesColumns, State.COLS-1);
			this.weightMaximumHeight = weightMaximumHeight;
			this.weightHoles = weightHoles;
		}
		

		@Override
		public int pickMove(State s, int[][] legalMoves) {
			float max = Float.NEGATIVE_INFINITY; 
			int bestMove=0;
			int n =0;
			for(int[] move: legalMoves){
				State next = TetrisSolver.nextState(s,move);
				float heuristic = weight0;
				int maxHeight =0;
				int holes =0;
				for(int i=0; i<State.COLS; i++){
					
					//height
					int height = next.getTop()[i];
					heuristic += weightsColumnsHeights[i]*height;
					if(height>maxHeight){
						maxHeight = height;
					}
					
					//holes
					for(int j=0; j<height-1;j++){
						if(next.getField()[j][i]==0){
							holes++;
						}
					}
				}
				heuristic += holes*weightHoles;
				heuristic += maxHeight*weightMaximumHeight;
				
				//differences
				for(int i=0; i<State.COLS-1; i++){
					int diff =Math.abs(next.getTop()[i]-next.getTop()[i+1]);
					
					heuristic += weightsDifferencesColumns[i]*diff;
				}
				
				if(next.lost){
					heuristic = Float.NEGATIVE_INFINITY;
				}
				
				if(heuristic>max){
					max = heuristic;
					bestMove = n;
				}
				n++;
			}
			
			return bestMove;
		}

	}
	
	
	
	
	
	
	
	
	/**
	 * MAIN FUNCTION
	 * @param args
	 */
	public static void main(String[] args) {
		State s = new State();
		new TFrame(s);
		TetrisSolver aI = BASIC_SOLVER;
		while(!s.hasLost()) {
			s.makeMove(aI.pickMove(s,s.legalMoves()));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}
