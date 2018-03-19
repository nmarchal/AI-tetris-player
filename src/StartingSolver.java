package src;

import java.util.Arrays;

import com.sun.org.apache.bcel.internal.generic.FLOAD;

public class StartingSolver implements TetrisSolver{

	private final float weight0;
	private final float[] weightsColumnsHeights;
	private final float[] weightsDifferencesColumns;
	private final float weightMaximumHeight;
	private final float weightHoles;
	
	public StartingSolver() {
		this.weight0 =0;
		this.weightsColumnsHeights =  new float[State.COLS];
		this.weightsDifferencesColumns = new float[State.COLS-1];
		this.weightHoles =0;
		this.weightMaximumHeight =0;
	}
	
	public StartingSolver(float weight0 ,float[] weigthsColumnsHeights, float[] weightsDifferencesColumns,float weightMaximumHeight,float weightHoles){
		this.weight0 = weight0;
		this.weightsColumnsHeights = Arrays.copyOf(weigthsColumnsHeights, State.COLS);
		this.weightsDifferencesColumns = Arrays.copyOf(weightsDifferencesColumns, State.COLS-1);
		this.weightMaximumHeight = weightMaximumHeight;
		this.weightHoles = weightHoles;
	}
	
	@Override
	public int pickMove(State s, int[][] legalMoves) {
		float max = Float.NEGATIVE_INFINITY; 
		int bestMove=0;
		int n =0;
		for(int[] move: legalMoves){
			State next = s.nextState(move);
			float heuristic = weight0;
			int maxHeight =0;
			int holes =0;
			for(int i=0; i<State.COLS; i++){
				
				//height
				int height = next.getTop()[i];
				heuristic += weightsColumnsHeights[i]*height;
				if(height>maxHeight){
					maxHeight = height;
				}
				
				//holes
				for(int j=0; j<height-1;j++){
					if(next.getField()[j][i]==0){
						holes++;
					}
				}
			}
			heuristic += holes*weightHoles;
			heuristic += maxHeight*weightMaximumHeight;
			
			//differences
			for(int i=0; i<State.COLS-1; i++){
				int diff =Math.abs(next.getTop()[i]-next.getTop()[i+1]);
				
				heuristic += weightsDifferencesColumns[i]*diff;
			}
			
			if(next.lost){
				heuristic = Float.NEGATIVE_INFINITY;
			}
			
			if(heuristic>max){
				max = heuristic;
				bestMove = n;
			}
			n++;
		}
		
		return bestMove;
	}

}
