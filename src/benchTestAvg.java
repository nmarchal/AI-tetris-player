package src;

public class benchTestAvg {

	public static void main(String[] args) {
		
		final int N = 4;
		
		final PlayerSkeleton.TetrisSolver ai = new PlayerSkeleton.MinMaxSolver(new PlayerSkeleton.GivenHeuristic(),1);
		for(int i = 0;i<10;i++){
			System.out.println("\nParrallel");
			long start = System.currentTimeMillis();
			int score = TetrisLearner.solveAvg(ai, PlayerSkeleton.BEST_WEIGHTS, 100_000, N, 0);
			long end = System.currentTimeMillis();
			System.out.println("AVG = "+score+" : "+(end-start)/1000 +" seconds");
		
		
			System.out.println("\n\nNON Parrallel");
			start = System.currentTimeMillis();
			int tot = 0;
			for(int game =0;game < N; game++){
				 tot+=TetrisLearner.runStateTillCompletion(ai, new State(), 100_000, PlayerSkeleton.BEST_WEIGHTS);
			}
			score = tot/N;
			end = System.currentTimeMillis();
			System.out.println("AVG = "+score+" : "+(end-start)/1000 +" seconds");
		}
		

	}

}
