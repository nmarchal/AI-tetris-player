package src;

public class PlayerSkeleton {

	/*
	 * Constants used as parameters for the AI
	 */
	private static final float[] DUMBS_WEIGHTS = new float[GivenHeuristic.LENGTH];
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
	public static final StartingSolver BASIC_SOLVER = new StartingSolver(new GivenHeuristic());
	public static final MinMaxSolver MINMAX_SOLVER = new MinMaxSolver(new GivenHeuristic(), 2);
	
	/**
	 * Interface of heuristic
	 *
	 */
	public static interface Heuristic {
		
		/**
		 * @param next
		 * 		The state to with the heuristic to compute
		 * @param w
		 * 		the weights parameters of the heuristic
		 * @return
		 * 		the heuristic result
		 */
		public float compute(State next,float[] w);
		
		 /**
	     * @return the number of weight used by the solver
	     */
	    public int weightsLength();
	}

	/**
	 * class representing the heuristic given ins the projects instruction
	 *
	 */
	public static class GivenHeuristic implements Heuristic{

		public static final int LENGTH = State.COLS + State.COLS-1 + 3;
		
		private static final int INDICE_COLS_WEIGHTS = 1;
		private static final int INDICE_COLS_DIFF_WEIGTHS = INDICE_COLS_WEIGHTS + State.COLS;
		private static final int INDICE_MAX_HEIGHT_WEIGHT = INDICE_COLS_DIFF_WEIGTHS +State.COLS -1;
		private static final int INDICE_HOLES_WEIGHT = INDICE_MAX_HEIGHT_WEIGHT +1;
		
		@Override
		public float compute(State state, float[] weights) {
			float heuristicValue = weights[0];
			int maxHeight =0;
			int holes =0;
			for(int i=0; i<State.COLS; i++){
				
				//height
				int height = state.getTop()[i];
				heuristicValue += weights[INDICE_COLS_WEIGHTS+i]*height;
				if(height>maxHeight){
					maxHeight = height;
				}
				
				//holes
				for(int j=0; j<height-1;j++){
					if(state.getField()[j][i]==0){
						holes++;
					}
				}
			}
			heuristicValue += holes*weights[INDICE_HOLES_WEIGHT];
			heuristicValue += maxHeight*weights[INDICE_MAX_HEIGHT_WEIGHT];
			
			//differences
			for(int i=0; i<State.COLS-1; i++){
				int diff =Math.abs(state.getTop()[i]-state.getTop()[i+1]);
				
				heuristicValue += weights[INDICE_COLS_DIFF_WEIGTHS+i]*diff;
			}
			
			if(state.lost){
				heuristicValue = Float.NEGATIVE_INFINITY;
			}
			return heuristicValue;
		}

		@Override
		public int weightsLength() {
			return LENGTH;
		}
		
	}
	
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
		
		private final Heuristic heuristic;
		/**
		 * Default constructor, initialize all weights to 0
		 */
		public StartingSolver(Heuristic heuristic) {
			this.heuristic = heuristic;
		}
		

		@Override
		public int pickMove(State s, int[][] legalMoves, float[] w) {
			if( w.length != weightsLength()){
				throw new IllegalArgumentException("wrong number of weights: "+ w.length+". Expected: "+weightsLength()); 
			}
			float max = Float.NEGATIVE_INFINITY; 
			int bestMove=0;
			int n =0;
			for(int[] move: legalMoves){
				State next = TetrisSolver.nextState(s,move);
				float heuristicValue = heuristic.compute(next, w);
				
				if(heuristicValue>max){
					max = heuristicValue;
					bestMove = n;
				}
				n++;
			}
			
			return bestMove;
		}

		@Override
		public int weightsLength() {
			return heuristic.weightsLength();
		}

	}
	
	
	
	
	/**
	 * Solver using MinMax Algorithm
	 *
	 */
	public static final class MinMaxSolver implements TetrisSolver{
		
		private final Heuristic heuristic;
		private final int depth;
		
		public MinMaxSolver(Heuristic heur,int depth) {
			heuristic = heur;
			this.depth = depth;
		}
		
		@Override
		public int pickMove(State s, int[][] legalMoves, float[] weights) {
			if( weights.length != weightsLength()){
				throw new IllegalArgumentException("wrong number of weights: "+ weights.length+". Expected: "+weightsLength()); 
			}
			float max = Float.NEGATIVE_INFINITY; 
			int bestMove=0;
			int n =0;
			for(int[] move: legalMoves){
				State next = TetrisSolver.nextState(s,move);
				float heuristicValue = minmax(next, depth, false, weights);
				
				if(heuristicValue>max){
					max = heuristicValue;
					bestMove = n;
				}
				n++;
			}
			
			return bestMove;
		}

		@Override
		public int weightsLength() {
			return heuristic.weightsLength();
		}
		
		/**
		 * @param s
		 * 		state of the node
		 * @param d
		 * 		depth
		 * @param maximizing
		 * 		boolean true if we try to maximize
		 * @param weights
		 * 		weights 
		 * @return the MINMAX best heuristic value
		 */
		private float minmax(State s,int d,boolean maximizing,float[] weights){
			if(s.hasLost()){
				return Float.NEGATIVE_INFINITY;
			}
			if(d <=0 ){
				return heuristic.compute(s,weights );
			}
			
			if(maximizing){
				float best = Float.NEGATIVE_INFINITY;
				for(int[] move :s.legalMoves()){
					State next = TetrisSolver.nextState(s, move);
					float v = minmax(next, d-1, false, weights);
					if(v>best){
						best = v;
					}
				}
				return best;
			}else{
				float best = Float.POSITIVE_INFINITY;
				for(int i = 0; i<State.N_PIECES; i++){
					s.nextPiece =i;
					float v = minmax(s, d-1, true, weights);
					if(v<best){
						best = v;
					}
				}
				return best;
			}
		}
	}
	
	
	
	/**
	 * MAIN FUNCTION
	 * @param args
	 */
	public static void main(String[] args) {
		State s = new State();
		new TFrame(s);
		TetrisSolver aI = MINMAX_SOLVER;
		long start = System.currentTimeMillis();
		while(!s.hasLost()) {
			s.makeMove(aI.pickMove(s,s.legalMoves(),BEST_WEIGHTS));
			s.draw();
			s.drawNext(0,0);
			/*try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
		long end = System.currentTimeMillis();
		long diff = end-start;
		System.out.println("it takes "+ diff + " milliseconds");
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}
