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
		
		int value = TetrisLearner.solveAvg(solver, weights , maxLine,averageGamePlayed);
		for(int n =0;n<duration ;n++){
			float[] next = weights.clone();
			// float t = schedule(n,duration);
			int nParams = 5 - (int)( 5*n/duration);
			for(int i =0;i<nParams; i++){
				int ind = random.nextInt(solver.weightsLength());
				boolean sign = random.nextInt(2)==1;
				if(sign){
					next[ind] += 1;
				}else{
					next[ind] -= 1;
				}
			}
			int nextVal =  TetrisLearner.solveAvg(solver, next , maxLine,averageGamePlayed);
			writer.write(nextVal + ",");
			if(nextVal > value){
				value = nextVal;
				weights = next.clone();
			}else{
				if (value > 200) {
					double r = random.nextDouble();
					if(r < Math.exp(-Math.sqrt((value - nextVal)/value) * Math.pow(n, 1.3))/(duration - n)){
						value = nextVal;
						weights = next.clone();
						System.out.println("r = " + r);
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
			throw new Error();
		}
		
	}
	
	//private float schedule(int t,int duration){
		//return duration-t;
	//}

}
