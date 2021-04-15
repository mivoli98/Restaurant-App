import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;

public class MainWindow extends JFrame{
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static Connection conn = connect_database();
	
	private JFrame formGui;
	private JTextField c_name_field;
	private JTextField c_id_field;
	private JTextField m_name_field;
	private JTextField m_id_field;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.formGui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**This function create a connection to the SQL database and returns that connection.**/
	public static Connection connect_database() {
		Connection connect = null;
				
		try {
			Class.forName("org.postgresql.Driver");
			connect = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/db901_group12_project2",
			dbSetup.user, dbSetup.pswd);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}//end try catch
		return connect;
	}
	
	/**This function makes sure that the account last name and id given is real.
	 * Function returns true if the account is found in the customers or managers SQL table
	 * and returns false if not. Its parameters consist of the last name, the ID, and the 
	 * status of the user. The status is either manager or customer.*/
	public static boolean isValidAccount(String lastname, String id, String status) {

		String query_lastname = "";

		String get = "";
		String sqlStatement = "";
		
		/*If statement to input the correct SQL statement given the user's status.*/
		if (status == "customer") {
			sqlStatement = "SELECT * FROM customers WHERE lastname='" + lastname + "' AND customerid='" + id + "' LIMIT 5";
			get = "lastname";
		} else if (status == "manager") {
			sqlStatement = "SELECT * FROM managers WHERE name='" + lastname + "' AND managerid='" + id + "' ";
			get = "name";	
		} else {
			return false;
		}
		
		try{
			Statement menu_stmt = conn.createStatement();
			ResultSet query_results = menu_stmt.executeQuery(sqlStatement);
			
			if (query_results.next())
				query_lastname = query_results.getString(get); //get query result
			
			
			if (query_lastname == "") //last name and customer id do not match then result will be blank; false account
				return false;
			else 
				return true;		//last name and id exist; valid account
			
		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Error accessing Database.");
		}
		return false;
	}
	
	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		formGui = new JFrame();
		formGui.setForeground(Color.WHITE);
		formGui.setBackground(Color.WHITE);
		formGui.setResizable(false);
		formGui.setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/assets/team-logo.jpg")));
		formGui.setTitle("Project 2");
		formGui.setBounds(100, 100, 1280, 720);
		formGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		formGui.getContentPane().setLayout(new BoxLayout(formGui.getContentPane(), BoxLayout.X_AXIS));
		//formGui.setExtendedState(formGui.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 255, 240));
		panel.setForeground(Color.LIGHT_GRAY);
		formGui.getContentPane().add(panel);
		
		JLabel lblNewLabel_1 = new JLabel("Login");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 30));
		
		JLabel menu_picture = new JLabel("");
		menu_picture.setIcon(new ImageIcon(MainWindow.class.getResource("/assets/subway-pic4.jpg")));
				
		c_name_field = new JTextField();
		c_name_field.setFont(new Font("Tahoma", Font.PLAIN, 14));
		c_name_field.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Customers");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 22));
		
		JLabel lblNewLabel_2 = new JLabel("Last Name");
		lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 20));
		
		c_id_field = new JTextField();
		c_id_field.setFont(new Font("Tahoma", Font.PLAIN, 14));
		c_id_field.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Customer ID");
		lblNewLabel_3.setFont(new Font("Arial", Font.PLAIN, 20));
		
		m_name_field = new JTextField();
		m_name_field.setFont(new Font("Tahoma", Font.PLAIN, 14));
		m_name_field.setColumns(10);
		
				
		JLabel lblNewLabel_5 = new JLabel("Managers");
		lblNewLabel_5.setFont(new Font("Arial", Font.PLAIN, 22));
		
		m_id_field = new JTextField();
		m_id_field.setFont(new Font("Tahoma", Font.PLAIN, 14));
		m_id_field.setColumns(10);
		
		
		JLabel lblNewLabel_6 = new JLabel("Last Name");
		lblNewLabel_6.setFont(new Font("Arial", Font.PLAIN, 20));
		
		JLabel lblNewLabel_7 = new JLabel("Manager ID");
		lblNewLabel_7.setFont(new Font("Arial", Font.PLAIN, 20));
		
		/*Handles the managers login.*/
		JButton managers_login = new JButton("Login");
		managers_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String m_name = m_name_field.getText();
				String m_id = m_id_field.getText();
				
				if ( isValidAccount(m_name, m_id, "manager") ) {
	                formGui.dispose();
	                ManagerPage manager_page = new ManagerPage();
	                manager_page.setVisible(true);
	                
	                manager_page.setExtendedState(manager_page.getExtendedState() | JFrame.MAXIMIZED_BOTH); //maximizes the window
				} else {
					JOptionPane.showMessageDialog(null,"Incorrect Login.");
				}
				
			}
		});
		managers_login.setFont(new Font("Arial", Font.PLAIN, 20));
		
		/*Handles the customers login.*/
		JButton customers_login = new JButton("Login");
		customers_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String c_name = c_name_field.getText().toUpperCase();
				String c_id = c_id_field.getText().toUpperCase();				
				
				if ( isValidAccount(c_name, c_id, "customer") ) {
	                formGui.dispose();
	                CustomerPage customer_page = new CustomerPage(c_name, c_id);
	                customer_page.setVisible(true);
	                
	                customer_page.setExtendedState(customer_page.getExtendedState() | JFrame.MAXIMIZED_BOTH); //maximizes the window
				} else {
					JOptionPane.showMessageDialog(null,"Incorrect Login.");
				}
				
			}
		});
		
		customers_login.setFont(new Font("Arial", Font.PLAIN, 20));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(122)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNewLabel_6)
								.addComponent(lblNewLabel_2)
								.addComponent(lblNewLabel_7)
								.addComponent(lblNewLabel_3))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel)
										.addComponent(customers_login)
										.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
											.addComponent(c_name_field, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
											.addComponent(c_id_field, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE))
										.addComponent(lblNewLabel_5, GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
										.addGroup(gl_panel.createSequentialGroup()
											.addGap(1)
											.addComponent(m_name_field, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)))
									.addPreferredGap(ComponentPlacement.RELATED, 162, Short.MAX_VALUE))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(m_id_field, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(managers_login)
									.addPreferredGap(ComponentPlacement.RELATED))))
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
							.addGap(214)))
					.addComponent(menu_picture, GroupLayout.PREFERRED_SIZE, 644, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addComponent(menu_picture, GroupLayout.PREFERRED_SIZE, 681, Short.MAX_VALUE)
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addGap(32)
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(c_name_field, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_2))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(c_id_field, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_3))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(customers_login, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addGap(81)
					.addComponent(lblNewLabel_5)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_name_field, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_6))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(m_id_field, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_7))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(managers_login)
					.addGap(182))
		);
		panel.setLayout(gl_panel);
	}
}
