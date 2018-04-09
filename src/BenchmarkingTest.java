package src;


public class BenchmarkingTest {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		TetrisLearner learner = new ArrangedSALearner();
		float[] optimalW =learner.learn( PlayerSkeleton.BASIC_SOLVER,
						300,
						50000,
						30,
						PlayerSkeleton.BASICS_WEIGHTS);

		long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (endTime-startTime) + "ms");
		System.out.println("Num states evaluated: " + (PlayerSkeleton.BASIC_SOLVER.statesEvaluated));


		System.out.println("END :");
		for(float w:optimalW){
			System.out.println(w + "f , ");
		}
	}


}
