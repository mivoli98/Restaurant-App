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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class ManagerPage extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static Connection conn = connect_database();
	
	//private ArrayList<String> menu_array = new ArrayList<String>(); //itemID, itemName, itemPrice
	//private ArrayList<String> cust_array = new ArrayList<String>(); //itemID, itemName, itemPrice
	
	private JPanel ManagerPane;
	private JLabel lblNewLabel_3;
	private JTextField item_quantity;
	private JTextField item_price;
	private JLabel lblNewLabel_6;
	private static JTable MenuTable;
	private JTable TrendsTable;
	private static JTable InvTable;
	private JTable RecTable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerPage frame = new ManagerPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**Prints everything from the menu SQL table into the window for the manager to see.
	 * Executes a query and goes row by row to insert into the window's JTable.*/
	public static void printMenu() {
		String item_name = "";
		String item_price = "";
		String item_type = "";
		String item_id = "";
		
		try{
			Statement menu_stmt = conn.createStatement();							
			String sqlStatement = "SELECT itemid, itemname, itemprice, itemtype FROM menu ORDER BY itemprice DESC";
			ResultSet query_results = menu_stmt.executeQuery(sqlStatement);
					
			DefaultTableModel model = (DefaultTableModel) MenuTable.getModel();

			if (MenuTable.getRowCount() > 0) { //resetting table
				model.setNumRows(0);
			}
			
			if(MenuTable.getColumnCount() == 0) {
				model.addColumn("Item ID");
				model.addColumn("Item Name");
				model.addColumn("Item Price");
				model.addColumn("Item Type");
			}

			model.addRow(new Object[] {"Item ID", "Item Name", "Item Price", "Item Type"});
			
			while (query_results.next()) {
				item_id = query_results.getString("itemid");
				item_name = query_results.getString("itemname");
				item_price = query_results.getString("itemprice");
				item_type = query_results.getString("itemtype");

				model.addRow(new Object[] {item_id, item_name, item_price, item_type});
			}			
		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Error accessing Database.");
		}
	}
	
	/**Updates the menu with the new item price that was entered.*/
	public void updateMenu(String item_id, String item_price) {
		
		try{
			//float price_f = Float.parseFloat(item_price);
			
			Statement menu_stmt = conn.createStatement();
			String sqlStatement = "UPDATE menu SET itemprice=" + item_price + " WHERE itemid=\'" + item_id + "\' ";
			//String sqlStatement = "UPDATE menu SET itemprice=? WHERE itemid = ?";

			//PreparedStatement p = conn.prepareStatement(sqlStatement);
			//p.setString(1, item_price);
			//p.setFloat(2, price_f);
			
			menu_stmt.executeUpdate(sqlStatement);
			//p.executeUpdate();
			
			printMenu();
			
		} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+e.getMessage());
				System.exit(0);
		}
		
	}
	
	/**This prints the inventory SQL table into the inventory for the manager to see.*/
	public static void printInventory() {
		String custom_id = "";
		String custom_name = "";
		String total_count = "";
		
		try{
			Statement menu_stmt = conn.createStatement();							
			String sqlStatement = "SELECT customid, customname, totalcount FROM inventory1 order by cast(substr(customid, 2, 10) as int)";
			ResultSet query_results = menu_stmt.executeQuery(sqlStatement);
					
			DefaultTableModel model = (DefaultTableModel) InvTable.getModel();

			if (InvTable.getRowCount() > 0) { //resetting table
				model.setNumRows(0);
			}
			
			if(InvTable.getColumnCount() == 0) {
				model.addColumn("Custom ID");
				model.addColumn("Customn Name");
				model.addColumn("Total Count");
			}

			model.addRow(new Object[] {"Custom ID", "Custom Name", "Total Count"});
			
			while (query_results.next()) {
				custom_id = query_results.getString("customid");
				custom_name = query_results.getString("customname");
				total_count = query_results.getString("totalcount");

				model.addRow(new Object[] {custom_id, custom_name, total_count});
			}			
		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Error accessing Database.");
		}

	}
	
	/**Print the trends SQL table for the manager.*/
	public void printTrends() {
		String fav_e = "";
		String fav_s = "";
		String fav_b = "";
		String fav_d = "";
		ArrayList<String> trends = new ArrayList<String>();
		
		try{
			Statement menu_stmt = conn.createStatement();							
			String sqlStatement = "SELECT entree, side, beverage, desserts FROM trends WHERE year='2020' AND month='12'";
			ResultSet query_results = menu_stmt.executeQuery(sqlStatement);
			
			DefaultTableModel model = (DefaultTableModel) TrendsTable.getModel();

			if (TrendsTable.getRowCount() > 0) { //resetting table
				model.setNumRows(0);
			}
			
			if(TrendsTable.getColumnCount() == 0) {
				model.addColumn("Popular Entree");
				model.addColumn("Popular Side");
				model.addColumn("Popular Beverage");
				model.addColumn("Popular Dessert");
			}

			model.addRow(new Object[] {"Popular Entree", "Popular Side", "Popular Beverage", "Popular Dessert"});
			
			while (query_results.next()) {
				fav_e = query_results.getString("entree");
				fav_s= query_results.getString("side");
				fav_b= query_results.getString("beverage");
				fav_d= query_results.getString("desserts");

				model.addRow(new Object[] {fav_e, fav_s, fav_b, fav_d});
			}
			
			
		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Error accessing trends.");
		}
		
		trends.add(fav_e);
		trends.add(fav_s);
		trends.add(fav_b);
		trends.add(fav_d);
		
		printRecommended(trends);
		
	}
	
	/**Prints the recommended prices for the manager. The recommendations take in the trends
	 * and adds an extra percentage to each item price.*/
	public void printRecommended(ArrayList<String> trends) {
		
		ArrayList<String> foods = new ArrayList<String>();
		ArrayList<String> prices = new ArrayList<String>();
		
		try{
			DefaultTableModel model = (DefaultTableModel) RecTable.getModel();

			if (RecTable.getRowCount() > 0) { //resetting table
				model.setNumRows(0);
			}
			
			if(RecTable.getColumnCount() == 0) {
				model.addColumn("Popular Food");
				model.addColumn("Current Price");
				model.addColumn("Recommended Price");
			}

			model.addRow(new Object[] {"Popular Food", "Current Price", "Recommended Price"});
			
			Statement menu_stmt = conn.createStatement();
			
			for(int i=0; i<trends.size(); ++i) {
				String sqlStatement = "SELECT itemname FROM menu WHERE itemid='" + trends.get(i) + "' ";
				ResultSet query_results = menu_stmt.executeQuery(sqlStatement);
				
				if (query_results.next()) {
					foods.add(query_results.getString("itemname"));
				}
			}
			
			for(int i=0; i<foods.size(); ++i) {
				String sqlStatement = "SELECT itemprice FROM menu WHERE itemname='" + foods.get(i) + "' ";
				ResultSet query_results = menu_stmt.executeQuery(sqlStatement);
				
				if (query_results.next()) {
					prices.add(query_results.getString("itemprice"));
				}
			}
			
			float price_f = 0;
			String price_s = "";
			double increase = 0.15; //percentage increase
			
			DecimalFormat df = new DecimalFormat("0.00");
			
			for(int i=0; i<foods.size(); ++i) {
				price_s = trimString(prices.get(i));
				
				price_f = Float.parseFloat(price_s);
								
				model.addRow(new Object[] {foods.get(i), prices.get(i), "$" + df.format(price_f * (1 + increase))});
			}
			
		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Error accessing recommendations.");
		}
	}
	
	/**Helper function that trims the first character in a string, this is mainly used to trim the dollar 
	 *sign in the price so it can be manipulated as a float.*/
	public static String trimString(String input) {
		int size = input.length();
		input = input.substring(1,size);
		return input;
	}
	
	/**This function create a connection to the SQL database and returns that connection.*/
	public static Connection connect_database() {
		Connection conn = null;
				
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/db901_group12_project2",
			dbSetup.user, dbSetup.pswd);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}//end try catch
		return conn;
	}
	
	/**This allows the manager to add availability to inventory items. Takes in a the custom id and 
	 * the number to update it to.*/
	public static void addInventory(String custom_id, int quantity) {
		
		try{
			
			Statement menu_stmt = conn.createStatement();
			String sqlStatement = "UPDATE inventory1 SET totalcount='" + quantity + "' WHERE customid='" + custom_id + "' ";
			
			menu_stmt.executeUpdate(sqlStatement);
			
			printMenu();
			printInventory();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Error updating inventory.");
		}
		JOptionPane.showMessageDialog(null,"Inventory successfully updated.");
	}
	
	/**
	 * Create the frame.
	 */
	public ManagerPage() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ManagerPage.class.getResource("/assets/team-logo.jpg")));
		setTitle("Manager View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1920, 1080);
		ManagerPane = new JPanel();
		
		ManagerPane.setBackground(new Color(250, 250, 210));
		ManagerPane.setForeground(new Color(0, 0, 0));
		ManagerPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(ManagerPane);
		
		JLabel lblNewLabel = new JLabel("Menu");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 40));
		
		MenuTable = new JTable();
		MenuTable.setFont(new Font("Arial", Font.PLAIN, 15));

		InvTable = new JTable();
		InvTable.setFillsViewportHeight(true);
		TrendsTable = new JTable();
		RecTable = new JTable();
		
		
		printMenu();
		printInventory();
		printTrends();
		
		JLabel lblNewLabel_1 = new JLabel("Trends");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 40));


		lblNewLabel_3 = new JLabel("Price Recommendations");
		lblNewLabel_3.setFont(new Font("Arial", Font.BOLD, 30));
		
		item_quantity = new JTextField();
		item_quantity.setColumns(10);
		
		item_price = new JTextField();
		item_price.setColumns(10);
		
		lblNewLabel_6 = new JLabel("Inventory");
		lblNewLabel_6.setFont(new Font("Arial", Font.BOLD, 30));
		
		final JComboBox<String> changeItem_comboBox = new JComboBox<String>();
		changeItem_comboBox.setFont(new Font("Arial", Font.PLAIN, 20));
		changeItem_comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Select Item:", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "S1", "S2", "S3", "S4", "B1", "B2", "B3", "B4", "D1", "D2", "D3", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15", "c16", "c17", "c18", "c19"}));
		
		String customs_Arr[] = new String[]{"c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15", "c16", "c17", "c18", "c19"};
		String menu_item_arr[] = new String[]{"E1", "E2", "E3", "E4", "E5", "E6", "E7", "S1", "S2", "S3", "S4", "B1", "B2", "B3", "B4", "D1", "D2", "D3"};
		
		final List<String> cust_arr = Arrays.asList(customs_Arr);
		final List<String> menu_arr = Arrays.asList(menu_item_arr);

		/*Changes the price of an item on the menu.*/
		JButton changePrice_btn = new JButton("Change Price");
		changePrice_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item_id = "";
				item_id = changeItem_comboBox.getSelectedItem().toString();
				
				if(item_id == "Select Item:") {//making sure the input is valid
					JOptionPane.showMessageDialog(null,"Please select an item first.");
				} else if( item_price.getText().isBlank() ) {
					JOptionPane.showMessageDialog(null,"Invalid price.");
				} else if (cust_arr.contains(item_id)) {
					JOptionPane.showMessageDialog(null,"Please select an item from the menu, not a customization.");
				} else {
					updateMenu(item_id, item_price.getText());
				}
				
			}
		});
		
		changePrice_btn.setFont(new Font("Arial", Font.PLAIN, 20));
		
		/*Button to take user back to the login page.*/
		JButton logout_btn = new JButton("Logout");
		logout_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManagerPane.setVisible(false);
				dispose();
				
				MainWindow.main(null);
			}
		});
		logout_btn.setFont(new Font("Arial", Font.PLAIN, 20));
		
		/*Changes the availability of an item in the inventory.*/
		JButton changeQuantity_btn = new JButton("Change Availability");
		changeQuantity_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String item_id = "";
				item_id = changeItem_comboBox.getSelectedItem().toString();
				
				if(item_id == "Select Item:") {//making sure the input is a valid one
					JOptionPane.showMessageDialog(null,"Please select an item first.");
				} else if( item_quantity.getText().isBlank() ){ 
					JOptionPane.showMessageDialog(null,"Please enter an integer for the item.");
				} else if (menu_arr.contains(item_id)) {
					JOptionPane.showMessageDialog(null,"Please select an item from the customizations, not from menu.");
				} else {
					int item_avail = Integer.parseInt(item_quantity.getText().toString());
					addInventory(item_id, item_avail);
				}
			}
		});
		changeQuantity_btn.setFont(new Font("Arial", Font.PLAIN, 20));
		
		
		
		GroupLayout gl_ManagerPane = new GroupLayout(ManagerPane);
		gl_ManagerPane.setHorizontalGroup(
			gl_ManagerPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_ManagerPane.createSequentialGroup()
					.addGroup(gl_ManagerPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_ManagerPane.createSequentialGroup()
							.addGap(500)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 731, Short.MAX_VALUE)
							.addComponent(lblNewLabel_1)
							.addGap(382))
						.addGroup(gl_ManagerPane.createSequentialGroup()
							.addGap(199)
							.addGroup(gl_ManagerPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_ManagerPane.createSequentialGroup()
									.addComponent(changeItem_comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addGroup(gl_ManagerPane.createParallelGroup(Alignment.LEADING, false)
										.addGroup(gl_ManagerPane.createSequentialGroup()
											.addComponent(item_quantity, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE)
											.addGap(18)
											.addComponent(changeQuantity_btn, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_ManagerPane.createSequentialGroup()
											.addComponent(item_price, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
											.addGap(18)
											.addComponent(changePrice_btn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
								.addComponent(MenuTable, GroupLayout.PREFERRED_SIZE, 732, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_ManagerPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_ManagerPane.createParallelGroup(Alignment.TRAILING)
									.addGroup(gl_ManagerPane.createSequentialGroup()
										.addGap(304)
										.addGroup(gl_ManagerPane.createParallelGroup(Alignment.TRAILING, false)
											.addComponent(TrendsTable, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addComponent(InvTable, GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
											.addComponent(RecTable, GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE))
										.addGap(236))
									.addGroup(gl_ManagerPane.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblNewLabel_3)
										.addGap(261)))
								.addGroup(gl_ManagerPane.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblNewLabel_6, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
									.addGap(354)))))
					.addGap(17))
				.addGroup(gl_ManagerPane.createSequentialGroup()
					.addContainerGap(1783, Short.MAX_VALUE)
					.addComponent(logout_btn, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE))
		);
		gl_ManagerPane.setVerticalGroup(
			gl_ManagerPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_ManagerPane.createSequentialGroup()
					.addGroup(gl_ManagerPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_ManagerPane.createSequentialGroup()
							.addComponent(logout_btn, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addGap(36)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addGap(18))
						.addGroup(gl_ManagerPane.createSequentialGroup()
							.addComponent(lblNewLabel_1)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_ManagerPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_ManagerPane.createSequentialGroup()
							.addGroup(gl_ManagerPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_ManagerPane.createSequentialGroup()
									.addGap(94)
									.addComponent(lblNewLabel_3)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(RecTable, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE))
								.addComponent(MenuTable, GroupLayout.PREFERRED_SIZE, 310, GroupLayout.PREFERRED_SIZE))
							.addGap(34)
							.addGroup(gl_ManagerPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_ManagerPane.createSequentialGroup()
									.addGroup(gl_ManagerPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_ManagerPane.createParallelGroup(Alignment.BASELINE)
											.addComponent(changeItem_comboBox, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
											.addComponent(item_price, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_ManagerPane.createSequentialGroup()
											.addGap(10)
											.addComponent(changePrice_btn)))
									.addGap(19)
									.addGroup(gl_ManagerPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(item_quantity, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
										.addComponent(changeQuantity_btn)))
								.addComponent(InvTable, GroupLayout.PREFERRED_SIZE, 409, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_ManagerPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(TrendsTable, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lblNewLabel_6, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addGap(415)))
					.addContainerGap(127, Short.MAX_VALUE))
		);
		ManagerPane.setLayout(gl_ManagerPane);
	}
}
