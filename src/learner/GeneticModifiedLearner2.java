package learner;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

import agent.TetrisSolver;
import javafx.util.Pair;

public class GeneticModifiedLearner2 implements TetrisLearner {

	private static final Random random = new Random();
	private final int populationSize;
	
	public GeneticModifiedLearner2(int population) {
		this.populationSize = population;
	}
	
	@Override
	public float[] learn(TetrisSolver solver, int duration, int maxLine, int averageGamePlayed,
			float[] startingWeights) {
		
		if(solver.weightsLength() != startingWeights.length){
			throw new IllegalArgumentException("You should enter "+solver.weightsLength()+" weights instead of "+startingWeights.length );
		}
		
		
		/*
		 * Initialize population
		 */
		LinkedList<Pair<Integer, float[]>> population = new LinkedList<>();
		for(int i=0; i<populationSize; i++ ){
			float[] w = generateState(solver.weightsLength());
			int value = TetrisLearner.solveAvg(solver, w , maxLine, averageGamePlayed, 0);
			Pair<Integer, float[]> pair = new Pair<>(value,w);
			population.add(pair);
		}
		Comparator<Pair<Integer, float[]>> bestVal = (p1,p2)->Integer.compare(p2.getKey(),p1.getKey());
		population.sort(bestVal);
		
		/*
		 * Start simulation
		 */
		for(int n =0; n<duration; n++){
			LinkedList<Pair<Integer, float[]>> nextGen = new LinkedList<>();
			
			
			boolean male = true;
			float[] last = null;
			for(Pair<Integer,float[]> p :population){
				if(male){
					last = p.getValue();
					male = false;
				}else{
					male = true;
					float[] w = new float[solver.weightsLength()];
					/*
					 * CrossOver
					 */
					for(int i =0;i<solver.weightsLength();i++){
						w[i] = (last[i]+p.getValue()[i])/2f;
					}
					float[] w1 = w.clone();
					/*
					 * Mutation
					 */
					int mutationIndex = random.nextInt(solver.weightsLength()) ;
					w1[mutationIndex] = random.nextFloat()-0.5f;
					
					/*
					 * Fitness evaluation
					 */
					int value1 = TetrisLearner.solveAvg(solver, w1, maxLine, averageGamePlayed, 0);
					int value2 = TetrisLearner.solveAvg(solver, w , maxLine, averageGamePlayed, 0);
					Pair<Integer, float[]> pair1 = new Pair<>(value1,w1);
					Pair<Integer, float[]> pair2 = new Pair<>(value2,w);
					nextGen.add(pair1);
					nextGen.add(pair2);
				}
			}
			
			/*
			 * Fitness selection
			 */
			population.addAll(nextGen);
			population.sort(bestVal);
			population.subList(populationSize,population.size()).clear();
			
			System.out.println(n+"/"+duration+" : "+population.getFirst().getKey());
		}
		
		return population.getFirst().getValue();
	}

	
	
	/**
	 * @param n number of weights
	 * @return a random set of weights
	 */
	private static final float[] generateState(int n){
		float[] weights = new float[n];
		for(int i =0; i<n; i++){
			weights[i] = random.nextFloat()-0.5f;
		}
		return weights;
	}
	
}
