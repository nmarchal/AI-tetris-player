package main;

import src.agent.TetrisSolver;
import src.agent.heuristic.ImprovedHeuristics;
import src.learner.ArrangedSALearner;
import src.learner.TetrisLearner;

public class Learning {

	public static void main(String[] args) {
				
		TetrisLearner learner = new ArrangedSALearner(3500);
		
		float[] optimalW = learner.learn(TetrisSolver.IMPROVED_BASIC_SOLVER, 10000, 200_000, 12, ImprovedHeuristics.BEST_WEIGHTS_FINAL);
		System.out.println("END :");
		for (float w : optimalW) {
			System.out.println(w + "f , ");
		}
	}

}
