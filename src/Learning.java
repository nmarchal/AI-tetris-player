package src;


public class Learning {

	public static void main(String[] args) {
		
		TetrisLearner learner = new ArrangedSALearner();
		float[] optimalW =learner.learn( PlayerSkeleton.EXPERIMENT_SOLVER, 300, 200_000, 30, PlayerSkeleton.BEST_WEIGHTS_EXP);
		System.out.println("END :");
		for(float w:optimalW){
			System.out.println(w + "f , ");
		}
	}

}
