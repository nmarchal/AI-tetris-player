package src.agent;

import src.game.State;

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

	public float[] featureValues(State s) {
		return null;
	}

}