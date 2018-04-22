package agent;

import agent.heuristic.GivenHeuristic;
import agent.heuristic.ImprovedHeuristics;
import game.State;

/**
 * Interface that represent an AI for Tetris
 */
public interface TetrisSolver {

	/*
	 * Solvers: different AI with different parameters
	 * 
	 */
	RandomSolver RANDOM_SOLVER = new RandomSolver();
	StartingSolver BASIC_SOLVER = new StartingSolver(new GivenHeuristic());
	MinMaxSolver MINMAX_SOLVER = new MinMaxSolver(new GivenHeuristic(), 2);
	MinMaxSolver DEEPER_MINMAX_SOLVER = new MinMaxSolver(new GivenHeuristic(), 3);
	TetrisSolver IMPROVED_BASIC_SOLVER = new StartingSolver(new ImprovedHeuristics());
	MinMaxSolver IMPROVED_MINMAX_SOLVER = new MinMaxSolver(new ImprovedHeuristics(), 2);
	MinMaxSolver IMPROVED_DEEPER_MINMAX_SOLVER = new MinMaxSolver(new ImprovedHeuristics(), 3);

	/**
	 * Return the best move given the current state of the Tetris board
	 * 
	 * @param s
	 *            the current state of the game
	 * @param legalMoves
	 *            authorized moves that can be played on this state
	 * @return the index of the selected move from legalMoves
	 */
	public int pickMove(State s, int[][] legalMoves, float[] weights);


	/**
	 * @return the number of weight used by the solver
	 */
	public int weightsLength();

	/**
	 * Create a new State of the game after the selected move This does not
	 * affect the given state, thus we can call this function on the current
	 * state without changing it.
	 * 
	 * @param s
	 *            Current state of the game
	 * @param move
	 *            move played
	 * @param next
	 * 		next state of the game
	 * @return the state of the game after playing the selected move
	 */
	public static State nextState(State s, int[] move) {
		State next = new State();
		return nextState(s, move, next);
		
	}
	public static State nextState(State s, int[] move,State next) {
		next.lost = s.lost;
		next.nextPiece = s.nextPiece;
		int[][] field = s.getField();
		int[][] copyField = next.getField();
		for (int i = 0; i < State.ROWS; i++) {
			for (int j = 0; j < State.COLS; j++) {
				copyField[i][j] = field[i][j];
			}
		}
		for (int i = 0; i < State.COLS; i++) {
			next.getTop()[i] = s.getTop()[i];
		}

		next.makeMove(move);

		return next;
	}
}