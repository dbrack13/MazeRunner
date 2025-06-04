package maze;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * This class provides a graphical interface to display and solve a Maze. It
 * visualizes the maze grid, the start and finish points, player position, user
 * controls, and the solution path using BFS and DFS.
 *
 * The maze is drawn using a grid where walls and paths are represented by
 * different colors. Solution Path will be displayed in blue and red.
 *
 * @author Manzel Kiinu + Darrin Bracken
 */
public class MazeFrameTesting extends JFrame implements ActionListener {
	private Maze maze;
	private MazeSolver solver;
	private JPanel mazePanel;
	private JPanel controlPanel;
	private JButton resetButton;
	private JButton bfsButton;
	private JButton dfsButton;
	private JButton upButton;
	private JButton downButton;
	private JButton leftButton;
	private JButton rightButton;
	private MazeTimer bfsTimer;
	private MazeTimer dfsTimer;
	private MazeTimer playerTimer;
	private JLabel bfsTimerLabel;
	private JLabel dfsTimerLabel;
	private JLabel playerTimerLabel;
	private java.util.List<Integer> bfsPath;
	private java.util.List<Integer> dfsPath;
	private int playerPosition;
	private JPanel SolveButtonsPanel;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JComboBox<String> difficultyChoice;
	private JButton setMaze;
	private JTextField textField_1;

	/**
	 * Constructs a MazeGUI to display and solve the specified Maze.
	 *
	 * Initializes the JFrame, sets up the maze display panel, and adds a button for
	 * solving the maze. Maze grid drawn at initialization
	 *
	 * @param maze - the Maze object to display and solve
	 */
	public MazeFrameTesting(Maze maze) {
		this.maze = maze;
		this.solver = new MazeSolver(maze);
		this.bfsTimer = new MazeTimer();
		this.dfsTimer = new MazeTimer();
		this.playerTimer = new MazeTimer();

		// Set up the JFrame
		initializeJFrame();
		// Initialize and add the maze to north panel
		initializeMaze(maze);
		// player Controls
		playerControls();
		// Reset button setup
		resetButton(maze);
		// Add Timer to East Panel
		pnlTimer();
		// Create buttonsPanel to Bottom panel
		pnlButtons(bfsButton);
		// Make GUI visible
		setVisible(true);
		// Assigns starting position of player
		playerPosition = maze.getStartPosition();
	}

	private void initializeMaze(Maze maze) {
		mazePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				drawMaze(g);
				int cols = maze.getCols();
				int cellSize = Math.min(mazePanel.getWidth() / maze.getCols(), mazePanel.getHeight() / maze.getRows());
				// Draw BFS Path in blue
				if (bfsPath != null) {
					g.setColor(Color.blue);
					for (int v : bfsPath) {
						int row = v / cols;
						int col = v % cols;
						g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
					}
				}
				// Draw DFS Path in red, lower opacity to see divergent paths
				if (dfsPath != null) {
					Color transparentRed = new Color(255, 0, 0, 100);
					g.setColor(transparentRed);
					for (int v : dfsPath) {
						int row = v / cols;
						int col = v % cols;
						g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
					}
				}
				// Draw Player position
				int playerRow = playerPosition / cols;
				int playerCol = playerPosition % cols;
				g.setColor(Color.GREEN);
				g.fillOval(playerCol * cellSize + cellSize / 4, playerRow * cellSize + cellSize / 4, cellSize / 2,
						cellSize / 2);
			}
		};
		mazePanel.setBackground(new Color(143, 188, 143));
		mazePanel.setPreferredSize(new Dimension(600, 600));
		getContentPane().add(mazePanel, BorderLayout.CENTER);
		
		// Initialize and add the control panel to south
		controlPanel = new JPanel();
		bfsButton = new JButton("Solve Maze (BFS)");
		bfsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				solveBFSAndDisplayPath();
				setMaze.setEnabled(false);
				resetButton.setEnabled(false);
				bfsButton.setEnabled(false);
			}
		});

		// DFS button setup
		dfsButton = new JButton("Solve Maze (DFS)");
		dfsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				solveDFSAndDisplayPath();
				setMaze.setEnabled(false);
				resetButton.setEnabled(false);
				dfsButton.setEnabled(false);
			}
		});

	}

	private void initializeJFrame() {
		setTitle("MazeRunner!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		setSize(600, 700); // Adjust to fit maze and buttons
	}

	private void playerControls() {
		controlPanel = new JPanel();
		controlPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

	}

	/**
	 * Formats the reset button and functionality
	 *
	 * @param maze
	 */
	private void resetButton(Maze maze) {
		resetButton = new JButton("Reset Maze");
		resetButton.setBackground(Color.RED);
		resetButton.setForeground(Color.WHITE);
		resetButton.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		resetButton.setFont(new Font("Tahoma", Font.BOLD, 15)); // Bold text
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				maze.generateSolvableMaze();
				playerPosition = maze.getStartPosition();
				mazePanel.repaint();
				solver = new MazeSolver(maze);
				bfsTimer.reset();
				dfsTimer.reset();
				playerTimer.reset();
				updateBfsTimerLabel(0);
				updateDfsTimerLabel(0);
				updatePlayerTimerLabel(0);
				bfsPath = null;
				dfsPath = null;
				textField_1.setText("");
			}
		});
	}

	/**
	 * Constructs the timer panel with a difficulty dropdown and Set Difficulty
	 * button.
	 */
	private void pnlTimer() {
		JPanel timerPanel = new JPanel();
		timerPanel.setBackground(new Color(143, 188, 143));
		timerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.Y_AXIS));
		// Add timer labels
		bfsTimerLabel = new JLabel("BFS Time: 0.0 seconds");
		dfsTimerLabel = new JLabel("DFS Time: 0.0 seconds");
		playerTimerLabel = new JLabel("Player Time: 0.0 seconds");
		timerPanel.add(bfsTimerLabel);
		timerPanel.add(dfsTimerLabel);
		timerPanel.add(playerTimerLabel);
		// Add spacing
		timerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		// Add the difficulty dropdown
		String[] difficulties = { "Easy", "Medium", "Hard" }; // Define difficulty options
		difficultyChoice = new JComboBox<>(difficulties); // Initialize JComboBox with options
		difficultyChoice.setAlignmentX(Component.LEFT_ALIGNMENT); // Optional: Align to the left
		textField_1 = new JTextField();
		timerPanel.add(textField_1);
		textField_1.setColumns(10);
		timerPanel.add(difficultyChoice);
		// Add spacing
		timerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		// Add the Set Difficulty button
		setMaze = new JButton("Set Difficulty");
		setMaze.setAlignmentX(Component.LEFT_ALIGNMENT);
		timerPanel.add(setMaze);
		// Add an action listener to the button
		setMaze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedDifficulty = (String) difficultyChoice.getSelectedItem();
				if (selectedDifficulty != null) {
					switch (selectedDifficulty) {
					case "Easy":
						maze.setDifficulty(Maze.Difficulty.EASY);
						break;
					case "Medium":
						maze.setDifficulty(Maze.Difficulty.MEDIUM);
						break;
					case "Hard":
						maze.setDifficulty(Maze.Difficulty.HARD);
						break;
					}
					maze.generateSolvableMaze();
					playerPosition = maze.getStartPosition();
					mazePanel.repaint();
					solver = new MazeSolver(maze);
					bfsTimer.reset();
					dfsTimer.reset();
					playerTimer.reset();
					updateBfsTimerLabel(0);
					updateDfsTimerLabel(0);
					updatePlayerTimerLabel(0);
					bfsPath = null;
					dfsPath = null;
				}
			}
		});
		// Add the timer panel to the EAST position of the content pane
		getContentPane().add(timerPanel, BorderLayout.EAST);
	}

	/**
	 * Constructs the panel which holds the solving buttons
	 *
	 * @param solveButton
	 */
	private void pnlButtons(JButton solveButton) {
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setPreferredSize(new Dimension(buttonsPanel.getWidth(), 500));
		SolveButtonsPanel = new JPanel();
		SolveButtonsPanel.setBackground(new Color(143, 188, 143));
		getContentPane().add(SolveButtonsPanel, BorderLayout.SOUTH);
		SolveButtonsPanel.setLayout(new GridLayout(4, 3, 10, 5));
		SolveButtonsPanel.add(solveButton);
		styleButton(solveButton, new Color(50, 205, 50)); // Styled as green
		SolveButtonsPanel.add(resetButton);
		SolveButtonsPanel.add(dfsButton);
		styleButton(dfsButton, new Color(65, 105, 225)); // Styled as blue
		lblNewLabel_2 = new JLabel("");
		SolveButtonsPanel.add(lblNewLabel_2);
		lblNewLabel = new JLabel("");
		SolveButtonsPanel.add(lblNewLabel);
		lblNewLabel_1 = new JLabel("");
		SolveButtonsPanel.add(lblNewLabel_1);
		lblNewLabel_3 = new JLabel("");
		SolveButtonsPanel.add(lblNewLabel_3);
		// Up Button
		upButton = new JButton("↑");
		styleButton(upButton, new Color(128, 128, 128)); // Styled as gray for arrows
		upButton.setFont(new Font("Tahoma", Font.BOLD, 20)); // Larger font for arrows
		upButton.addActionListener(e -> movePlayer(0, -1));
		SolveButtonsPanel.add(upButton);
		JLabel label_1 = new JLabel();
		SolveButtonsPanel.add(label_1);
		// Left Button
		leftButton = new JButton("←");
		styleButton(leftButton, new Color(128, 128, 128)); // Styled as gray for arrows
		leftButton.setFont(new Font("Tahoma", Font.BOLD, 20)); // Larger font for arrows
		leftButton.addActionListener(e -> movePlayer(-1, 0));
		SolveButtonsPanel.add(leftButton);
		// Down Button
		downButton = new JButton("↓");
		styleButton(downButton, new Color(128, 128, 128)); // Styled as gray for arrows
		downButton.setFont(new Font("Tahoma", Font.BOLD, 20)); // Larger font for arrows
		downButton.addActionListener(e -> movePlayer(0, 1));
		SolveButtonsPanel.add(downButton);
		// Right Button
		rightButton = new JButton("→");
		styleButton(rightButton, new Color(128, 128, 128)); // Styled as gray for arrows
		rightButton.setFont(new Font("Tahoma", Font.BOLD, 20)); // Larger font for arrows
		rightButton.addActionListener(e -> movePlayer(1, 0));
		SolveButtonsPanel.add(rightButton);
	}

	/**
	 * Styles a button with a consistent look.
	 *
	 * @param button the button to style
	 * @param color  the background color of the button
	 */
	private void styleButton(JButton button, Color color) {
		button.setFont(new Font("Tahoma", Font.BOLD, 15)); // Bold text
		button.setBackground(color);
		button.setForeground(Color.WHITE); // White text
		button.setFocusPainted(false); // Remove focus border
		button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Add a border
		button.setOpaque(true); // Make the background color solid
	}

	
	/**
	 * Tracks the players position.
	 *
	 * @param dx
	 * @param dy
	 */
	private void movePlayer(int dx, int dy) {
		// Check if the player is moving for the first time
		if (!playerTimer.isRunning()) {
			playerTimer.start();
		}

		int rows = maze.getRows();
		int cols = maze.getCols();
		int newRow = playerPosition / cols + dy;
		int newCol = playerPosition % cols + dx;
		if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && maze.isPath(newRow, newCol)) {
			playerPosition = newRow * cols + newCol;
			mazePanel.repaint();
			updatePlayerTimerLabel(playerTimer.getElapsedTimeInSeconds());
		}

		// Check if the player has reached the finish cell
		if (newRow == rows - 1 && newCol == cols - 1) {
			playerTimer.stop();
			updatePlayerTimerLabel(playerTimer.getElapsedTimeInSeconds());
			System.out.println(
					"Player completed the maze in " + playerTimer.getElapsedTimeInSeconds() + " milliseconds.");
			textField_1.setText("You solved the maze! Congrats!");
		}
	}

	/**
	 * Draws the maze grid on the screen, indicating paths and walls with different
	 * colors. Shows the start and end points, and if a solution path exists,
	 * animates the path in blue.
	 *
	 * @param g - the Graphics object used to render the maze on the screen
	 */
	public void drawMaze(Graphics g) {
		int rows = maze.getRows();
		int cols = maze.getCols();
		int cellSize = Math.min(mazePanel.getWidth() / cols, mazePanel.getHeight() / rows);
		// Draw the maze
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (maze.isPath(i, j)) {
					g.setColor(Color.WHITE); // Walls
				} else {
					g.setColor(Color.BLACK); // Paths
				}
				g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
			}
		}
		// Draw the start and finish labels
		int startX = 0 * cellSize;
		int startY = 0 * cellSize;
		g.setColor(Color.GREEN);
		g.fillOval(startX + cellSize / 4, startY + cellSize / 4, cellSize / 2, cellSize / 2);
		// Draw the finish point with a larger red circle
		int endX = (cols - 1) * cellSize;
		int endY = (rows - 1) * cellSize;
		g.setColor(Color.RED);
		g.fillOval(endX + cellSize / 4, endY + cellSize / 4, cellSize / 2, cellSize / 2);
	}

	/**
	 * Solves the maze and displays the solution.
	 */
	public void solveBFSAndDisplayPath() {
		bfsTimer.start();
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				bfsPath = new ArrayList<>();
				Iterable<Integer> path = solver.solveBFS();
				if (path != null) {
					for (int v : path) {
						bfsPath.add(v);
						Thread.sleep(100);
						repaint();
					}
				} else {
					JOptionPane.showMessageDialog(MazeFrameTesting.this, "No path found from start to end.");
				}
				return null;
			}

			@Override
			protected void done() {
				bfsTimer.stop();
				updateBfsTimerLabel(bfsTimer.getElapsedTimeInSeconds());
				resetButton.setEnabled(true);
				bfsButton.setEnabled(true);
				dfsButton.setEnabled(true);
				setMaze.setEnabled(true);
				textField_1.setText("BFS Maze has been solved!");
			}
		};
		worker.execute();
	}

	/**
	 * Solves the maze and displays the solution.
	 */
	public void solveDFSAndDisplayPath() {
		// Draw the solution path, if it exists
		dfsTimer.start();
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				dfsPath = new ArrayList<>();
				Iterable<Integer> path = solver.solveDFS();
				if (path != null) {
					for (int v : path) {
						dfsPath.add(v);
						Thread.sleep(100);
						repaint();
					}
				} else {
					JOptionPane.showMessageDialog(MazeFrameTesting.this, "No path found from start to end.");
				}
				return null;
			}

			@Override
			protected void done() {
				dfsTimer.stop(); // Stop the timer when done solving
				updateDfsTimerLabel(dfsTimer.getElapsedTimeInSeconds()); // Update label with elapsed time
				resetButton.setEnabled(true);
				bfsButton.setEnabled(true);
				dfsButton.setEnabled(true);
				setMaze.setEnabled(true);
				textField_1.setText("DFS Maze has been solved!");
			}
		};
		worker.execute();
	}

	/**
	 * Updates the timer label with the provided elapsed time.
	 *
	 * @param seconds - The elapsed time in seconds
	 */
	private void updateBfsTimerLabel(double seconds) {
		SwingUtilities.invokeLater(() -> bfsTimerLabel.setText(String.format("BFS Time: %.2f seconds", seconds)));
	}

	/**
	 * Updates the timer label with the provided elapsed time.
	 *
	 * @param seconds - The elapsed time in seconds
	 */
	private void updateDfsTimerLabel(double seconds) {
		SwingUtilities.invokeLater(() -> dfsTimerLabel.setText(String.format("DFS Time: %.2f seconds", seconds)));
	}

	/**
	 * Updates the player timer label with the provided elapsed time.
	 *
	 * @param seconds - The elapsed time in seconds
	 */
	private void updatePlayerTimerLabel(double seconds) {
		SwingUtilities.invokeLater(() -> playerTimerLabel.setText(String.format("Player Time: %.2f seconds", seconds)));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
}
