package src;

import src.State;

public interface TetrisSolver {
    int pickMove(State s, int[][] legalMoves);
}
