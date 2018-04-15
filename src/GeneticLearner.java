package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

import javafx.util.Pair;
import src.PlayerSkeleton.TetrisSolver;

public class GeneticLearner implements TetrisLearner {

	private static final Random random = new Random();
	private final int populationSize;

	public GeneticLearner(int population) {
		this.populationSize = population;
	}

	@Override
	public float[] learn(TetrisSolver solver, int duration, int maxLine, int averageGamePlayed,
			float[] startingWeights) {

		if (solver.weightsLength() != startingWeights.length) {
			throw new IllegalArgumentException(
					"You should enter " + solver.weightsLength() + " weights instead of " + startingWeights.length);
		}

		try (Writer writer = new BufferedWriter(new FileWriter("data.csv", false))) {
			
			
			for (int i = 0; i < startingWeights.length; i++) {
				writer.write("w" + i + ";");
			}
			writer.write("next Value; value; time(ms);\n");
			
			long startTime = System.currentTimeMillis();
			/*
			 * Initialize population
			 */
			LinkedList<Pair<Integer, float[]>> population = new LinkedList<>();
			for (int i = 0; i < populationSize; i++) {
				float[] w = generateState(solver.weightsLength());
				int value = TetrisLearner.solveAvg(solver, w, maxLine, averageGamePlayed, 0);
				Pair<Integer, float[]> pair = new Pair<>(value, w);
				population.add(pair);
			}
			Comparator<Pair<Integer, float[]>> bestVal = (p1, p2) -> Integer.compare(p2.getKey(), p1.getKey());
			population.sort(bestVal);

			/*
			 * Start simulation
			 */
			for (int n = 0; n < duration; n++) {
				LinkedList<Pair<Integer, float[]>> nextGen = new LinkedList<>();

				boolean male = true;
				float[] last = null;
				for (Pair<Integer, float[]> p : population) {
					if (male) {
						last = p.getValue();
						male = false;
					} else {
						male = true;
						float[] w1 = new float[solver.weightsLength()];
						float[] w2 = new float[solver.weightsLength()];
						/*
						 * CrossOver
						 */
						for (int i = 0; i < solver.weightsLength(); i++) {
							boolean selection = random.nextInt(2) == 0;
							if (selection) {
								w1[i] = last[i];
								w2[i] = p.getValue()[i];
							} else {
								w2[i] = last[i];
								w1[i] = p.getValue()[i];
							}
						}

						/*
						 * Mutation
						 */
						int mutationIndex = random.nextInt(solver.weightsLength());
						w1[mutationIndex] = random.nextFloat() - 0.5f;
						mutationIndex = random.nextInt(solver.weightsLength());
						w2[mutationIndex] = random.nextFloat() - 0.5f;

						/*
						 * Fitness evaluation
						 */
						int value1 = TetrisLearner.solveAvg(solver, w1, maxLine, averageGamePlayed, 0);
						int value2 = TetrisLearner.solveAvg(solver, w2, maxLine, averageGamePlayed, 0);
						Pair<Integer, float[]> pair1 = new Pair<>(value1, w1);
						Pair<Integer, float[]> pair2 = new Pair<>(value2, w2);
						nextGen.add(pair1);
						nextGen.add(pair2);
					}
				}

				/*
				 * Fitness selection
				 */
				population.addAll(nextGen);
				population.sort(bestVal);
				population.subList(populationSize, population.size()).clear();
				for (float w : nextGen.getFirst().getValue()) {
					writer.write(w + ";");
				}
				writer.write(nextGen.getFirst().getKey() + ";" + population.getFirst().getKey() + ";" +(System.currentTimeMillis()-startTime)+"\n");
				writer.flush();

				System.out.println(n + "/" + duration + " : " + population.getFirst().getKey());
			}
			return population.getFirst().getValue();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Error();
		}
	}

	/**
	 * @param n
	 *            number of weights
	 * @return a random set of weights
	 */
	private static final float[] generateState(int n) {
		float[] weights = new float[n];
		for (int i = 0; i < n; i++) {
			weights[i] = random.nextFloat() - 0.5f;
		}
		return weights;
	}

}
