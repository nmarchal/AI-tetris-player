package src.agent;

import src.agent.heuristic.Heuristic;
import src.game.State;

/**
 * Solver using MinMax Algorithm
 *
 */
public final class MinMaxSolver implements TetrisSolver {

	private final Heuristic heuristic;
	private final int depth;

	/**
	 * @param heur
	 * @param depth
	 *            depth used for the minmax algorithm,
	 */
	public MinMaxSolver(Heuristic heur, int depth) {
		heuristic = heur;
		this.depth = depth;
	}

	@Override
	public int pickMove(State s, int[][] legalMoves, float[] weights) {
		if (weights.length != weightsLength()) {
			throw new IllegalArgumentException(
					"wrong number of weights: " + weights.length + ". Expected: " + weightsLength());
		}
		float max = Float.NEGATIVE_INFINITY;
		int bestMove = 0;

		int d = depth - 1;
		while (max == Float.NEGATIVE_INFINITY && d >= 0) {
			int n = 0;
			for (int[] move : legalMoves) {
				State next = TetrisSolver.nextState(s, move);
				float heuristicValue = minmax(next, d, false, weights, max, Float.POSITIVE_INFINITY);

				if (heuristicValue > max) {
					max = heuristicValue;
					bestMove = n;
				}
				n++;
			}
			d -= 1;
		}
		return bestMove;
	}

	@Override
	public int weightsLength() {
		return heuristic.weightsLength();
	}

	/**
	 * Minmax algorithm applied for tetris. Max tries to play the best legal
	 * move Min tries to select the most annoying piece as the next piece
	 * playable
	 * 
	 * @param s
	 *            state of the node
	 * @param d
	 *            depth
	 * @param maximizing
	 *            boolean true if we try to maximize
	 * @param weights
	 *            weights
	 * @return the MINMAX best heuristic value
	 */
	private float minmax(State s, int d, boolean maximizing, float[] weights, float alpha, float beta) {
		if (s.hasLost()) {
			return Float.NEGATIVE_INFINITY;
		}
		if (d <= 0) {
			return heuristic.compute(s, weights);
		}

		if (maximizing) {
			float best = Float.NEGATIVE_INFINITY;
			for (int[] move : s.legalMoves()) {
				State next = TetrisSolver.nextState(s, move);
				float v = minmax(next, d - 1, false, weights, alpha, beta);
				if (v > best) {
					best = v;
				}
				if (alpha < v) {
					alpha = v;
				}
				if (beta <= alpha) {
					break;
				}
			}
			return best;
		} else {
			float best = Float.POSITIVE_INFINITY;
			for (int i = 0; i < State.N_PIECES; i++) {
				s.nextPiece = i;
				float v = minmax(s, d, true, weights, alpha, beta);
				if (v < best) {
					best = v;
				}
				if (v < beta) {
					beta = v;
				}
				if (beta <= alpha) {
					break;
				}
			}
			return best;
		}
	}

	public float[] featureValues(State s) {
		return heuristic.featureValues(s);
	}
}