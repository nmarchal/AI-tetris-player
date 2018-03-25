package src;


public class Learning {

	public static void main(String[] args) {
		
		TetrisLearner learner = new ArrangedSALearner();
		float[] optimalW =learner.learn( PlayerSkeleton.BASIC_SOLVER, 3000, 5000, 30);
		System.out.println("END :");
		for(float w:optimalW){
			System.out.println(w + "f , ");
		}
	}

}
