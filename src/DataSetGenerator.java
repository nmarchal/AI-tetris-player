/**
 * Routine to generate training data based on the currently very good set of weights we have.
 * @author Ryan
 */
package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Random;

import src.PlayerSkeleton.GivenHeuristic;
import src.PlayerSkeleton.MinMaxSolver;
import src.PlayerSkeleton.TetrisSolver;

public class DataSetGenerator {

	private static final Random random = new Random();

	public static void main (String args[]) {
		TetrisSolver solver = new MinMaxSolver(new GivenHeuristic(), 2);
		try (Writer writer = new BufferedWriter(new FileWriter("data.csv", true))) {
			for (int i = 0; i < 1000000; i++) {
				State s = new State();
				int chosenMove = solver.pickMove(s, s.legalMoves(), PlayerSkeleton.BEST_WEIGHTS);
				int numMoves = s.legalMoves().length;
				String stateString = Arrays.toString(solver.featureValues(s));
				// store the ideal move
				writer.write(stateString.substring(1, stateString.length() - 1) + "," + chosenMove + ",1");
				//generate random data points for wrong moves
				for (int j = 0; j < random.nextInt(numMoves); j++) {
					int wrongMove = random.nextInt(numMoves);
					if (wrongMove != chosenMove)
						writer.write(stateString.substring(1, stateString.length() - 1) + "," + wrongMove + ",0");
				}
				System.out.println("Generated data set " + i);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error();
		}
	}
}
