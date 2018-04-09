package src;


public class PSOLearning {

	public static void main(String[] args) {
		
		TetrisLearner learner = new PSOLearner(200,null);
		float[] optimalW =learner.learn(PlayerSkeleton.EXPERIMENT_SOLVER,
						1000,
						200_000,
						30,
						PlayerSkeleton.DUMB_EXPERIMENTAL_WEIGHTS);
		System.out.println("END :");
		for(float w:optimalW){
			System.out.println(w + "f , ");
		}
	}

}
