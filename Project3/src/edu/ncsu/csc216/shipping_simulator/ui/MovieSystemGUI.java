package edu.ncsu.csc216.shipping_simulator.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.ncsu.csc216.flix_2.customer.CustomerAccountManager;
import edu.ncsu.csc216.flix_2.customer.MovieCustomerAccountSystem;
import edu.ncsu.csc216.flix_2.rental_system.DVDRentalSystem;
import edu.ncsu.csc216.flix_2.rental_system.RentalManager;

/**
 * GUI client
 *
 * @author Collin
 *
 */
public class MovieSystemGUI extends JFrame implements ActionListener {
	/** Constant for Serial Version */
	private static final long serialVersionUID = 1L;
	/** String constant for the title of the application */
	private static final String APP_TITLE = "Movie System";
	private CardLayout cardLayout;
	private JPanel panel;
	// private JButton btnLogin = new JButton("Login");
	// private JButton btnAddNewCust = new JButton("Add New Customer");
	// private JButton btnCancelAccount = new JButton("Cancel Account");
	// private JButton btnQuit = new JButton("Quit");
	// private JButton btnBrowse = new JButton("Browse");
	// private JButton btnShowMyQueue = new JButton("Show My Queue");
	// private JButton btnLogout = new JButton("Logout");
	// private static final String LOGIN_PANEL = "LoginPanel";
	// private static final String BROWSE_PANEL = "BrowsePanel";
	// private static final String SHOW_MY_QUEUE_PANEL = "ShowMyQueuePanel";
	private static CustomerAccountManager accountManager;
	private static RentalManager rentals;

	private LoginPanel pnlLogin = new LoginPanel();
	// private Browse pnlBrowse = new BrowsePanel();
	// private ShowPanel pnlShow = new ShowPanel();

	/**
	 * Default constructor for GUI
	 */
	public MovieSystemGUI() {
		super();
		this.setTitle(APP_TITLE);
		this.setSize(800, 400);
		this.setLocation(50, 50);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		panel = new JPanel();
		cardLayout = new CardLayout();
		panel.setLayout(cardLayout);
		panel.add(pnlLogin, "LoginPanel");
		// panel.add(pnlBrowse, "BrosePanel");
		// panel.add(pnlShow, "ShowPanel");
		cardLayout.show(panel, "MovieSystemPanel");
		Container c = getContentPane();
		c.add(panel, BorderLayout.CENTER);
		this.setVisible(true);

	}

	/**
	 * Main method.
	 *
	 * @param args
	 *            for file input
	 */
	public static void main(String[] args) {
		new MovieSystemGUI();
		rentals = new DVDRentalSystem(args[0]);
		accountManager = new MovieCustomerAccountSystem(rentals);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object blah = e.getSource();
		blah.toString();
	}

	/**
	 * Handles the Login Panel
	 *
	 * @author Collin
	 *
	 */
	private class LoginPanel extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;
		private JButton btnLogin;
		private JTextField usernameTXT;
		private JTextField passwordTXT;

		public LoginPanel() {
			super(new BorderLayout());
			usernameTXT = new JTextField(10);
			passwordTXT = new JTextField(10);
			btnLogin = new JButton("Login");
			btnLogin.addActionListener(this);
			JPanel pnlActions = new JPanel();
			pnlActions.setLayout(new GridLayout(2, 3));
			pnlActions.add(usernameTXT);
			pnlActions.add(passwordTXT);
			pnlActions.add(btnLogin);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnLogin) {
				try {
					accountManager.login(usernameTXT.getText(), passwordTXT.getText());
				} catch (IllegalStateException e1) {
					JOptionPane.showMessageDialog(MovieSystemGUI.this, e1.getMessage());
				}
			}
		}

	}
}
