package src;

public class PlayerSkeleton {

	/*
	 * Constants used as parameters for the AI
	 */
	private static final float[] DUMBS_WEIGHTS = new float[StartingSolver.LENGTH];
	private static final float[] BASICS_WEIGHTS = {
			0,								//weight 0
			-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,	//columns weights
			-1,-1,-1,-1,-1,-1,-1,-1,-1,		//differences between columns weights
			-1,								//
			-50
	};
	private static final float[] COMPUTED_WEIGHTS = { -4.0f, 0.0f, -1.0f, 0.0f, -2.0f, -1.0f, 0.0f, 0.0f, -1.0f, -2.0f,
			1.0f, -3.0f, -1.0f, -2.0f, -2.0f, -2.0f, -1.0f, -2.0f, -2.0f, -1.0f, -3.0f, -9.0f };
	private static final float[] BEST_WEIGHTS = {
			-4.0f, 0.0f, -1.0f, 0.0f, -2.0f, -1.0f, 0.0f, 0.0f, -1.0f, -2.0f,
			1.0f, -3.0f, -1.0f, -2.0f, -2.0f, -2.0f, -1.0f, -2.0f, -2.0f, -1.0f, -3.0f, -9.0f 
	};
	
	
	/*
	 * Solvers: different AI with different parameters
	 * 
	 */
	public static final RandomSolver RANDOM_SOLVER = new RandomSolver();
	public static final StartingSolver BASIC_SOLVER = new StartingSolver();

	
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
	    public int pickMove(State s, int[][] legalMoves, float[] weights);
	    
	    /**
	     * @return the number of weight used by the solver
	     */
	    public int weightsLength();
	    
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
	public static final class RandomSolver implements TetrisSolver {

		@Override
		public int pickMove(State s, int[][] legalMoves, float[] w) {
			return (int) (Math.random()*legalMoves.length);
		}

		@Override
		public int weightsLength() {
			return 0;
		}

	}
	
	
	
	/**
	 * Solver using the heuristic function at depth 1
	 *
	 */
	public static final class StartingSolver implements TetrisSolver{
		
		public static final int LENGTH = State.COLS + State.COLS-1 + 3;
		
		private static final int INDICE_COLS_WEIGHTS = 1;
		private static final int INDICE_COLS_DIFF_WEIGTHS = INDICE_COLS_WEIGHTS + State.COLS;
		private static final int INDICE_MAX_HEIGHT_WEIGHT = INDICE_COLS_DIFF_WEIGTHS +State.COLS -1;
		private static final int INDICE_HOLES_WEIGHT = INDICE_MAX_HEIGHT_WEIGHT +1;
		
		
		/**
		 * Default constructor, initialize all weights to 0
		 */
		public StartingSolver() {}
		

		@Override
		public int pickMove(State s, int[][] legalMoves, float[] w) {
			if( w.length != LENGTH){
				throw new IllegalArgumentException("wrong number of weights: "+ w.length+". Expected: "+LENGTH); 
			}
			float max = Float.NEGATIVE_INFINITY; 
			int bestMove=0;
			int n =0;
			for(int[] move: legalMoves){
				State next = TetrisSolver.nextState(s,move);
				float heuristic = w[0];
				int maxHeight =0;
				int holes =0;
				for(int i=0; i<State.COLS; i++){
					
					//height
					int height = next.getTop()[i];
					heuristic += w[INDICE_COLS_WEIGHTS+i]*height;
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
				heuristic += holes*w[INDICE_HOLES_WEIGHT];
				heuristic += maxHeight*w[INDICE_MAX_HEIGHT_WEIGHT];
				
				//differences
				for(int i=0; i<State.COLS-1; i++){
					int diff =Math.abs(next.getTop()[i]-next.getTop()[i+1]);
					
					heuristic += w[INDICE_COLS_DIFF_WEIGTHS+i]*diff;
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

		@Override
		public int weightsLength() {
			return LENGTH;
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
		long start = System.currentTimeMillis();
		while(!s.hasLost()) {
			s.makeMove(aI.pickMove(s,s.legalMoves(),COMPUTED_WEIGHTS));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		long diff = end-start;
		System.out.println("it takes "+ diff + " milliseconds");
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}
