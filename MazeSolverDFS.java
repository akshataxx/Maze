/*
* c3309266
* Akshata Dhuraji
* COMP2230 Assignment
*/

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MazeSolverDFS {
	/**
	 * Main driver method of MazSolverDFS app
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		// validating number of arguments
		if (args.length != 1) {
			throw new IllegalArgumentException("Illegal number of command line arguments. 1 argument expected");
		}

		// scanning input file
		try (Scanner scanner = new Scanner(new File(args[0]))) {
			// creating maze
			Maze maze = new Maze(scanner.nextLine());
			// starting time count
			long start = System.currentTimeMillis();
			// solving maze
			List<Integer> result = maze.solve();
			// stopping time count
			long time = System.currentTimeMillis() - start;
			// outputting result path
			System.out.println("(" + result.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(",")) + ")");
			// showing path length
			System.out.println(result.size() - 1);
			// showing time used
			System.out.println(time);
		}
		catch (IOException ignored) {
			throw new IllegalArgumentException("Can not open input file");
		}
	}
}
