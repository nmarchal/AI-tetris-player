package src;

public class PlayerSkeleton {

	
	public static void main(String[] args) {
		State s = new State();
		new TFrame(s);
		TetrisSolver aI = new StartingSolver();
		while(!s.hasLost()) {
			s.makeMove(aI.pickMove(s,s.legalMoves()));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}
