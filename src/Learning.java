package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;

public class Learning {

	public static void main(String[] args) {

		try(Writer writer = new BufferedWriter(new FileWriter("count.csv", false))){}
		catch(Exception e){
			throw new Error();
		}
		
		
		TetrisLearner learner = new ArrangedSALearner(3500);
		float[] optimalW = learner.learn(PlayerSkeleton.IMPROVED_BASIC_SOLVER, 10000, 200_000, 12, PlayerSkeleton.BEST_WEIGHTS_FINAL);
		System.out.println("END :");
		for (float w : optimalW) {
			System.out.println(w + "f , ");
		}
	}

}
