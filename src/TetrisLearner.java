public interface TetrisLearner {
    public void learn(int threshold, int iteration, int startingWeight, State startingState);
    public void storeResult(String fileName);
}
