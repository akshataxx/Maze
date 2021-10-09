/*
* c3309266
* Akshata Dhuraji
* COMP2230 Assignment
*/

import java.io.IOException;
import java.io.PrintWriter;

public class MazeGenerator {
	/**
	 * Main driver method of MazeGenerator app
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		// validating number of command line arguments
		if (args.length != 3) {
			throw new IllegalArgumentException("Illegal number of command line arguments. 3 arguments expected");
		}

		// validating and parsing maze height
		int height;
		try {
			height = Integer.parseInt(args[0]);
		}
		catch (NumberFormatException ignored) {
			throw new IllegalArgumentException("Can not parse maze height. Positive integer expected");
		}

		// validating and parsing maze width
		int width;
		try {
			width = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException ignored) {
			throw new IllegalArgumentException("Can not parse maze width. Positive integer expected");
		}

		// generating maze with given parameters and writing it to file with given filename
		try (PrintWriter pw = new PrintWriter(args[2])) {
			Maze maze = new Maze(height, width);
			pw.println(maze);
		}
		catch (IOException ignored) {
			throw new IllegalArgumentException("Can not open output file");
		}
	}
}
