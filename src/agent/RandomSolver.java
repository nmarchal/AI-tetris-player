package agent;

import game.State;

/**
 * Solver that return a random move
 *
 */
public final class RandomSolver implements TetrisSolver {

	@Override
	public int pickMove(State s, int[][] legalMoves, float[] w) {
		return (int) (Math.random() * legalMoves.length);
	}

	@Override
	public int weightsLength() {
		return 0;
	}


}