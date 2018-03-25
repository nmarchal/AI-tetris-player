package src;

import java.util.Random;

import src.PlayerSkeleton.TetrisSolver;

public final class ArrangedSALearner implements TetrisLearner {
	
	private final Random random = new Random();	

	@Override
	public float[] learn(TetrisSolver solver, int duration, int maxLine) {
		float[] weights = new float[solver.weightsLength()];
		
		int value = TetrisLearner.solveAvg(solver, weights , maxLine,10);//FIXME 10 = number of time played
		for(int n =0;n<duration ;n++){
			float[] next = weights.clone();
			float t = schedule(n,duration);
			int nParams = 5;
			for(int i =0;i<nParams; i++){
				int ind = random.nextInt(solver.weightsLength());
				boolean sign = random.nextInt(2)==1;
				if(sign){
					next[ind] += 1;
				}else{
					next[ind] -= 1;
				}
			}
			int nextVal =  TetrisLearner.solveAvg(solver, next , maxLine,20);
			if(nextVal > value){
				value = nextVal;
				weights = next.clone();
			}/*else{
				double r = random.nextDouble();
				if(r < Math.exp((-5*n)/duration)){
					value = nextVal;
					weights = next.clone();
				}
			}*/
			System.out.println(n+"/"+duration+" : "+value);
			if(value >= maxLine){
				break;
			}
		}
		
		return weights;
	}
	
	private float schedule(int t,int duration){
		return duration-t;
	}

}
