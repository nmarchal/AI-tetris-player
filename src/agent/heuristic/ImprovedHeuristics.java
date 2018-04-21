package src.agent.heuristic;

import src.game.State;

/**
 * Uses both given heuristics & new heuristics FIXME change name?
 */
public class ImprovedHeuristics extends GivenHeuristic {

	public static final float[] BEST_WEIGHTS_FINAL = { -9.854448f, 2.4389153f,	-2.333226f,	0.36771116f,	-0.52315074f,
			-1.3534356f,	0.024638796f,	0.27249014f, 	-0.54726523f,	-1.4634881f, 	2.3102803f,  	-3.7318974f,
			-2.8147786f, 	-3.0926073f, 	-2.84025f, 	-3.6721375f, 	-2.2535586f, 	-2.2111578f, 	-2.9119258f,
			-3.4484754f, 	-3.2507586f, 	-7.856307f, 	-8.207972f, 	-0.047947817f, 	1.5860023f,	3.6743877f
	};

	@Override
	public float compute(State next, float[] w) {
		float oldScore = super.compute(next, w);

		int nextIndex = super.weightsLength();
		float newScore = 0;

		// grouped holes
		newScore += HeuristicTool.groupedHoles(next) * w[nextIndex];
		nextIndex++;

		// sum of heights
		newScore += HeuristicTool.sumOfHeights(next) * w[nextIndex];
		nextIndex++;

		// max difference
		newScore += HeuristicTool.maxHeightsDifference(next) * w[nextIndex];
		nextIndex++;

		// squares above holes
		newScore += HeuristicTool.squaresAboveHoles(next) * w[nextIndex];
		nextIndex++;

		if (next.hasLost())
			newScore = Float.NEGATIVE_INFINITY;

		return oldScore + newScore;
	}

	@Override
	public int weightsLength() {
		return super.weightsLength() + 4;
	}

	@Override
	public float[] featureValues(State state) {
		float[] oldValues = super.featureValues(state);

		float[] newValues = new float[oldValues.length + weightsLength()];
		System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);

		int nextIndex = oldValues.length;
		newValues[nextIndex] = HeuristicTool.groupedHoles(state);
		nextIndex++;

		newValues[nextIndex] = HeuristicTool.sumOfHeights(state);
		nextIndex++;

		newValues[nextIndex] = HeuristicTool.maxHeightsDifference(state);
		nextIndex++;

		newValues[nextIndex] = HeuristicTool.squaresAboveHoles(state);
		nextIndex++;

		return newValues;
	}
}