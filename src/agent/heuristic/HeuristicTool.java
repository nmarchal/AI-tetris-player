package src.agent.heuristic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import src.game.State;

/**
 * Tool interface containing useful functions used by the heuristics
 *
 */
public interface HeuristicTool {

	/**
	 * compute the number of filled tiles above every holes
	 * 
	 * @param s
	 *            the state of the game
	 * @return number of filled tiles above every holes
	 */
	public static int squaresAboveHoles(State s) {
		int count = 0;
		for (int c = 0; c < State.COLS; c++) {
			int end = s.getTop()[c];
			for (int r = 0; r < end; r++) {
				boolean isHole = s.getField()[r][c] == 0;
				if (isHole) {
					count += end - r;
					break;
				}
			}
		}
		return count;
	}

	/**
	 * count the number of grouped holes in the state
	 * 
	 * @param s
	 *            the state of the game
	 * @return number of grouped holes.
	 */
	public static int groupedHoles(State s) {
		int[][] grid = s.getField().clone();
		// fill up all non-holes

		// for each col, set all squares above to 1
		for (int c = 0; c < State.COLS; c++) {
			int start = s.getTop()[c];
			for (int r = start; r < State.ROWS; r++) {
				grid[r][c] = 1;
			}
		}

		// count number of hole groups (all connected holes make up 1 group)
		int numGroups = 0;
		for (int c = 0; c < State.COLS; c++) {
			for (int r = 0; r < s.getTop()[c]; r++) {
				boolean isNotHole = grid[r][c] > 0;
				if (isNotHole)
					continue;
				numGroups++;
				fillNeighbors(grid, r, c);
			}
		}

		return numGroups;
	}

	/**
	 * help function, that fill the neighbors of a given coordinate
	 * 
	 * @param grid
	 * @param y
	 *            coordinate y
	 * @param x
	 *            coordinate x
	 */
	public static void fillNeighbors(int[][] grid, int y, int x) {
		if (grid[y][x] == 0) {
			grid[y][x] = 1;

			// explore up, down, left, right recursively
			int left = x - 1;
			int right = x + 1;
			int down = y - 1;
			int up = y + 1;
			if (left >= 0)
				fillNeighbors(grid, y, left);
			if (right < grid[0].length)
				fillNeighbors(grid, y, right);
			if (down >= 0)
				fillNeighbors(grid, down, x);
			if (up < grid.length)
				fillNeighbors(grid, up, x);
		}
	}

	/**
	 * Compute the sum of heights
	 * 
	 * @param s
	 *            the state of the game
	 * @return the sum
	 */
	public static int sumOfHeights(State s) {
		int sum = 0;
		for (int i = 0; i < State.COLS; i++) {
			sum += s.getTop()[i];
		}
		return sum;
	}

	/**
	 * compute the difference between the highest column and the smallest
	 * 
	 * @param s
	 *            the state of the game
	 * @return
	 */
	public static int maxHeightsDifference(State s) {
		Integer[] box = new Integer[s.getTop().length];
		for (int i = 0; i < box.length; i++) {
			box[i] = s.getTop()[i];
		}
		List<Integer> tops = Arrays.asList(box);
		int highest = Collections.max(tops);
		int lowest = Collections.min(tops);
		return (highest - lowest);
	}

	public static int holes(State s) {
		int holes = 0;
		for (int i = 0; i < State.COLS; i++) {
			int height = s.getTop()[i];
			for (int j = 0; j < height - 1; j++) {
				if (s.getField()[j][i] == 0) {
					holes++;
				}
			}
		}
		return holes;
	}

}