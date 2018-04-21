package src.agent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import src.agent.heuristic.Heuristic;
import src.game.State;

/**
 * Solver using the heuristic function at depth 1
 *
 */
public final class StartingSolver implements TetrisSolver {

	// for benchmarking
	public int statesEvaluated = 0;

	private final Heuristic heuristic;

	/**
	 * Default constructor, initialize all weights to 0
	 */
	public StartingSolver(Heuristic heuristic) {
		this.heuristic = heuristic;
	}

	@Override
	public int pickMove(State s, int[][] legalMoves, float[] w) {
		if (w.length != weightsLength()) {
			throw new IllegalArgumentException(
					"wrong number of weights: " + w.length + ". Expected: " + weightsLength());
		}
		List<Float> values = Arrays.stream(legalMoves).parallel().map(move -> getHeuristicValue(move, s, w))
				.collect(Collectors.toList());

		statesEvaluated += values.size();

		int maxIdx = IntStream.range(0, values.size()).reduce(0, (i, j) -> values.get(i) > values.get(j) ? i : j);

		return maxIdx;
	}

	private float getHeuristicValue(int[] move, State state, float[] weights) {
		State next = TetrisSolver.nextState(state, move);
		return heuristic.compute(next, weights);
	}

	@Override
	public int weightsLength() {
		return heuristic.weightsLength();
	}

	public float[] featureValues(State s) {
		return heuristic.featureValues(s);
	}

}