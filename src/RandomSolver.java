package src;

public class RandomSolver implements TetrisSolver {

	@Override
	public int pickMove(State s, int[][] legalMoves) {
		return (int) Math.abs(Math.random()*legalMoves.length);
	}

}
