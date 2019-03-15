/*
	Work in progress. 
	What I still need to add:
		* right click to mark bomb (add a little flag icon. if right clicked again it will revert to the blank button)
		* counters for: time, remaining bombs
		* left clicking a bomb ends the game
		* aethetic stuff
		* fix up the fxn that opens up large empty spaces (only open empty tiles directly beside, above, or below. not diagonal)
		* make a start screen where you can see previous times, choose # of bombs and grid size.
		  (with edge cases for impossible grid size & bomb combinations)
	What I have:
		* most of the important functions like creating a grid with random bombs and the correct numbers. 
		  (with any # on bombs and size of grid. They're specified in Run.java) 
		* opening up empty spaces nearby if an empty tile is clicked.
		* clicking on a tile with display the value of it
*/
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Minesweeper {

	private List<Integer> mineList;
	private String[][] grid;
	private String[][] hiddenGrid;
	private JButton[][] visibleGrid;
	private String q;
	private int num;
	private JFrame f;
	private JPanel p;
	
	public Minesweeper(int row, int col, int mines) {

		gridGen(row, col, mines);
		display(row, col);

	}
	
	private void gridGen(int row, int col, int mines) {
		
		mineList = randomizedNumList(mines, 1, row*col);
		grid = empty2DArray(row, col);
		
		// putting mines onto the grid
		num = 1;
		for (int r = 0; r < row; r++) {
			for (int c = 0; c < col; c++) {
				// if (r, c) is a mine, set mine
				if (mineList.contains(num)) {
					grid[r][c] = "MINE";
					
					// +1 to nearby blocks without a mine
					for (int h = -1; h <= 1; h++) {
						for (int k = -1; k <= 1; k++) {
							try { grid[r+h][c+k] += "1"; }
							catch (Exception e) {}
						}
					}
				}
				num+=1;
			}
		}
	}
	// gridGen end


	private void display(int row, int col) {

		//Frame
		f = new JFrame("Minesweeper!");
		f.setSize((row+1)*100, col*100);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		
		// Bottom panel
		p = new JPanel();
		p.setVisible(true);
		p.setBackground(Color.WHITE);
		p.setLayout(new GridLayout(row+1, col, 2, 2));
		f.add(p);

		//String Array for Button Labels.
		this.hiddenGrid = new String[row][col];
		for (int r = 0; r < row; r++) {
			for (int c = 0; c < col; c++) {
				q = grid[r][c];
				
				if (q.contains("MINE")) {
					hiddenGrid[r][c] = "*";
				}
				else if (q.equals("")) {
					hiddenGrid[r][c] = " ";
				}
				else {
					hiddenGrid[r][c] = Integer.toString(q.length());
				}
			}
		}
		
		// Visible JButton Array
		this.visibleGrid = new JButton[row][col];
		for (int r = 0; r < row; r++) {
			final int R = r;
			for (int c = 0; c < col; c++) {
				
				//sloppy ik but, capital letter vars to pass through the actionlistener.
				final int C = c;
				visibleGrid[r][c] = new JButton(" ");
				visibleGrid[r][c].addActionListener( e -> {
					clicked(R, C);
				});
				p.add(visibleGrid[r][c]);
			}
		}
		
		//display
		f.pack();
		f.setVisible(true);
		p.setVisible(true);
	}
	// Display end
	
	
	// When a button is clicked
	private void clicked(int row, int col) {

		if (hiddenGrid[row][col].equals(" ")) {
			emptyOrNah(row, col);
		}
		else if (!hiddenGrid[row][col].equals(null)) {
			visibleGrid[row][col].setText(hiddenGrid[row][col]);
			visibleGrid[row][col].setEnabled(false);
		}
		else if (hiddenGrid[row][col].equals(null)) {
			visibleGrid[row][col].setVisible(false);
		}
		else {
			System.out.print("error");
		}
	}

	// is (r, c) empty? are it's nearby tiles empty too?
	private void emptyOrNah(int r, int c) {
			for (int h = -1; h <= 1; h+=2) {
				try {
					if (hiddenGrid[r+h][c].equals(" ")) {
						visibleGrid[r+h][c].setVisible(false);
						hiddenGrid[r+h][c] = null;
						emptyOrNah(r+h, c);
					}
				}
				catch (Exception e) {}
			}

			for (int k = -1; k <= 1; k+=2) {
				try {
					if (hiddenGrid[r][c+k].equals(" ")) {
						visibleGrid[r][c+k].setVisible(false);
						hiddenGrid[r][c+k] = null;
						emptyOrNah(r, c+k);
					}
				}
				catch (Exception e) {}
			}
	}

	/*------------------------------------- save these for later -------------------------------------*/
	
	// ArrayList of (length) random numbers between (min) and (max) with no repeated numbers.
		public static List<Integer> randomizedNumList(int length, int min, int max) {
			
			List<Integer> numList =  new ArrayList<>();
			for (int i = min; i <= max; i++) {
				numList.add(i);
			}
			
			Collections.shuffle(numList);
			while (numList.size() > length) {
				numList.remove(numList.size() - 1);
			}
			
			return numList;
		}
		
		// empty String 2d array, column length of (cLength), row length of (rLength)
		public static String[][] empty2DArray(int rLength, int cLength) {
			
			String[][] grid = new String[rLength][cLength];
			for (int r = 0; r < rLength; r++) {
				for (int c = 0; c < cLength; c++) {
					grid[r][c] = "";
				}
			}
			
			return grid;

		}

}

