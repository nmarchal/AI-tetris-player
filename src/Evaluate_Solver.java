package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;

import src.PlayerSkeleton.GivenHeuristic;
import src.PlayerSkeleton.MinMaxSolver;
import src.PlayerSkeleton.TetrisSolver;

public class Evaluate_Solver {
	/**
	 * MAIN FUNCTION
	 * 
	 * @param args
	 */
	private static final boolean APPEND = false;

	public static void main(String[] args) {
		try (Writer writer = new BufferedWriter(new FileWriter("performances.csv", APPEND))) {

			final long MAX_TIME_PLAYING = 900_000;
			final long NAVG = 1000;

			System.out.println("Start:");
			if (!APPEND) {
				writer.write("AI;Heuristic;score;time;average time for 100 moves;lost?;moves\n");
			}
			while (true) {

				long time, avg;

				/*
				 * AI 1: BASIC with given heuristic
				 */
				//System.out.println("\nBasic_solver --> Basic Heuristic:");
				PlayerSkeleton.TetrisSolver aI = PlayerSkeleton.IMPROVED_MINMAX_SOLVER;
				State s = new State();
				long startingTime = System.currentTimeMillis();
				while (!s.hasLost()) {
					s.makeMove(aI.pickMove(s, s.legalMoves(), PlayerSkeleton.BEST_WEIGHTS_IMPROVED));
				}
				time = System.currentTimeMillis() - startingTime;
				avg = time * NAVG / s.getTurnNumber();
				System.out.println("END :\n\tscore =\t" + s.getRowsCleared() + "\n\ttime =\t" + time
						+ "\n\taverageTimePerTurn =\t" + avg);
				writer.write("MinMax;Improved;" + s.getRowsCleared() + ";" + time + ";" + avg + ";" + s.hasLost()+ ";" + s.getTurnNumber()+ "\n");
				writer.flush();

//				/*
//				 * AI 2: BASIC WITH IMPROVED
//				 */
//				System.out.println("\nBasic_solver --> IMPROVED Heuristic:");
//				aI = PlayerSkeleton.IMPROVED_BASIC_SOLVER;
//				s = new State();
//				startingTime = System.currentTimeMillis();
//				while (!s.hasLost() && MAX_TIME_PLAYING > System.currentTimeMillis() - startingTime) {
//					s.makeMove(aI.pickMove(s, s.legalMoves(), PlayerSkeleton.BEST_WEIGHTS_IMPROVED));
//				}
//				time = System.currentTimeMillis() - startingTime;
//				avg = time * NAVG / s.getTurnNumber();
//				writer.write(
//						"Basic;Improved;" + s.getRowsCleared() + ";" + time + ";" + avg + ";" + s.hasLost() + "\n");
//				System.out.println("END :\n\tscore =\t" + s.getRowsCleared() + "\n\ttime =\t" + time
//						+ "\n\taverageTimePerTurn =\t" + avg);
//				writer.flush();
//
//				/*
//				 * AI 3: MinMax depth 1 with improved == Basic without
//				 * parallelism
//				 */
//				System.out.println("\nBasic_solver_No_para --> improved Heuristic:");
//				aI = new PlayerSkeleton.MinMaxSolver(new GivenHeuristic(), 1);
//				s = new State();
//				startingTime = System.currentTimeMillis();
//				while (!s.hasLost() && MAX_TIME_PLAYING > System.currentTimeMillis() - startingTime) {
//					s.makeMove(aI.pickMove(s, s.legalMoves(), PlayerSkeleton.BEST_WEIGHTS));
//				}
//				time = System.currentTimeMillis() - startingTime;
//				avg = time * NAVG / s.getTurnNumber();
//				writer.write("Basic No parallelism;Given;" + s.getRowsCleared() + ";" + time + ";" + avg + ";"
//						+ s.hasLost() + "\n");
//				System.out.println("END :\n\tscore =\t" + s.getRowsCleared() + "\n\ttime =\t" + time
//						+ "\n\taverageTimePerTurn =\t" + avg);
//				writer.flush();

//				/*
//				 * AI 4: Minmax depth 2 given heuristic
//				 */
//				System.out.println("\nMinMax_solver depth 2 --> Basic Heuristic:");
//				aI = PlayerSkeleton.MINMAX_SOLVER;
//				s = new State();
//				startingTime = System.currentTimeMillis();
//				while (!s.hasLost() && MAX_TIME_PLAYING > System.currentTimeMillis() - startingTime) {
//					s.makeMove(aI.pickMove(s, s.legalMoves(), PlayerSkeleton.BEST_WEIGHTS));
//				}
//				time = System.currentTimeMillis() - startingTime;
//				avg = time * NAVG / s.getTurnNumber();
//				writer.write("Minmax2;Given;" + s.getRowsCleared() + ";" + time + ";" + avg + ";" + s.hasLost() + "\n");
//				System.out.println("END :\n\tscore =\t" + s.getRowsCleared() + "\n\ttime =\t" + time
//						+ "\n\taverageTimePerTurn =\t" + avg);
//				writer.flush();
//				
//				/*
//				 * AI 5: Minmax depth 3 given heuristic
//				 */
//				System.out.println("\nMinMax_solver depth 3 --> Basic Heuristic:");
//				 aI = PlayerSkeleton.DEEPER_MINMAX_SOLVER;
//				s = new State();
//				startingTime = System.currentTimeMillis();
//				while (!s.hasLost() && MAX_TIME_PLAYING>System.currentTimeMillis()-startingTime) {
//					s.makeMove(aI.pickMove(s, s.legalMoves(), PlayerSkeleton.BEST_WEIGHTS));
//				}
//				time = System.currentTimeMillis()-startingTime;
//				avg = time*NAVG/s.getTurnNumber();
//				writer.write("Minmax3;Given;"+s.getRowsCleared()+";"+time+";"+avg+";"+s.hasLost()+"\n");
//				System.out.println("END :\n\tscore =\t" + s.getRowsCleared() + "\n\ttime =\t" + time+"\n\taverageTimePerTurn =\t"+avg);			
//				writer.flush();
//				
//				/*
//				 * AI 6: MinMax depth 4 given
//				 */
//				System.out.println("\nMinMax_solver depth 4 --> Basic Heuristic:");
//				 aI = new PlayerSkeleton.MinMaxSolver(new GivenHeuristic(), 4);
//				s = new State();
//				startingTime = System.currentTimeMillis();
//				while (!s.hasLost() && MAX_TIME_PLAYING>System.currentTimeMillis()-startingTime) {
//					s.makeMove(aI.pickMove(s, s.legalMoves(), PlayerSkeleton.BEST_WEIGHTS));
//				}
//				time = System.currentTimeMillis()-startingTime;
//				avg = time*NAVG/s.getTurnNumber();
//				writer.write("MinMax4;Given;"+s.getRowsCleared()+";"+time+";"+avg+";"+s.hasLost()+"\n");
//				System.out.println("END :\n\tscore =\t" + s.getRowsCleared() + "\n\ttime =\t" + time+"\n\taverageTimePerTurn =\t"+avg);			
//				writer.flush();
//				
//				/*
//				 * AI 7: Minmax depth 2 given heuristic
//				 */
//				System.out.println("\nMinMax_solver depth 2 --> Improved Heuristic:");
//				 aI = PlayerSkeleton.IMPROVED_MINMAX_SOLVER;
//				s = new State();
//				startingTime = System.currentTimeMillis();
//				while (!s.hasLost() && MAX_TIME_PLAYING>System.currentTimeMillis()-startingTime) {
//					s.makeMove(aI.pickMove(s, s.legalMoves(), PlayerSkeleton.BEST_WEIGHTS_IMPROVED));
//				}
//				time = System.currentTimeMillis()-startingTime;
//				avg = time*NAVG/s.getTurnNumber();
//				writer.write("Minmax2 noPruning;Improved;"+s.getRowsCleared()+";"+time+";"+avg+";"+s.hasLost()+"\n");
//				System.out.println("END :\n\tscore =\t" + s.getRowsCleared() + "\n\ttime =\t" + time+"\n\taverageTimePerTurn =\t"+avg);			
//				writer.flush();
//				
//				/*
//				 * AI 8: Minmax depth 3 given heuristic
//				 */
//				System.out.println("\nMinMax_solver depth 3 --> Improved Heuristic:");
//				 aI = PlayerSkeleton.IMPROVED_DEEPER_MINMAX_SOLVER;
//				s = new State();
//				startingTime = System.currentTimeMillis();
//				while (!s.hasLost() && MAX_TIME_PLAYING>System.currentTimeMillis()-startingTime) {
//					s.makeMove(aI.pickMove(s, s.legalMoves(), PlayerSkeleton.BEST_WEIGHTS_IMPROVED));
//				}
//				time = System.currentTimeMillis()-startingTime;
//				avg = time*NAVG/s.getTurnNumber();
//				writer.write("Minmax3 noPruning;Improved;"+s.getRowsCleared()+";"+time+";"+avg+";"+s.hasLost()+"\n");
//				System.out.println("END :\n\tscore =\t" + s.getRowsCleared() + "\n\ttime =\t" + time+"\n\taverageTimePerTurn =\t"+avg);			
//				writer.flush();
//				
//				/*
//				 * AI 9: MinMax depth 4 given
//				 */
//				System.out.println("\nMinMax_solver depth 4 --> Improved Heuristic:");
//				 aI = new PlayerSkeleton.MinMaxSolver(new PlayerSkeleton.ImprovedHeuristics(), 4);
//				s = new State();
//				startingTime = System.currentTimeMillis();
//				while (!s.hasLost() && MAX_TIME_PLAYING>System.currentTimeMillis()-startingTime) {
//					s.makeMove(aI.pickMove(s, s.legalMoves(), PlayerSkeleton.BEST_WEIGHTS_IMPROVED));
//				}
//				time = System.currentTimeMillis()-startingTime;
//				avg = time*NAVG/s.getTurnNumber();
//				writer.write("MinMax4 noPruning;Improved;"+s.getRowsCleared()+";"+time+";"+avg+";"+s.hasLost()+"\n");
//				System.out.println("END :\n\tscore =\t" + s.getRowsCleared() + "\n\ttime =\t" + time+"\n\taverageTimePerTurn =\t"+avg);	
//				writer.flush();
//				
//				
				
			}	
		}catch(Exception e){
			e.printStackTrace();
			throw new Error();
		}
	}
}
