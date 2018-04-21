package src.agent.heuristic;

import src.game.State;

/**
 * Interface of heuristic
 *
 */
public interface Heuristic {

	/**
	 * Compute the heuristic value of a state
	 * 
	 * @param next
	 *            The state to with the heuristic to compute
	 * @param w
	 *            the weights parameters of the heuristic
	 * @return the heuristic result
	 */
	public float compute(State next, float[] w);

	/**
	 * Gives an approximation of the current state based on the heuristic
	 * 
	 * @param state
	 *            The state to approximate
	 * @return The vector of feature values at the state
	 */
	public float[] featureValues(State state); // FIXME ????

	/**
	 * @return the number of weight used by the solver
	 */
	public int weightsLength();
}