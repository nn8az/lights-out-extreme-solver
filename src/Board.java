import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class Board extends JFrame {
	// Fields that deals with the puzzle's logic
	private int size = 5;
	private int state = 2;
	private int[][] valueTable;
	private int numberOfClicks = 0;

	// Fields that deals with GUI
	// private JPanel m = new JPanel(new FlowLayout());
	private JPanel m = new JPanel();
	private JPanel p = new JPanel();
	private JPanel sideP = new JPanel();
	private JPanel sideButtonP = new JPanel();
	private JPanel sideLabelP = new JPanel();
	private JPanel dropDownP = new JPanel();
	private JPanel solutionP = new JPanel();
	private JPanel subSolutionP = new JPanel();

	// Buttons
	private JButton[][] buttons;
	private JButton solveB = new JButton();
	private JButton resetB = new JButton();
	private JButton editB = new JButton();

	// labels
	private JLabel currentL = new JLabel();
	private JLabel bestL = new JLabel();
	private JLabel[][] solutionGrid;
	private JLabel solutionHeader;
	private JLabel percentSolvableLabel;

	// combo Boxes

	// boolean edit
	private boolean editMode = false;
	private int editOnOff = 0;

	public Board(int size, int state) {
		// Board GUI
		super("Lights Out!");
		setSize(820, 635);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().add(m);
		m.setBackground(getColor("#E0E0E0"));
		m.add(p);
		m.add(sideP);
		sideP.setLayout(new FlowLayout());
		// sideButtonP.setLayout(new BoxLayout(sideButtonP, BoxLayout.Y_AXIS));
		sideButtonP.setLayout(new GridLayout(3, 1));
		sideLabelP.setLayout(new BoxLayout(sideLabelP, BoxLayout.Y_AXIS));
		solutionP.setLayout(new BoxLayout(solutionP, BoxLayout.Y_AXIS));
		dropDownP.setLayout(new BoxLayout(dropDownP, BoxLayout.Y_AXIS));

		// set Sizes
		sideButtonP.setPreferredSize(new Dimension(200, 100));
		dropDownP.setPreferredSize(new Dimension(200, 100));
		solutionP.setPreferredSize(new Dimension(200, 200));
		subSolutionP.setPreferredSize(new Dimension(200, 200));

		// add Components
		sideP.add(sideLabelP);
		sideP.add(solutionP);
		sideP.add(dropDownP);
		sideP.add(sideButtonP);

		// SideP GUI
		sideP.setPreferredSize(new Dimension(200, 600));

		// Set puzzle's parameter
		this.size = size;
		this.state = state;

		// initialize GUI for side Panel
		// need to add methods for these buttons
		solveB.setText("Show Solution");
		solveB.setBackground(getColor("#CCCCCC"));
		solveB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				publishSolution();
			}
		});
		sideButtonP.add(solveB);
		resetB.setText("Reset Board");
		resetB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createBoard();
				p.revalidate();
			}
		});
		resetB.setBackground(getColor("#CCCCCC"));
		sideButtonP.add(resetB);
		editB.setText("Edit Board");
		editB.setBackground(getColor("#CCCCCC"));
		editB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editOnOff = (editOnOff + 1) % 2;
				if (editOnOff == 1) {
					editMode = true;
					editB.setBackground(getColor("#AAAAAA"));
				} else {
					editMode = false;
					editB.setBackground(getColor("#CCCCCC"));
				}
			}
		});
		sideButtonP.add(editB);

		// initialize GUI for side Label Panel
		// need to add methods for these panels
		JLabel lightsOutHeader = new JLabel();
		lightsOutHeader.setText("Lights Out");
		// lightsOutHeader.setFont(new Font("Lights Out", Font.BOLD, 12));
		currentL.setText("Current Moves: " + numberOfClicks);
		sideLabelP.add(lightsOutHeader);
		sideLabelP.add(currentL);

		// initialize GUI for Solution Panel
		// also includes sub solution

		solutionHeader = new JLabel();
		solutionHeader.setText("Solution");
		solutionP.add(solutionHeader);
		percentSolvableLabel = new JLabel("100% Solvable");
		solutionP.add(percentSolvableLabel);

		// initialize comboBoxes for dropDown Panel
		String[] boardSizes = { " ", "3x3", "4x4", "5x5", "6x6", "7x7", "8x8" };
		String[] numOfColors = { "2 colors", "3 colors", "5 colors", "7 colors" };
		final JComboBox boardSizeCB = new JComboBox(boardSizes);
		final int color = state;
		boardSizeCB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String s = (String) boardSizeCB.getSelectedItem();
				switch (s) {
				case " ":
					break;
				case "3x3":
					setSize(3);
					createBoard();
					p.revalidate();
					subSolutionP.revalidate();
					break;
				case "4x4":
					setSize(4);
					createBoard();
					p.revalidate();
					subSolutionP.revalidate();
					break;
				case "5x5":
					setSize(5);
					createBoard();
					p.revalidate();
					subSolutionP.revalidate();
					break;
				case "6x6":
					setSize(6);
					createBoard();
					p.revalidate();
					subSolutionP.revalidate();
					break;
				case "7x7":
					setSize(7);
					createBoard();
					p.revalidate();
					subSolutionP.revalidate();
					break;
				case "8x8":
					setSize(8);
					createBoard();
					p.revalidate();
					subSolutionP.revalidate();
					break;
				}
			}

		});
		final JComboBox colorsCB = new JComboBox(numOfColors);
		colorsCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = (String) colorsCB.getSelectedItem();
				switch (s) {
				case "2 colors":
					setState(2);
					createBoard();
					p.revalidate();
					break;
				case "3 colors":
					setState(3);
					createBoard();
					p.revalidate();
					break;
				case "5 colors":
					setState(5);
					createBoard();
					p.revalidate();
					break;
				case "7 colors":
					setState(7);
					createBoard();
					p.revalidate();
					break;
				}
			}

		});
		dropDownP.add(boardSizeCB);
		dropDownP.add(colorsCB);
		// initialize the buttons
		createBoard();
		setVisible(true);
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void createBoard() {
		// Compute how unsolvable the current version of the puzzle is
		Solver percentSolvableCalculator = new Solver(size, size, state);
		percentSolvableCalculator.setBVector(new int[size * size]);
		percentSolvableCalculator.RowReduce();
		double percentSolvable = percentSolvableCalculator.percentSolvable()*100;
		percentSolvableLabel.setText((new DecimalFormat("#0.00")).format(percentSolvable) + "% Solvable");
		
		numberOfClicks = 0;
		p.removeAll();
		// initialize the values
		valueTable = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int times = 0; times < (int) (Math.random() * size); times++) {
					select(i, j);
				}
			}
		}
		p.setLayout(new GridLayout(size, size));
		p.setPreferredSize(new Dimension(600, 600));
		p.setBackground(getColor("#F0F0F0"));
		buttons = new JButton[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				final int x = i;
				final int y = j;
				buttons[i][j] = new JButton(valueTable[i][j] + "");
				buttons[i][j].setFont(new Font("Dialog", Font.PLAIN, 30));
				buttons[i][j].setOpaque(true);
				changeColor(i, j);
				/*
				 * buttons[i][j].addActionListener(new ActionListener() {
				 * 
				 * @Override public void actionPerformed(ActionEvent e) {
				 * press(x, y); } });
				 */
				buttons[i][j].addMouseListener(new MouseListener() {
					@Override
					public void mousePressed(MouseEvent e) {
						// do some stuff
					}

					@Override
					public void mouseClicked(MouseEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						// TODO Auto-generated method stub
						highlightAdj(x, y);
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						// TODO Auto-generated method stub
						changeColor(x, y);
						if (x - 1 >= 0) { // cycle the left tile
							changeColor(x - 1, y);
						}
						if (x + 1 <= size - 1) { // cycle the right tile
							changeColor(x + 1, y);
						}
						if (y + 1 <= size - 1) { // cycle the bottom tile
							changeColor(x, y + 1);
						}
						if (y - 1 >= 0) { // cycle the top tile
							changeColor(x, y - 1);
						}

					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
						press(x, y);
					}
				});
				p.add(buttons[i][j]);
			}
		}
		subSolutionP.removeAll();
		subSolutionP.setLayout(new GridLayout(size, size));
		subSolutionP.setBackground(getColor("#F0F0F0"));

		solutionGrid = new JLabel[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				solutionGrid[i][j] = new JLabel("0");
				subSolutionP.add(solutionGrid[i][j]);
			}
		}
		solutionP.add(subSolutionP);
	}

	public void changeColor(int i, int j) {
		valueTable[i][j] = (valueTable[i][j]) % state;
		Color theColor = Color.WHITE;
		theColor = getColor(colorHex(valueTable[i][j]));
		buttons[i][j].setBackground(theColor);
	}
	
	public Color getColor(String a) {
		return Color.decode(a);
	}

	public void press(int x, int y) {
		// System.out.println("you have pressed " + x + " " + y);
		select(x, y);
		buttons[x][y].setText(valueTable[x][y] + "");
		changeColor(x, y);
		if (editMode == false) {
			if (x - 1 >= 0) { // cycle the left tile
				buttons[x - 1][y].setText(valueTable[x - 1][y] + "");
				changeColor(x - 1, y);
			}
			if (x + 1 <= size - 1) { // cycle the right tile
				buttons[x + 1][y].setText(valueTable[x + 1][y] + "");
				changeColor(x + 1, y);
			}
			if (y + 1 <= size - 1) { // cycle the bottom tile
				buttons[x][y + 1].setText(valueTable[x][y + 1] + "");
				changeColor(x, y + 1);
			}
			if (y - 1 >= 0) { // cycle the top tile
				buttons[x][y - 1].setText(valueTable[x][y - 1] + "");
				changeColor(x, y - 1);
			}
			numberOfClicks++;
			currentL.setText("Current Moves: " + numberOfClicks);
		}
	}

	public void select(int x, int y) {
		valueTable[x][y] = (valueTable[x][y] + 1) % state; // cycle the
															// selected
		if (editMode == false) {
			// tile
			if (x - 1 >= 0) { // cycle the left tile
				valueTable[x - 1][y] = (valueTable[x - 1][y] + 1) % state;
				// System.out.println("you have cycled " + (x-1) + " " + y);
			}
			if (x + 1 <= size - 1) { // cycle the right tile
				valueTable[x + 1][y] = (valueTable[x + 1][y] + 1) % state;
			}
			if (y + 1 <= size - 1) { // cycle the bottom tile
				valueTable[x][y + 1] = (valueTable[x][y + 1] + 1) % state;
			}
			if (y - 1 >= 0) { // cycle the top tile
				valueTable[x][y - 1] = (valueTable[x][y - 1] + 1) % state;
			}
		}
	}

	public void highlightAdj(int x, int y) {
		buttons[x][y].setBackground(Color.decode(darkenColor(valueTable[x][y])));
		if (x - 1 >= 0) { // cycle the left tile
			buttons[x - 1][y].setBackground(Color.decode(darkenColor(valueTable[x - 1][y])));
		}
		if (x + 1 <= size - 1) { // cycle the right tile
			buttons[x + 1][y].setBackground(Color.decode(darkenColor(valueTable[x + 1][y])));
		}
		if (y + 1 <= size - 1) { // cycle the bottom tile
			buttons[x][y + 1].setBackground(Color.decode(darkenColor(valueTable[x][y + 1])));
		}
		if (y - 1 >= 0) { // cycle the top tile
			buttons[x][y - 1].setBackground(Color.decode(darkenColor(valueTable[x][y - 1])));
		}
	}

	public void publishSolution() {
		// Create a solver
		Solver einstein = new Solver(size, size, state);
		
		// Build the b vector
		int[] b = new int[size*size];
		int index = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				b[index] = state - valueTable[i][j];
				index++;
			}
		}
		
		// Give the b vector to the solver and solve
		einstein.setBVector(b);
		einstein.RowReduce();
		
		// Count the row of zero
		int zeroRow = einstein.zeroRowCount();
		double percentSolvable = 1 / Math.pow(state, zeroRow);
		
		
		// Check if a solution exists
		if (einstein.hasSolution()) {
			solutionHeader.setText("Solution");
			solutionHeader.setForeground(Color.black);
			int[][] solution = einstein.publishSolution();
			for (int i = 0; i < size; i++){
				for (int j = 0; j < size; j++) {
					solutionGrid[i][j].setText("" + solution[i][j]);
				}
			} 
		} else {
			solutionHeader.setText("NO SOLUTION!");
			solutionHeader.setForeground(Color.red);
		}
	}

	public String toString() {
		String output = "";
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				output += valueTable[i][j] + " ";
			}
			output += "\n";
		}
		return output;
	}

	public int[] publishB() {
		int[] b = new int[size * size];
		int count = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				b[count] = (state - valueTable[i][j]) % state;
				count++;
			}
		}
		return b;
	}

	public int[][] solve() {
		return valueTable;
	}
	
	private String darkenColor(int currState) {
		int r, g, b;
		String rHex, gHex, bHex;
		int[] color1 = { 45, 117, 182 };
		int[] color2 = { 255, 255, 255 };
		double x = (double) (currState) / (state - 1); // Percentage of color1
														// in the mixture
		r = (int) ((x * color1[0] + (1 - x) * color2[0]) * 0.7);
		g = (int) ((x * color1[1] + (1 - x) * color2[1]) * 0.7);
		b = (int) ((x * color1[2] + (1 - x) * color2[2]) * 0.7);

		rHex = Integer.toHexString(r);
		gHex = Integer.toHexString(g);
		bHex = Integer.toHexString(b);

		if (rHex.length() == 1) {
			rHex = "0" + rHex;
		}
		if (gHex.length() == 1) {
			gHex = "0" + gHex;
		}
		if (bHex.length() == 1) {
			bHex = "0" + bHex;
		}

		return "#" + rHex + gHex + bHex;
	}

	private String colorHex(int currState) {
		int r, g, b;
		String rHex, gHex, bHex;
		int[] color1 = { 45, 117, 182 };
		int[] color2 = { 255, 255, 255 };
		double x = (double) (currState) / (state - 1); // Percentage of color1
														// in the mixture
		r = (int) (x * color1[0] + (1 - x) * color2[0]);
		g = (int) (x * color1[1] + (1 - x) * color2[1]);
		b = (int) (x * color1[2] + (1 - x) * color2[2]);

		rHex = Integer.toHexString(r);
		gHex = Integer.toHexString(g);
		bHex = Integer.toHexString(b);

		if (rHex.length() == 1) {
			rHex = "0" + rHex;
		}
		if (gHex.length() == 1) {
			gHex = "0" + gHex;
		}
		if (bHex.length() == 1) {
			bHex = "0" + bHex;
		}

		return "#" + rHex + gHex + bHex;
	}

	private boolean isSolved() {
		int n = valueTable[0][0]; // check the first tile
		for (int i = 0; i < size; i++) { // as soon as we encounter a tile of
											// different value
			for (int j = 0; j < size; j++) {
				if (valueTable[i][j] != n) {
					return false; // return false
				}
			}
		}
		return true;
	}

}
