package edu.ncsu.csc216.biosimulation.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.ncsu.csc216.biosimulation.simulator.Simulator;

/**
 * Three-species predator prey simulation.
 *
 * @author Jo Perry
 */
public class SimulatorGUI extends JFrame implements ActionListener {

	/**
	 * Private class. Used to pick colors and names of species. Code adapted
	 * from InformIT article by David Geary
	 *
	 * @see http://www.informit.com/articles/article.aspx?p=32060&seqNum=2
	 */
	private static class ColorSetter extends JPanel {
		/**
		 * Listener class for closing the window
		 */
		private class CloseListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				final Color color = chooser.getColor();
				setBackground(color);
				repaint();
			}
		}

		private final JColorChooser chooser = new JColorChooser();
		private JDialog dialog;

		private final int which;

		public ColorSetter(Color color, int k) {
			which = k;
			setBackground(color);

			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					dialog = JColorChooser.createDialog(ColorSetter.this, "Color of " + SPECIES_LABELS[which], false,
							chooser, new CloseListener(), null);
					chooser.setColor(getBackground());
					dialog.setVisible(true);
				}
			});
		}
	}

	///// Window and label constants
	/**
	 * Window title string
	 */
	private final static String TITLE = "Predator-Prey-Scavenger Simulation";
	/**
	 * Number of species in simulation
	 */
	private final static int NUM_SPECIES = 3;
	/**
	 * Width for input text fields
	 */
	private final static int TEXT_FIELD_WIDTH = 10;
	/**
	 * Species type strings
	 */
	private final static String[] SPECIES_LABELS = { "Predator", "Prey", "Scavenger" };

	/**
	 * Descriptions of simulation parameters: [0] predator, [1] prey, [2]
	 * scavenger
	 */
	private final static String[][] DESCRIPTIONS = //
	{ { "Count:", "Birth Rate (via predation):", "Death Rate:" },
			{ "Count:", "Birth Rate:", "Death Rate (via predation):" }, { "Count:", "Birth Rate (via predation):",
					"Death Rate:", "Birth Rate (natural prey deaths):", "Birth Rate (predator deaths):" } };
	///// Components for simulation parameter input
	/**
	 * Input parameter labels
	 */
	private static JLabel[][] lblParameters = new JLabel[NUM_SPECIES][];
	/**
	 * Species population counts
	 */
	private static JTextField[] txtCounts = new JTextField[NUM_SPECIES];
	/**
	 * Simulation parameters for each species
	 */
	private static JTextField[][] txtParmValues = new JTextField[NUM_SPECIES][]; // Parameters
																					// for
																					// each
																					// species
	///// Size constants for the window and panels
	/**
	 * Main window width
	 */
	private final static int FRAME_WIDTH = 950;
	/**
	 * Main window height
	 */
	private final static int FRAME_HEIGHT = 600;

	/**
	 * Simulation graph width
	 */
	private final static int GRAPH_WIDTH = 650;
	/**
	 * Simulation graph height
	 */
	private final static int GRAPH_HEIGHT = 550;
	/**
	 * X and Y offsets from upper left corner of screen
	 */
	private final static int SCREEN_OFFSET = 100;

	/**
	 * X offset for simulation graph.
	 */
	private final static int X_OFFSET = 150;
	/**
	 * Y offset for simulation graph
	 */
	private final static int Y_OFFSET = 25;
	/**
	 * Diameter of plotted point
	 */
	private final static int POINT_SIZE = 3;
	/**
	 * Separate panels for species parameters (embedded in pnlInput).
	 */
	private static JPanel[] pnlSpecies = new JPanel[NUM_SPECIES];
	///// Association with the backend
	/**
	 * Number of simulation steps.
	 */
	private final static int STEPS = 600;

	/**
	 * Main method. Instantiates the user interface.
	 *
	 * @param args
	 *            Not used.
	 */
	public static void main(String[] args) {
		new SimulatorGUI();
	}

	///// Strings and components for the initial window to choose names and
	///// colors
	/**
	 * Default species names
	 */
	private final String[] speciesNames = { "Wolf", "Elk", "Magpie" };
	/**
	 * Default species colors
	 */
	private final Color[] speciesColors = { Color.RED, Color.BLUE, Color.BLACK };

	/**
	 * User component for selecting colors
	 */
	private final ColorSetter[] colorPicker = new ColorSetter[NUM_SPECIES];
	/**
	 * Text fields for setting species names
	 */
	private final JTextField[] txtName = new JTextField[NUM_SPECIES];
	/**
	 * Initial name/color selection window
	 */
	private final JFrame jfInit = new JFrame();

	/**
	 * Closes the name/color selection window
	 */
	private final JButton btnClose = new JButton("Close");
	///// Buttons for the main window
	/**
	 * Button to start a new simulation.
	 */
	private final JButton btnStart = new JButton("Start");
	/**
	 * Button to reset the simulation parameters to their default values.
	 */
	private final JButton btnReset = new JButton("Reset Values");
	/**
	 * Button to quit the application.
	 */
	private final JButton btnQuit = new JButton("Quit");

	///// Panels, Boxes, and Borders for the main window
	/**
	 * Panel to hold main window buttons.
	 */
	private final JPanel pnlButtons = new JPanel(new FlowLayout());
	/**
	 * Panel to hold user input text components.
	 */
	private final JPanel pnlInput = new JPanel();

	/**
	 * Panel to hold the simulation graph
	 */
	private final JPanel pnlSimulation = new JPanel();

	/**
	 * Backend simulator.
	 */
	private Simulator simulator;

	////////////// Public methods /////////////////////
	/**
	 * Constructor. Sets up the components and opens the initial name/color
	 * window.
	 */
	public SimulatorGUI() {
		addActionListenersToButtons();
		setNamesColors();
	}

	////////////// Private methods /////////////////////

	/**
	 * Specifies actions to be performed when the user clicks a button.
	 *
	 * @param e
	 *            Source is the button clicked.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Get the animal colors and names, then set up and open the simulation
		// window
		if (e.getSource() == btnClose) {
			for (int k = 0; k < NUM_SPECIES; k++) {
				speciesColors[k] = colorPicker[k].getBackground();
				speciesNames[k] = txtName[k].getText().trim();
			}
			jfInit.setVisible(false);
			setUpSimulationWindow();
			setVisible(true);
		}
		// Instantiate a Simulator with values in the text fields and run it
		if (e.getSource() == btnStart) {
			try {
				setUpSimulator();
				clearGraph();
				run();
				for (int k = 0; k < NUM_SPECIES; k++) {
					txtCounts[k].setText(simulator.getPopulations()[k] + "");
				}
			} catch (final IllegalArgumentException exc) {
				JOptionPane.showMessageDialog(this, exc.getMessage(), "Input error", JOptionPane.ERROR_MESSAGE);
			}
		}
		// Refill the text fields with Simulator default values
		if (e.getSource() == btnReset) {
			assignDefaultParameters();
		}
		// Exit the program
		if (e.getSource() == btnQuit) {
			System.exit(0);
		}
	}

	/**
	 * Private method. Adds listeners to the buttons.
	 */
	private void addActionListenersToButtons() {
		btnStart.addActionListener(this);
		btnReset.addActionListener(this);
		btnQuit.addActionListener(this);
		btnClose.addActionListener(this);
	}

	/**
	 * Private method. Add labels and text fields to the input panel.
	 */
	private void addWidgetsToInputPanel() {
		for (int j = 0; j < NUM_SPECIES; j++) {
			pnlSpecies[j] = new JPanel();
			pnlSpecies[j].setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(speciesColors[j]),
					speciesNames[j]));
			pnlSpecies[j].setLayout(new GridLayout(5, 2));
			txtCounts[j] = new JTextField(TEXT_FIELD_WIDTH);
			lblParameters[j] = new JLabel[DESCRIPTIONS[j].length];
			txtParmValues[j] = new JTextField[DESCRIPTIONS[j].length - 1];

			for (int k = 0; k < DESCRIPTIONS[j].length; k++) {
				lblParameters[j][k] = new JLabel(DESCRIPTIONS[j][k]);
				lblParameters[j][k].setHorizontalAlignment(JLabel.RIGHT);
				pnlSpecies[j].add(lblParameters[j][k]);
				if (k == 0) {
					pnlSpecies[j].add(txtCounts[j]);
				} else {
					txtParmValues[j][k - 1] = new JTextField(TEXT_FIELD_WIDTH);
					pnlSpecies[j].add(txtParmValues[j][k - 1]);
				}
			}
		}
		assignDefaultParameters();
	}

	/**
	 * Private method. Writes default simulation parameter values in input text
	 * fields.
	 */
	private void assignDefaultParameters() {
		for (int j = 0; j < NUM_SPECIES; j++) {
			txtCounts[j].setText(Simulator.getDefaultInitialCounts()[j] + "");
			for (int k = 1; k < DESCRIPTIONS[j].length; k++) {
				txtParmValues[j][k - 1].setText(sFormat(Simulator.getDefaultParameters()[j][k - 1]));
			}
		}
	}

	/**
	 * Private method. Clears the simulation graph area.
	 */
	private void clearGraph() {
		final Graphics brush = pnlSimulation.getGraphics();
		brush.setColor(Color.white);
		brush.fillRect(0, 0, pnlSimulation.getWidth(), pnlSimulation.getHeight());
	}

	/**
	 * Private method. Sets up the panels by specifying sizes and borders and
	 * adding components.
	 */
	private void drawAxes(JPanel panel) {
		final Graphics g = panel.getGraphics();
		g.setColor(Color.GRAY);
		g.drawLine(X_OFFSET, Y_OFFSET, X_OFFSET, GRAPH_HEIGHT - Y_OFFSET);
		g.drawLine(X_OFFSET, GRAPH_HEIGHT - Y_OFFSET, STEPS + X_OFFSET, GRAPH_HEIGHT - Y_OFFSET);
		g.drawString("500 (population)", X_OFFSET - 15, Y_OFFSET - 2);
		g.drawString("600 (time)", STEPS + X_OFFSET - 10, GRAPH_HEIGHT - Y_OFFSET + 15);

	}

	/**
	 * Private method. Plots a point on a graph.
	 *
	 * @param panel
	 *            Panel where the graph is drawn.
	 * @param x
	 *            x-coordinate of the point
	 * @param y
	 *            y-coordinate of the point
	 * @param c
	 *            color of the point
	 */
	private void plotPoint(JPanel panel, int x, int y, Color c) {
		final Graphics g = panel.getGraphics();
		g.setColor(c);
		g.fillOval(x + X_OFFSET, GRAPH_HEIGHT - y - Y_OFFSET, POINT_SIZE, POINT_SIZE);
	}

	/**
	 * Private method. Runs the simulation and draws the population graph.
	 */
	private void run() {
		drawAxes(pnlSimulation);
		for (int i = 1; i <= STEPS; i++) {
			final int[] populations = simulator.getPopulations();
			for (int k = 0; k < NUM_SPECIES; k++) {
				plotPoint(pnlSimulation, i, populations[k], speciesColors[k]);
			}
			simulator.step();
			try {
				Thread.sleep(3);
			} catch (final InterruptedException e) {
				continue;
			}
		}
	}

	/**
	 * Private method. Creates initial dialog for colors and names of species.
	 */
	private void setNamesColors() {
		final Container initContainer = jfInit.getContentPane();
		final JPanel jpInfo = new JPanel();
		jpInfo.setLayout(new GridLayout(NUM_SPECIES, 3));
		jfInit.setTitle("Set Species Names and Colors");
		for (int k = 0; k < NUM_SPECIES; k++) {
			jpInfo.add(new JLabel(SPECIES_LABELS[k] + ": "));
			txtName[k] = new JTextField(speciesNames[k]);
			jpInfo.add(txtName[k]);
			colorPicker[k] = new ColorSetter(speciesColors[k], k);
			jpInfo.add(colorPicker[k]);
		}
		initContainer.add(jpInfo, BorderLayout.CENTER);
		initContainer.add(btnClose, BorderLayout.SOUTH);
		jfInit.setSize(300, 150);
		jfInit.setVisible(true);
	}

	/**
	 * Private method. Adds buttons to the panels and sets up the input panel.
	 */
	private void setUpInputPanels() {
		pnlSimulation.setBackground(Color.WHITE);
		pnlButtons.add(btnStart);
		pnlButtons.add(btnReset);
		pnlButtons.add(btnQuit);
		addWidgetsToInputPanel();
		pnlInput.setLayout(new BoxLayout(pnlInput, BoxLayout.LINE_AXIS));
		for (int k = 0; k < NUM_SPECIES; k++) {
			pnlInput.add(pnlSpecies[k]);
		}
		pnlSimulation.setPreferredSize(new Dimension(GRAPH_WIDTH, GRAPH_HEIGHT));
	}

	/**
	 * Private method. Creates the main simulation window.
	 */
	private void setUpSimulationWindow() {
		setUpInputPanels();

		// Add the panels to the window. Set its size, location, title
		final Container c = getContentPane();
		c.add(pnlButtons, BorderLayout.NORTH);
		c.add(pnlSimulation, BorderLayout.CENTER);
		c.add(pnlInput, BorderLayout.SOUTH);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocation(SCREEN_OFFSET, SCREEN_OFFSET);
		setTitle(TITLE);

		// Finish setting up the frame. Exit the application when the user
		// closes the window.
		pack();
		setVisible(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Private method. Instantiates the simulator.
	 */
	private void setUpSimulator() {
		final String[] counts = new String[3];
		final String[][] parms = new String[3][];
		for (int j = 0; j < 3; j++) {
			counts[j] = txtCounts[j].getText();
			parms[j] = new String[DESCRIPTIONS[j].length - 1];
			for (int k = 0; k < DESCRIPTIONS[j].length - 1; k++) {
				parms[j][k] = txtParmValues[j][k].getText();
			}
		}
		simulator = new Simulator(counts, parms);
	}

	/**
	 * Private method. Formats a double in non-scientific notation.
	 *
	 * @param d
	 *            double to be formatted
	 * @return String representation of the double
	 */
	private String sFormat(double d) {
		return String.format("%f", d);
	}

}