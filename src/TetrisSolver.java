package src;

public interface TetrisSolver {
    int pickMove(State s, int[][] legalMoves);
}
