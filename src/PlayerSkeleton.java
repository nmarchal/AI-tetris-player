package src;

public class PlayerSkeleton {
	public static final StartingSolver DUMB_SOLVER = new StartingSolver();
	public static final RandomSolver RANDOM_SOLVER = new RandomSolver();
	public static final float[] weigthsCols = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static final float[] weigthsDiffs = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public static final StartingSolver BASIC_SOLVER = new StartingSolver(1, weigthsCols,weigthsDiffs, -1, -50);

	
	public static void main(String[] args) {
		State s = new State();
		new TFrame(s);
		TetrisSolver aI = BASIC_SOLVER;
		while(!s.hasLost()) {
			s.makeMove(aI.pickMove(s,s.legalMoves()));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}
