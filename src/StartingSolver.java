package src;

import java.util.Arrays;

public class StartingSolver implements TetrisSolver{

	private final float[] weightsColumnsHeights;
	private final float[] weightsDifferencesColumns;
	private final float weightMaximumHeight;
	private final float weightHoles;
	
	public StartingSolver() {
		this.weightsColumnsHeights =  new float[State.COLS];
		this.weightsDifferencesColumns = new float[State.COLS-1];
		this.weightHoles =0;
		this.weightMaximumHeight =0;
	}
	
	public StartingSolver(float[] weigthsColumnsHeights, float[] weightsDifferencesColumns,float weightMaximumHeight,float weightHoles){
		this.weightsColumnsHeights = Arrays.copyOf(weigthsColumnsHeights, State.COLS);
		this.weightsDifferencesColumns = Arrays.copyOf(weightsDifferencesColumns, State.COLS-1);
		this.weightMaximumHeight = weightMaximumHeight;
		this.weightHoles = weightHoles;
	}
	
	@Override
	public int pickMove(State s, int[][] legalMoves) {
		
		return 0;
	}

}
