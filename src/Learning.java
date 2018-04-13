package src;


public class Learning {

	public static void main(String[] args) {
		
		TetrisLearner learner = new ArrangedSALearner();
		float[] optimalW =learner.learn( PlayerSkeleton.EXPERIMENT_SOLVER, 10000, 400_000, 8);
		System.out.println("END :");
		for(float w:optimalW){
			System.out.println(w + "f , ");
		}
	}

}
