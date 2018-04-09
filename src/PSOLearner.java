package src;

import javafx.util.Pair;
import src.PlayerSkeleton.TetrisSolver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public final class PSOLearner implements TetrisLearner {

	private final Random random = new Random();
	private final int populationSize;
	// key is min, value is max
	private final Pair<Float, Float> initializationRange;

	public PSOLearner(int populationSize, Pair<Float, Float> initializationRange) {
		this.populationSize = populationSize;

		if (initializationRange == null) {
			this.initializationRange = new Pair<>(-1f, 1f);
		} else {
			this.initializationRange = initializationRange;
		}
	}

	@Override
	public float[] learn(TetrisSolver solver, int duration, int maxLine, int averageGamePlayed,
			float[] startingWeights) {

		/*
		 * Open 2 files, 
		 * 	1 for all the data, easier for us to understand what happen
		 *  1 for the best weigths only, so it's easier to find the right result
		 */
		try (Writer writer = new BufferedWriter(new FileWriter("data.csv", false));
				Writer writerBest = new BufferedWriter(new FileWriter("bestRes.txt", false))) {

			//Initialize the file header
			for (int i = 0; i < startingWeights.length; i++) {
				writer.write("w" + i + ";");
			}
			writer.write("next Value; value;\n");


			float[] bestWeights = startingWeights;
			int bestScore = TetrisLearner.solveAvg(solver, startingWeights, maxLine, averageGamePlayed, Integer.MAX_VALUE);

			/*
			 * Initialization
			 */
			// initialize the particles
			// randomly scatter particles around the weights
			// start them with 0 velocity
			List<float[]> particlePositions = new ArrayList<>();
			List<float[]> particleBestPositions = new ArrayList<>();
			List<Integer> particleBestScores = new ArrayList<>();
			List<float[]> particleVelocities = new ArrayList<>();
			for (int i = 0; i < populationSize; i++) {
				float[] cloned = startingWeights.clone();

				for (int j = 0; j < cloned.length; j++) {
					float min = initializationRange.getKey();
					float max = initializationRange.getValue();
					double random = min + Math.random() * (max-min);
					cloned[j] += random;
				}
				particlePositions.add(cloned);
				particleBestPositions.add(cloned);
				particleVelocities.add(new float[cloned.length]);
				particleBestScores.add(bestScore);
			}

			int previousIterationBestScore = bestScore;
			/*Ø
			 * PSO using n cycles
			 * Terminate early if we get a good enough result
			 */
			for (int n = 0; n < duration; n++) {

				// for each particle:
				// evaluate the score of each particle
				int finalBestScore = bestScore;
				int[] scores = IntStream.range(0, populationSize)
								.parallel()
								.map(idx -> TetrisLearner.solveAvg(solver,
												particleBestPositions.get(idx),
												maxLine,
												averageGamePlayed,
												finalBestScore))
								.toArray();

				// update best score & previous best position
				IntStream.range(0, populationSize)
								.forEach(idx -> {
									if (scores[idx] > particleBestScores.get(idx)){
										particleBestPositions.set(idx, particlePositions.get(idx));
										particleBestScores.set(idx, scores[idx]);
									}
								});

				// use gbest topology to find neighbor
				int maxIdx = IntStream.range(0, scores.length)
								.reduce(0, (i,j) -> scores[i] > scores[j] ? i : j);

				float[] iterationBestWeights = particlePositions.get(maxIdx);
				int iterationBestScore = scores[maxIdx];

				// adjust the position & velocity of all particles
				// use Clerc & Kennedy's results for constricting updates
				float w = 0.7298f;
				float sigma = 1.49618f;
				IntStream.range(0, populationSize)
								.forEach(particleIdx -> {
									// update velocity, float[] is an object, don't need to set back into the list
									float[] velocity = particleVelocities.get(particleIdx);
									float[] position = particlePositions.get(particleIdx);
									float[] bestPosition = particleBestPositions.get(particleIdx);
									IntStream.range(0, velocity.length)
													.forEach(weightIdx -> {
														double gaussian1 = random.nextGaussian() * sigma;
														double gaussian2 = random.nextGaussian() * sigma;
														velocity[weightIdx] = (float) (w*velocity[weightIdx]
																		+ gaussian1*(bestPosition[weightIdx]-position[weightIdx])
																		+ gaussian2*(iterationBestWeights[weightIdx]-position[weightIdx]));
													});

									// update the position, float[] is an object, don't need to set back into the list
									float[] pos = particlePositions.get(particleIdx);
									IntStream.range(0, pos.length)
													.forEach(weightIdx -> pos[weightIdx] += velocity[weightIdx]);
								});


				/*
				 * Write data in file
				 */
				for (float x : iterationBestWeights) {
					writer.write(x + ";");
				}
				writer.write(iterationBestScore + ";" + previousIterationBestScore + "\n");
				writer.flush();
				previousIterationBestScore = iterationBestScore;

				/*
				 * change the value and weights if the score is better
				 */
				// change some values

				/*
				 * If the value is the best found so far write it on a file
				 */
				if (bestScore < iterationBestScore) {
					writerBest.write(Arrays.toString(iterationBestWeights)+"\n");
					bestScore = iterationBestScore;
					bestWeights = iterationBestWeights;
					writerBest.flush();
				}

				/*
				 * Print the progress
				 */
				System.out.println(n + "/" + duration + " : " + iterationBestScore);

				/*
				 * If we have a good enough score we stop it
				 */
				if (iterationBestScore >= maxLine) {
					break;
				}
			}

			writer.close();
			return bestWeights;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error();
		}

	}

}
