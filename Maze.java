/*
* c3309266
* Akshata Dhuraji
* COMP2230 Assignment 
*/

import java.util.*;

public class Maze {
	// coordinates shift for each of directions
	private static final int[][] DIRS = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};

	// directions constants
	private static final int LEFT = 0;
	private static final int UP = 1;
	private static final int RIGHT = 2;
	private static final int DOWN = 3;

	/**
	 * Maze height
	 */
	private final int height;

	/**
	 * Maze width
	 */
	private final int width;

	/**
	 * 2D array of maze cells
	 */
	private final Node[][] graph;

	/**
	 * Start cell of maze
	 */
	private final Node startNode;

	/**
	 * Finish node of maze
	 */
	private final Node finishNode;

	/**
	 * Constructor for parsing maze from string
	 * @param s string to parse maze from
	 */
	public Maze(String s) {
		// separating string into parts with delimiter '
		String[] parts = s.split(":");

		// there must be 4 parts
		if (parts.length != 4) {
			throw new IllegalArgumentException("Invalid number of delimiters ':'");
		}

		// first part contains height and width, separated with ','
		String[] parts2 = parts[0].split(",");
		// there must be 2 parts after separating
		if (parts2.length != 2) {
			throw new IllegalArgumentException("Invalid number of delimiters ','");
		}

		// parsing height
		height = Integer.parseInt(parts2[0]);

		// parsing width
		width = Integer.parseInt(parts2[1]);

		// creating cells 2D-array
		graph = new Node[height][width];
		for (int i = 0; i<height; i++) {
			for (int j = 0; j<width; j++) {
				graph[i][j] = new Node(i, j);
			}
		}

		// parsing index of start cell
		int startIndex = Integer.parseInt(parts[1]);

		// getting start cell from 2D array by index
		startNode = graph[(startIndex-1) / width][(startIndex-1) % width];

		// parsing index of finish cell
		int finishIndex = Integer.parseInt(parts[2]);

		// getting finish cell from 2D array by index
		finishNode = graph[(finishIndex-1) / width][(finishIndex-1) % width];

		// iterating over cell codes in the last part of string
		for (int i = 0; i<parts[3].length(); i++) {
			// getting current symbol
			char c = parts[3].charAt(i);
			// calculating row from current index
			int row = i / width;
			// calculating column from current width
			int column = i % width;

			// transforming char digit to int
			int code = c - '0';

			// if code is odd, it means, that right door for current cell is open
			if (code % 2 == 1) {
				graph[row][column].neighbours[RIGHT] = graph[row][column+1];
				graph[row][column+1].neighbours[LEFT] = graph[row][column];
			}

			// if code is >= 2, it means, that down door for current cell is open
			if (code / 2 == 1) {
				graph[row][column].neighbours[DOWN] = graph[row+1][column];
				graph[row+1][column].neighbours[UP] = graph[row][column];
			}
		}
	}

	/**
	 * Constructor for creating maze with given height, width and start cell.
	 * If startIndex == 0, start node is selected randomly
	 * @param height of maze to create
	 * @param width of maze to create
	 * @param startIndex index of start cell. If equals 0, start is selected randomly
	 */
	public Maze(int height, int width, int startIndex) {
		// validating sizes
		if (height < 2 || width < 2) {
			throw new IllegalArgumentException("Invalid size");
		}

		// validating start index
		if (startIndex < 0 || startIndex > height * width) {
			throw new IllegalArgumentException("Invalid start");
		}

		// assigning size parameters
		this.height = height;
		this.width = width;

		// selecting start index if it equals 0
		Random random = new Random();
		if (startIndex == 0) {
			startIndex = random.nextInt(height * width) + 1;
		}

		// creating cells 2D-array
		graph = new Node[height][width];
		for (int i = 0; i<height; i++) {
			for (int j = 0; j<width; j++) {
				graph[i][j] = new Node(i, j);
			}
		}

		// getting start cell from 2D array by index
		startNode = graph[(startIndex - 1) / width][(startIndex - 1) % width];

		// creating collection of visited cells. It will be used in mage generation procedure
		List<Node> visited = new ArrayList<>();
		// adding start cell to visited collection
		visited.add(startNode);

		// generating maze, using DFS algorithm
		buildMaze(startNode, visited);

		// finish node is last visited node
		finishNode = visited.get(visited.size()-1);
	}

	/**
	 * Constructor for creating maze with given height, width and random start cell.
	 * @param height of maze to create
	 * @param width of maze to create
	 */
	public Maze(int height, int width) {
		this(height, width, 0);
	}

	/**
	 * Method for finding path from start cell to finish cell in maze using DFS approach
	 * @return list of cell indices in order of visiting
	 */
	public List<Integer> solve() {
		// creating collection of visited cells.
		List<Node> visited = new ArrayList<>();
		// adding start cell to visited collection
		visited.add(startNode);

		// creating list of passed indices
		List<Integer> path = new ArrayList<>();

		// calling recursive procedure from start node with initialized visited and path variables
		solve(startNode, visited, path);

		// returning result path
		return path;
	}

	/**
	 * Helper recursive method
	 * @param node to continue DFS from
	 * @param visited current collection of visited cells
	 * @param path current list of visited cell indices
	 * @return true, if solution is already found, false - otherwise
	 */
	private boolean solve(Node node, Collection<Node> visited, List<Integer> path) {
		// adding current cell index to path
		path.add(node.index);
		// if current cell is finish cell, returning true
		if (node.index == finishNode.index) {
			return true;
		}

		// generating list of dirs, containing all directions
		List<Integer> dirsList = new ArrayList<>();
		for (int i = 0; i<DIRS.length; i++) {
			dirsList.add(i);
		}
		// shuffling generated list
		Collections.shuffle(dirsList);

		// trying to make move in each direction from dir list
		for (int dir : dirsList) {
			// checking, if current cell has open door in that direction
			if (node.neighbours[dir] != null) {
				// if there is an open door, getting the neighbor cell of that direction
				Node next = node.neighbours[dir];
				// checking, that neighbor cell is not visited yet
				if (!visited.contains(next)) {
					// adding this not-visited cell to visited collection.
					visited.add(next);
					// continuing dfs from this new cell
					boolean result = solve(next, visited, path);
					// if dfs, continued with new cell was successful, returning true
					if (result) {
						return true;
					}
					// the dfs from new cell, was failed - returning to current cell
					path.add(node.index);
				}
			}
		}
		// all directions of current cell were failed - returning false
		return false;
	}

	/**
	 * Helper method for generating maze using DFS approach
	 * @param node current cell
	 * @param visited current collection of visited cells
	 */
	private void buildMaze(Node node, Collection<Node> visited) {
		// generating list of dirs, containing all directions
		List<Integer> dirsList = new ArrayList<>();
		for (int i = 0; i<DIRS.length; i++) {
			dirsList.add(i);
		}
		// shuffling generated list
		Collections.shuffle(dirsList);

		// trying to open door in each direction from dir list
		for (int dir : dirsList) {
			// getting neighbor row
			int r = node.row + DIRS[dir][0];
			// getting neighbor column
			int c = node.column + DIRS[dir][1];

			// checking, that neighbor row and column are in range
			if (r >= 0 && r < height && c >= 0 && c < width) {
				// getting neighbor cell
				Node next = graph[r][c];
				// checking, that neighbor cell is not visited yet
				if (!visited.contains(next)) {
					// adding this not-visited cell to visited collection.
					visited.add(next);
					// opening appropriate door of current cell and appropriate door of neighbor cell
					node.neighbours[dir] = next;
					next.neighbours[2 * (1 - dir / 2) + dir % 2] = node;

					// continue build maze from neighbor cell
					buildMaze(next, visited);
				}
			}
		}
	}

	/**
	 * Overridden method for generating code string for maze
	 * @return code string of this maze
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// appending maze size
		builder.append(height).append(',')
				.append(width).append(':')
				// appending start cell index
				.append(startNode.index).append(':')
				// appending finish cell index
				.append(finishNode.index).append(':');

		// for each cell of maze appending its neighbor code
		for (int i = 0; i<height; i++) {
			for (int j = 0; j<width; j++) {
				builder.append(graph[i][j].getNeighboursCode());
			}
		}
		// returning result string
		return builder.toString();
	}

	/**
	 * Nested class, representing cell of the maze
	 */
	private class Node {
		/**
		 * Cell row
		 */
		final int row;

		/**
		 * Cell column
		 */
		final int column;

		/**
		 * Array of cell neighbors
		 */
		final Node[] neighbours;

		/**
		 * Index of cell
		 */
		final int index;

		/**
		 * Constructor, creating cell of given row and column with empty neighbors list
		 * @param row of cell to create
		 * @param column of cell to create
		 */
		Node(int row, int column) {
			// assigning row
			this.row = row;

			// assigning column
			this.column = column;

			// calculating and assignning index
			this.index = width * row + column + 1;

			// creating empty array for neighbors
			this.neighbours = new Node[DIRS.length];
		}

		/**
		 * Method for getting neighbors code of this cell
		 * @return neighbors code
		 */
		public int getNeighboursCode() {
			// no doors - 0
			// only right door - 1
			// only down door - 2
			// both doors - 3
			int down = neighbours[DOWN] != null ? 1 : 0;
			int right = neighbours[RIGHT] != null ? 1 : 0;
			return 2*down + right;
		}

		/**
		 * Overridden equals method.
		 * @param o other cell
		 * @return true, iff two cells have the same index
		 */
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Node node = (Node) o;
			return index == node.index;
		}

		/**
		 * Overridden hashCode method
		 * @return hash value
		 */
		@Override
		public int hashCode() {
			return Objects.hash(index);
		}
	}
}
