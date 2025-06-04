package maze;
import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.DepthFirstPaths;
import edu.princeton.cs.algs4.Graph;
/**
* This class provides functionality to solve a given maze by finding
* a path from the starting cell to the ending cell, if one exists. It supports
* breadth-first search (BFS) and depth-first search (DFS) approaches for path finding.
*
* @author Manzel Kiinu + Darrin Bracken
*/
public class MazeSolver {
	private final Maze maze;
	private final Graph graph;
	/**
	 * Constructs a MazeSolver for the specified Maze. Creates a graph
	 * representation of the maze to facilitate pathfinding.
	 *
	 * @param maze - the Maze object to solve
	 */
	public MazeSolver(Maze maze) {
		this.maze = maze;
		this.graph = createGraph(maze);
	}
	/**
	 * Converts the maze into a graph, where each cell in the maze grid is
	 * represented as a node in the graph, and edges connect adjacent path cells.
	 *
	 * @param maze - the Maze object to represent as a graph
	 * @return the graph representation of the maze
	 */
	private Graph createGraph(Maze maze) {
		int rows = maze.getRows();
		int cols = maze.getCols();
		Graph graph = new Graph(rows * cols);
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (maze.isPath(row, col)) {
					int v = row * cols + col;
					if (row + 1 < rows && maze.isPath(row + 1, col)) {
						graph.addEdge(v, (row + 1) * cols + col);
					}
					if (col + 1 < cols && maze.isPath(row, col + 1)) {
						graph.addEdge(v, row * cols + col + 1);
					}
				}
			}
		}
		return graph;
	}
	/**
	 * Solves the maze by finding a path from the start to the end cell using
	 * breadth-first search.
	 *
	 * @return an iterable of integers representing the path from start to end if
	 *         one exists; null if no path is found
	 */
	public Iterable<Integer> solveBFS() {
		int start = 0;
		int end = maze.getRows() * maze.getCols() - 1;
		BreadthFirstPaths bfs = new BreadthFirstPaths(graph, start);
		return bfs.hasPathTo(end) ? bfs.pathTo(end) : null;
	}
	
	/**
	    * Solves the maze by finding a path from the start to the end cell using DFS.
	    *
	    * @return an iterable of integers representing the path from start to end if
	    *         one exists; null if no path is found
	    */
	   public Iterable<Integer> solveDFS() {
	       int start = 0; // Start at the top-left corner (0,0)
	       int end = maze.getRows() * maze.getCols() - 1; // bottom-right corner
	       DepthFirstPaths dfs = new DepthFirstPaths(graph, start);
	       return dfs.hasPathTo(end) ? dfs.pathTo(end) : null;
	   }
}



