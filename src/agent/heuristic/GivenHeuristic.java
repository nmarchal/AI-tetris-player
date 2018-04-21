package src.agent.heuristic;

import src.game.State;

/**
 * class representing the heuristic given ins the projects instruction 0 ->
 * bias next num_cols -> height of walls next num_cols -> difference between
 * adj columns next 1 -> max col height next 1 -> num holes in wall
 */
public class GivenHeuristic implements Heuristic {

	static final int LENGTH = State.COLS + State.COLS - 1 + 3;

	private static final int INDICE_COLS_WEIGHTS = 1;
	private static final int INDICE_COLS_DIFF_WEIGTHS = INDICE_COLS_WEIGHTS + State.COLS;
	private static final int INDICE_MAX_HEIGHT_WEIGHT = INDICE_COLS_DIFF_WEIGTHS + State.COLS - 1;
	private static final int INDICE_HOLES_WEIGHT = INDICE_MAX_HEIGHT_WEIGHT + 1;

	public static final float[] BEST_WEIGHTS = { -2.064638f, 0.99241304f, -0.9892719f, -0.022724867f, -0.9527318f,
	-0.021005929f, 0.012356937f, -0.016091943f, -0.0025559664f, -1.0049679f, 0.9978579f, -2.940033f, -1.950203f,
	-2.0007744f, -1.956233f, -1.9986322f, -1.9524348f, -1.9964991f, -1.97714f, -2.9492042f, -0.009090185f,
	-9.940321f }; // loic 80'000

	@Override
	public float compute(State state, float[] weights) {

		if (state.lost) {
			return Float.NEGATIVE_INFINITY;
		}

		float heuristicValue = weights[0];
		int maxHeight = 0;

		for (int i = 0; i < State.COLS; i++) {
			// height
			int height = state.getTop()[i];
			heuristicValue += weights[INDICE_COLS_WEIGHTS + i] * height;
			if (height > maxHeight) {
				maxHeight = height;
			}
		}
		heuristicValue += maxHeight * weights[INDICE_MAX_HEIGHT_WEIGHT];

		// holes
		heuristicValue += HeuristicTool.holes(state) * weights[INDICE_HOLES_WEIGHT];

		// differences
		for (int i = 0; i < State.COLS - 1; i++) {
			int diff = Math.abs(state.getTop()[i] - state.getTop()[i + 1]);

			heuristicValue += weights[INDICE_COLS_DIFF_WEIGTHS + i] * diff;
		}

		return heuristicValue;
	}

	@Override
	public int weightsLength() {
		return LENGTH;
	}

	@Override
	public float[] featureValues(State state) {
		float[] values = new float[LENGTH];
		int maxHeight = 0;
		int holes = 0;
		for (int i = 0; i < State.COLS; i++) {
			// height
			int height = state.getTop()[i];
			values[i] = height;
			if (height > maxHeight) {
				maxHeight = height;
			}

			// holes
			for (int j = 0; j < height - 1; j++) {
				if (state.getField()[j][i] == 0) {
					holes++;
				}
			}
		}
		values[LENGTH - 1] = holes;
		values[LENGTH - 2] = maxHeight;

		// differences
		for (int i = 0; i < State.COLS - 1; i++) {
			int diff = Math.abs(state.getTop()[i] - state.getTop()[i + 1]);
			values[State.COLS + i] = diff;
		}
		return values;
	}

}