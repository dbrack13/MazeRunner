package maze;

import java.util.Random;
import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Graph;

/**
 * This class provides functionality to solve a maze using graph-based pathing.
 * It supports bot breadth-first search (BFS) and depth-first search (DFS) for
 * finding paths between the start point and end points. It generates a random,
 * solvable maze of specified rows and columns, with guaranteed connectivity
 * from the starting cell at the top-left corner to the ending cell at the
 * bottom-right corner. The class works by converting the maze into a graph
 * where each cell is represented as a node, and edges connect adjacent walkable
 * cells.
 *
 * @author Manzel Kiinu + Darrin Bracken
 */
public class Maze {
	// Enum to create difficulty for the maze, size and wall percentage
	public enum Difficulty {
		EASY(10, 10), // Easy maze dimensions
		MEDIUM(20, 20), // Medium maze dimensions
		HARD(30, 30); // Hard maze dimensions

		private final int rows;
		private final int cols;

		// Constructor
		Difficulty(int rows, int cols) {
			this.rows = rows;
			this.cols = cols;
		}

		// Getter for rows
		public int getRows() {
			return rows;
		}

		// Getter for cols
		public int getCols() {
			return cols;
		}
	}

	private int rows;
	private int cols;
	private boolean[][] grid;
	private Difficulty difficulty;

	/**
	 * Constructs the Maze and Initializes the difficulty, rows, cols, and generates
	 * a solveable maze
	 *
	 * @param difficulty The difficulty of the maze.
	 */
	public Maze(Difficulty difficulty) {
		this.difficulty = difficulty; // Initialize the difficulty
		this.rows = difficulty.getRows();
		this.cols = difficulty.getCols();
		this.grid = new boolean[rows][cols];
		generateSolvableMaze();
	}

	/**
	 * Initializes the difficulty of the maze and adjusts rows and cols, and
	 * regenerates maze based on the new difficulty
	 *
	 * @param difficulty
	 */
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
		this.rows = difficulty.getRows(); // Adjust rows based on difficulty
		this.cols = difficulty.getCols(); // Adjust cols based on difficulty
		this.grid = new boolean[rows][cols]; // Reinitialize the grid
		generateSolvableMaze(); // Regenerate the maze based on the new difficulty
	}

	/**
	 * Repeatedly generates a random maze until a solvable one is created. Ensures
	 * there is a valid path from the start cell (0, 0) to the end cell (rows-1,
	 * cols-1).
	 */
	public void generateSolvableMaze() {
		Random rand = new Random();
		if (difficulty == Difficulty.EASY) {
			// Fewer walls, simple paths
			generateMaze(); // Use random generation
		} else if (difficulty == Difficulty.MEDIUM) {
			// Moderate walls and complexity
			generateMaze();
			addObstacles(rand, 0.3); // Add 30% more walls
		} else if (difficulty == Difficulty.HARD) {
			// Complex paths, many walls
			generateMaze();
			addObstacles(rand, 0.5); // Add 50% more walls
		}
		// Ensure solvability
		while (!isSolvable()) {
			generateMaze(); // Regenerate if not solvable
		}
	}

	private void addObstacles(Random rand, double density) {
		int totalCells = rows * cols;
		int numObstacles = (int) (totalCells * density);
		for (int i = 0; i < numObstacles; i++) {
			int row = rand.nextInt(rows);
			int col = rand.nextInt(cols);
			// Avoid overwriting start and end cells
			if ((row != 0 || col != 0) && (row != rows - 1 || col != cols - 1)) {
				grid[row][col] = false; // Add wall
			}
		}
	}

	/**
	 * Generates a random maze by setting each cell as either a path or a wall. The
	 * top-left and bottom-right corners are always paths to designate the start and
	 * end points.
	 */
	private void generateMaze() {
		Random rand = new Random();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j] = rand.nextBoolean(); // Randomly decide if it's a wall or path
			}
		}
		grid[0][0] = true; // Start
		grid[rows - 1][cols - 1] = true; // End
	}

	/**
	 * Checks if the maze has a valid path from the start to the end cell using
	 * Breadth-First Search (BFS). Converts the maze grid to a graph representation
	 * and verifies connectivity.
	 *
	 * @return true if the maze is solvable; false otherwise
	 */
	private boolean isSolvable() {
		Graph graph = createGraph();
		int start = 0;
		int end = rows * cols - 1;
		BreadthFirstPaths bfs = new BreadthFirstPaths(graph, start);
		return bfs.hasPathTo(end);
	}

	/**
	 * Creates a graph representation of the maze grid. Each cell is treated as a
	 * node, and edges are added between adjacent path cells.
	 *
	 * @return the graph representation of the maze
	 */
	private Graph createGraph() {
		Graph graph = new Graph(rows * cols);
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (isPath(row, col)) {
					int v = row * cols + col;
					if (row + 1 < rows && isPath(row + 1, col)) {
						graph.addEdge(v, (row + 1) * cols + col);
					}
					if (col + 1 < cols && isPath(row, col + 1)) {
						graph.addEdge(v, row * cols + col + 1);
					}
				}
			}
		}
		return graph;
	}

	/**
	 * Checks if the cell at the specified row and column is a path (rather than a
	 * wall).
	 *
	 * @param row - the row index of the cell
	 * @param col - the column index of the cell
	 * @return true if the cell is a path; false if it is a wall
	 */
	public boolean isPath(int row, int col) {
		return row >= 0 && col >= 0 && row < rows && col < cols && grid[row][col];
	}

	/**
	 * Gets the number of rows in the maze.
	 *
	 * @return the number of rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Gets the number of columns in the maze.
	 *
	 * @return the number of columns
	 */
	public int getCols() {
		return cols;
	}

	/**
	 * Gets the start position of the maze (top-left).
	 *
	 * @return the start position
	 */
	public int getStartPosition() {
		return 0;
	}
}
