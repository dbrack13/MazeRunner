package maze;
import maze.Maze.Difficulty;
/**
* This class initializes the GUI in MazeFrame and starts it on easy mode.
*
* @author Manzel Kiinu + Darrin Bracken
*/
public class MazeProgram {
  public static void main(String[] args) {
  	
      // Create a new maze with a specified difficulty (e.g., EASY)
      Maze maze = new Maze(Difficulty.EASY);     
      new MazeFrameTesting(maze);
     
  }
}





