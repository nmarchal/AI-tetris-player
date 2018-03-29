package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Random;

import src.PlayerSkeleton.TetrisSolver;

public final class ArrangedSALearner implements TetrisLearner {
	
	private final Random random = new Random();	

	@Override
	public float[] learn(TetrisSolver solver, int duration, int maxLine, int averageGamePlayed) {

		try (Writer writer = new BufferedWriter(new FileWriter("data.csv",false))) {
		
		float[] weights = new float[solver.weightsLength()];
		for(int i =0; i<weights.length; i++){
			writer.write("w"+i+";");
		}
		writer.write("next Value; value;\n");
		
		int value = TetrisLearner.solveAvg(solver, weights , maxLine,averageGamePlayed,0);
		for(int n =0;n<duration ;n++){
			float[] next = weights.clone();
			// float t = schedule(n,duration);
			int nParams = 5 - (int)( 5*n/duration);
			for(int i =0;i<nParams; i++){
				int ind = random.nextInt(solver.weightsLength());
				boolean sign = random.nextInt(2)==1;
				float tolWeight = 0.1f;
				if(sign){
					next[ind] += Math.exp(-Math.pow((double)n, 1.2)/duration);
				}else{
					next[ind] -= 1 + ((float)n/duration)*(tolWeight - 1);
				}
			}
			int nextVal =  TetrisLearner.solveAvg(solver, next , maxLine,averageGamePlayed,value);
			
			/*
			 * Write data in file
			 */
			for(float w:next){
				writer.write(w+";");
			}
			writer.write(nextVal + ";"+value+"\n");
			
			/*
			 * 
			 */
			if(nextVal > value){
				value = nextVal;
				weights = next.clone();
			}else{
				if (value > 30) {
					double r = random.nextDouble();
					double proba =Math.exp(-(double)(Math.pow( (double) (value - nextVal+1)/value,3.1) * Math.pow(n, 1.2)));
					if (proba > 0.5) {
						proba = 0.5 ;
						}
					if(r < proba){
						value = nextVal;
						weights = next.clone();
					}
				}
			}
			System.out.println(n+"/"+duration+" : "+value);
			if(value >= maxLine){
				break;
			}
		}

		writer.close();
		return weights;
		}catch(Exception e) {
			e.printStackTrace();
			throw new Error();
		}
		
	}
	
	//private float schedule(int t,int duration){
		//return duration-t;
	//}

}
