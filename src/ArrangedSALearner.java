package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Random;

import src.PlayerSkeleton.TetrisSolver;

public final class ArrangedSALearner implements TetrisLearner {

	private final Random random = new Random();

	@Override
	public float[] learn(TetrisSolver solver, int duration, int maxLine, int averageGamePlayed,
			float[] startingWeights) {

		if(solver.weightsLength() != startingWeights.length){
			throw new IllegalArgumentException("You should enter "+solver.weightsLength()+" weights instead of "+startingWeights.length );
		}
		
		/*
		 * Open 2 files, 
		 * 	1 for all the data, easier for us to understand what happen
		 *  1 for the best weigths only, so it's easier to find the right result
		 */
		try (Writer writer = new BufferedWriter(new FileWriter("data.csv", false));
				Writer writerBest = new BufferedWriter(new FileWriter("bestRes.txt", false))) {

			
			/*
			 * Initialization
			 */
			//current weights
			float[] weights = startingWeights.clone();
			//current score corresponding to the weights
			int value = TetrisLearner.solveAvg(solver, weights, maxLine, averageGamePlayed, 0);
			//best value find so far
			int best = value;

			//Initialize the file header
			for (int i = 0; i < weights.length; i++) {
				writer.write("w" + i + ";");
			}
			writer.write("next Value; value;\n");
			
			
			/*
			 * Simulated Annealing
			 * 		using n cycles
			 */
			for (int n = 0; n < duration; n++) {
				
				/*
				 * Set the next weights. 
				 * Change randomly nParams weights where it
				 * starts changing 5 parameters and decrease the number of
				 * parameters changed the further we go.
				 */
				float[] next = weights.clone();
				int nParams = 5 - (int) (5 * n / duration);
				for (int i = 0; i < nParams; i++) {
					int ind = random.nextInt(solver.weightsLength());
					boolean sign = random.nextInt(2) == 1;
					if (sign) {
						next[ind] += Math.exp(-Math.pow((double) n, 1.2) / duration);
					} else {
						next[ind] -= Math.exp(-Math.pow((double) n, 1.2) / duration); 
					}
				}
				
				/*
				 * Compute the score of the new weights
				 */
				int nextVal = TetrisLearner.solveAvg(solver, next, maxLine, averageGamePlayed, value);

				/*
				 * Write data in file
				 */
				for (float w : next) {
					writer.write(w + ";");
				}
				writer.write(nextVal + ";" + value + "\n");
				writer.flush();

				/*
				 * change the value and weights if the score is better
				 */
				if (nextVal > value) {
					value = nextVal;
					weights = next.clone();
				} else {
					/*
					 * change value and weights with some probability that decrease as time goes
					 */
					if (value > 30) {
						double r = random.nextDouble();
						double proba = Math.exp(
								-(double) (Math.pow((double) (value - nextVal + 1) / value, 3.1) * Math.pow(n, 1.2)));
						if (proba > 0.5) {
							proba = 0.5;
						}
						if (r < proba) {
							value = nextVal;
							weights = next.clone();
						}
					}
				}
				
				/*
				 * If the value is the best found so far write it on a file
				 */
				if (best < value) {
					writerBest.write(Arrays.toString(weights)+"\n");
					best = value;
					writerBest.flush();
				}

				/*
				 * Print the progress
				 */
				System.out.println(n + "/" + duration + " : " + value);
				
				/*
				 * If we have the best score we stop it
				 */
				if (value >= maxLine) {
					break;
				}
			}

			writer.close();
			return weights;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error();
		}

	}

}
