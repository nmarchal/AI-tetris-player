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
	public static void main(String[] args) {
		try(Writer writer = new BufferedWriter(new FileWriter("performances.csv",false))){

			TetrisSolver aI = new MinMaxSolver(new GivenHeuristic(), 2);
			int test_number = 0;
			while(true){
				State s = new State();
				int i =1000;
				
				while (!s.hasLost()) {
					s.makeMove(aI.pickMove(s, s.legalMoves(), PlayerSkeleton.BEST_WEIGHTS));
					if(i < s.getRowsCleared() ){
						System.out.println(s.getRowsCleared());
						i+= 1000;
					}
				}
				
				System.out.println("Test "+test_number+" finished with "+ s.getRowsCleared()+" rows cleared\n\n");
				writer.write(s.getRowsCleared()+"\n");
				writer.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Error();
		}
	}
}
