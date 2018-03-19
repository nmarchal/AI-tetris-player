public interface TetrisLearner {
    void learn(int threshold, int iteration, int startingWeight, State startingState);
    void storeResult(String fileName);
}
