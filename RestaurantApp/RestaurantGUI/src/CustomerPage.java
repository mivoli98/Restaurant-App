import java.awt.EventQueue;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.text.*;

import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Date;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;


public class CustomerPage extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/*Sets up the connection for the rest of the page*/
	private static Connection conn = connect_database();

	/*These are the order arrays, when a customer adds an item, the item ID is added to the array.
	 *The order_menu_array lists the item IDs that were added that are in the menu, not in the customizations.
	 *The order_cust_array lists the item IDs that were added that are in the customizations, not in the menu.*/
	private ArrayList<String> order_menu_array = new ArrayList<String>();
	private ArrayList<String> order_cust_array = new ArrayList<String>();
	
	/*Arrays that are used to map the menu items with the item ID and item name.*/
	private ArrayList<String> menu_array = new ArrayList<String>(); //itemID, itemName, itemPrice
	private ArrayList<String> cust_array = new ArrayList<String>(); //itemID, itemName, itemPrice
	
	private String customerFirstName;
	private String customerLastName;
	
	private JPanel CustomerPane;
	private JTable MenuTable;
	private JTable rec_table;
	private JTable c_table;
	private JLabel dess_label;
	private JTable order_table;
	private JTable trends_table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String name = "TEST";
					String id = "CUSTOMER";
					CustomerPage frame = new CustomerPage(name, id);
					frame.setVisible(true);
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
	
	/**Closes the connection to the SQL database*/
	public static void close_connection(Connection connect) {
		try {
			connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	/**This function places the order into the orders SQL table, it joins the order_menu array and order_cust_array 
	 *into their respective strings. These strings are entered into the orders table in the row containing the 
	 *customer's login. The customer name comes from the login page that the user inputs.*/
	public void placeOrder(String f_name, String l_name, String main_order, String cust_order) {

		try{
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

			Statement order_stmt = conn.createStatement();
			String sqlStatement = "INSERT INTO orders (firstname, lastname, date, items, addCust) VALUES (' "+ f_name + "','" + l_name + "','" + simpleDateFormat.format(new Date()) + "','" + main_order + "','"+ cust_order +"')";

			order_stmt.executeUpdate(sqlStatement);

		} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+e.getMessage());
				System.exit(0);
		}

		JOptionPane.showMessageDialog(null,"Order Confirmed!");
		printRec();

	}
	
	/**Prints the menu from the menu SQL table to the menu section of the customer page.*/
	public void printMenu() {

		String item_id = "";
		String item_name = "";
		String item_price = "";
		String item_type = "";
		String item_custom = "";

		try{
			
			Statement menu_stmt = conn.createStatement();
			String sqlStatement = "SELECT itemid, itemname, itemprice, itemtype, customid FROM menu ORDER BY itemprice DESC";
			ResultSet query_results = menu_stmt.executeQuery(sqlStatement);

			Object header[] = new Object[] {"Item ID", "Item Name", "Item Price", "Item Type", "Ingredients"};

			DefaultTableModel model = (DefaultTableModel) MenuTable.getModel();


			model.addColumn("Item ID");
			model.addColumn("Item Name");
			model.addColumn("Item Price");
			model.addColumn("Item Type");
			model.addColumn("Ingredients");
			model.addRow(header);

			
			while (query_results.next()) {

				item_id = query_results.getString("itemid");
				item_name = query_results.getString("itemname");
				item_price = query_results.getString("itemprice");
				item_type = query_results.getString("itemtype");
				item_custom = query_results.getString("customid");

				menu_array.add(item_id);
				menu_array.add(item_name);
				menu_array.add(item_price);

				model.addRow(new Object[] {item_id, item_name, item_price, item_type, item_custom});
			}

		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Error accessing menu.");
		}
	}
	
	/**Print the customizations table to the customer from the customizations SQL table.*/
	public void printCustoms() {
		String c_id = "";
		String c_price = "";
		String c_name = "";

		try{
			Statement menu_stmt = conn.createStatement();
			String sqlStatement = "SELECT customid, customprice, customname FROM customizations ORDER BY CAST(substr(customid, 2, 10) AS int)";
			ResultSet query_results = menu_stmt.executeQuery(sqlStatement);

			DefaultTableModel model1 = (DefaultTableModel) c_table.getModel();

			model1.addColumn("Custom ID");
			model1.addColumn("Custom Price");
			model1.addColumn("Custom Name");

			model1.addRow(new Object[] {"Custom ID", "Custom Price", "Custom Name"});

			while (query_results.next()) {

				c_id = query_results.getString("customid");
				c_price = query_results.getString("customprice");
				c_name = query_results.getString("customname");

				cust_array.add(c_id);
				cust_array.add(c_name);
				cust_array.add(c_price);

				model1.addRow(new Object[] {c_id, c_name, c_price});
			}

		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Error accessing customizations.");
		}
	}

	/**This function prints recommendations to the customer. These recommendations are from
	 *the most recent orders of the customer. */
	public void printRec() {
		String order = "";

		DefaultTableModel model = (DefaultTableModel) rec_table.getModel();

		try {
			Statement rec_stmt = conn.createStatement();
			String sqlStatement = "SELECT items, addcust, remcust FROM orders WHERE firstname=' " + customerFirstName + "' AND lastname='" + customerLastName + "' ORDER BY date DESC limit 3";
			ResultSet query_results = rec_stmt.executeQuery(sqlStatement);

			if(rec_table.getRowCount() > 0) {
				model.setNumRows(0);
			}

			if(rec_table.getColumnCount()==0) {
				model.addColumn("Order");
			}

			model.addRow(new Object[] {"Order"});

			while(query_results.next()) {
				order = query_results.getString("items");
				model.addRow(new Object[] {order});
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Error making recommendation.");
		}
	}

	/**This function returns the first name of the customer. It gets the first name from the customer ID used to login.*/
	public static String getFirstName(String customerId) {

		String first_name = "";

		try {
			Statement menu_stmt = conn.createStatement();

			String sqlStatement = "SELECT firstname FROM customers WHERE customerid='" + customerId + "' ";
			ResultSet query_results = menu_stmt.executeQuery(sqlStatement);

			if (query_results.next()) {
				first_name = query_results.getString("firstname");
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Error accessing customer name.");
		}


		return first_name;
	}

	/**This is an enum that maps the items from the item name to the item ID.*/
	enum MenuItems {
		E1("Turkey Sandwich"), E2("Salami Sandwich"), E3("Tuna Sandwich"), E4("Oven Roast Chicken Sandwich"), E5("Veggie Sandwich"), E6("Ham Sandwich"), E7("Beef Sandwich"),
		S1("Baguette"), S2("Fries"), S3("Salad"), S4("Onion Rings"),
		B1("Coke"), B2("Pepsi"), B3("Iced Tea"), B4("Gatorade"),
	    D1("Chocolate Chip Cookie"), D2("Brownie"), D3("Snickerdoodle Cookie"),

	    c1("turkey"), c2("tuna"), c3("chicken"), c4("ham"), c5("beef"), c6("salami"), c7("ranch"), c8("mayo"), c9("ketchup"), c10("tomato"),
	    c11("lettuce"), c12("veggie patty"), c13("bread"), c14("potato"), c15("onions"), c16("syrup"), c17("tea"), c18("chocolate"), c19("cinammon");

	    private final String display;

	    private MenuItems(String s) {
	        display = s;
	    }
	    public static String getItemId(String input) {
	    	for(MenuItems m : MenuItems.values()) {
	    		if (m.toString() == input)
	    			return m.name().toString();
	    	}
	    	return null;
	    }

	    @Override
	    public String toString() {
	        return display;
	    }
	}

	/**This function updates the current order that the customer is editing. It prints out the order_menu_array
	 *and the order_cust_array to the order section in the window.*/
	public void updateOrder() {

		String item_price = "";
		String item_name = "";

		float item_price_float = 0;
		float total_price = 0;

		try{
			DefaultTableModel model = (DefaultTableModel) order_table.getModel();

			if(order_table.getRowCount() > 0) {
				model.setNumRows(0);
				model.setColumnCount(0);
			}

			if(order_table.getColumnCount() == 0) {
				model.addColumn("Item");
			}

			if( (order_menu_array.size() == 0) && (order_cust_array.size() == 0) ) {
				Object header[] = new Object[] {"Add Items and View Your Order Here"};
				model.addRow(header);
			} else {
				model.addColumn("Price");
				Object header[] = new Object[] {"Your Order:", "Price: "};
				model.addRow(header);
			}


			for(String s: order_menu_array) {
				item_price = menu_array.get( menu_array.indexOf(s)+2 );
				item_name = menu_array.get( menu_array.indexOf(s)+1 );

				model.addRow(new Object[] {item_name, item_price});

				item_price = trimString(item_price);

				item_price_float = Float.parseFloat(item_price);
				total_price += item_price_float;
			}

			for(String s: order_cust_array) {
				item_price = cust_array.get( cust_array.indexOf(s)+2 );
				model.addRow(new Object[] {MenuItems.valueOf(s), item_price});

				item_price = trimString(item_price);

				item_price_float = Float.parseFloat(item_price);
				total_price += item_price_float;

			}

			if( (order_menu_array.size() == 0) && (order_cust_array.size() == 0) ) {

			} else {
				DecimalFormat df = new DecimalFormat("0.00");
				model.addRow(new Object[] { "", "Total Price: $"+ df.format(total_price)});
			}


		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Error updating order.");
		}
	}

	/**Helper function that trims the first character in a string, this is mainly used to trim the dollar 
	 *sign in the price so it can be manipulated as a float.*/
	public static String trimString(String input) {
		int size = input.length();
		input = input.substring(1,size);
		return input;
	}

	/**Updates the combo box that lists all of the items currently in the order.*/
	public void updateRemoveComboBox(JComboBox<String> remove_comboBox) {
		remove_comboBox.removeAllItems();

		for(String s: order_menu_array) {
			remove_comboBox.addItem( MenuItems.valueOf(s).toString() );
		}

		for(String s: order_cust_array) {
			remove_comboBox.addItem( MenuItems.valueOf(s).toString() );
		}
	}

	/**Checks if the item is available before adding it to the order.*/
	public boolean isAvailable(String item_id) {
		
		int total = 0;
		
		try {
			ResultSet rs = null;

				if(item_id == "E1"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid='c1' OR customid='c7' OR customid='13'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				else if(item_id == "E2"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = 'c6' OR customid = 'c8' OR customid = 'c13'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				else if(item_id == "E3" || item_id == "E4" ){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = 'c3' OR customid = 'c8' OR customid = 'c13'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				else if(item_id == "E5"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = 'c10' OR customid = 'c11' OR customid = 'c12' OR customID = 'c13'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				else if(item_id == "E6"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = 'c4' OR customid = 'c7' OR customid = 'c13'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				else if(item_id == "E7"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = 'c5' OR customid = 'c7' OR customid = 'c13'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				else if(item_id == "S1"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = 'c13'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				else if(item_id == "S2"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = 'c14'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				else if(item_id == "S3"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = 'c10' OR customid = 'c11' OR customid = 'c14' OR customID = 'c7'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				else if(item_id == "S4"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = 'c15'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				else if(item_id == "B1" || item_id == "B2" || item_id == "B4"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = 'c16'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				else if(item_id == "B3"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = 'c17'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				else if(item_id == "D1"  || item_id == "D2"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = 'c18'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				else if(item_id == "D3"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = 'c19'";
					rs = menu_stmt.executeQuery(sqlStatement);
				} else { //is a customizable item
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "SELECT totalcount FROM inventory1 WHERE customid = '" + item_id + "'";
					rs = menu_stmt.executeQuery(sqlStatement);
				}
				System.out.println(item_id);
				while( rs.next() ) {
					total = rs.getInt("totalcount");
					
					if(total <  10) {
						return false;
					}
				}
				
			
		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Error checking inventory.");
		}
		
		return true;
		
	}

	/**Updates the inventory when an order is placed. Takes all of the ingredients from menu items and 
	 *subtracts from the quantity in the inventory table.*/
	public void updateInventory() {
		try{
			
			/*Loops through the customers order to see which items to decrement.*/
			for(int i = 0; i < order_menu_array.size(); i++ ){
				if(order_menu_array.get(i) == "E1"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c1' OR customid = 'c7' OR customid = 'c13'";
					menu_stmt.executeUpdate(sqlStatement);
					
				}
				else if(order_menu_array.get(i) == "E2"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c6' OR customid = 'c8' OR customid = 'c13'";
					menu_stmt.executeUpdate(sqlStatement);
					
				}
				else if(order_menu_array.get(i) == "E3" || order_menu_array.get(i) == "E4" ){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c3' OR customid = 'c8' OR customid = 'c13'";
					menu_stmt.executeUpdate(sqlStatement);	
				}
				else if(order_menu_array.get(i) == "E5"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c10' OR customid = 'c11' OR customid = 'c12' OR customID = 'c13'";
					menu_stmt.executeUpdate(sqlStatement);
				}
				else if(order_menu_array.get(i) == "E6"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c4' OR customid = 'c7' OR customid = 'c13'";
					menu_stmt.executeUpdate(sqlStatement);
					
				}
				else if(order_menu_array.get(i) == "E7"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c5' OR customid = 'c7' OR customid = 'c13'";
					menu_stmt.executeUpdate(sqlStatement);
				}
				else if(order_menu_array.get(i) == "S1"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c13'";
					menu_stmt.executeUpdate(sqlStatement);
				}
				else if(order_menu_array.get(i) == "S2"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c14'";
					menu_stmt.executeUpdate(sqlStatement);
				}
				else if(order_menu_array.get(i) == "S3"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c10' OR customid = 'c11' OR customid = 'c14' OR customID = 'c7'";
					menu_stmt.executeUpdate(sqlStatement);
				}
				else if(order_menu_array.get(i) == "S4"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c15'";
					menu_stmt.executeUpdate(sqlStatement);
				}
				else if(order_menu_array.get(i) == "B1" || order_menu_array.get(i) == "B2" || order_menu_array.get(i) == "B4"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c16'";
					menu_stmt.executeUpdate(sqlStatement);
				}
				else if(order_menu_array.get(i) == "B3"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c17'";
					menu_stmt.executeUpdate(sqlStatement);
				}
				else if(order_menu_array.get(i) == "D1"  || order_menu_array.get(i) == "D2"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c18'";
					menu_stmt.executeUpdate(sqlStatement);
				}
				else if(order_menu_array.get(i) == "D3"){
					Statement menu_stmt = conn.createStatement();
					String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = 'c19'";
					menu_stmt.executeUpdate(sqlStatement);
				} else {

				}
			}//end of for loop
			
			/*Loops through the customers order to see which customizations to directly decrement.*/
			for(int i = 0; i < order_cust_array.size(); i++ ){
				Statement menu_stmt = conn.createStatement();
				String sqlStatement = "UPDATE inventory1 SET totalcount = totalcount-1 WHERE customid = '" + order_cust_array.get(i) + "'";
				menu_stmt.executeUpdate(sqlStatement);
			}

		}
		catch (Exception e){
			JOptionPane.showMessageDialog(null,"Error updating inventory.");
		}
	}

	
	
	/**Create the frame for Customer Page.*/
	public CustomerPage(String LastName, String customerId) {

		String FirstName = getFirstName(customerId);	//gets the first name from the customer login information

		/*Sets the names to a private variable so that it can be used in all scopes of the class.*/
		customerFirstName = FirstName;
		customerLastName = LastName;

		setTitle("Customer");
		setIconImage(Toolkit.getDefaultToolkit().getImage(CustomerPage.class.getResource("/assets/team-logo.jpg")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1920, 1080);

		CustomerPane = new JPanel();
		CustomerPane.setBackground(new Color(250, 250, 210));
		CustomerPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(CustomerPane);

		/*Setting up all of the JTables in the window*/
		MenuTable = new JTable();
		printMenu();

		rec_table = new JTable();
		printRec();

		c_table = new JTable();
		printCustoms();

		order_table = new JTable();
		updateOrder();
		/*------------------------------------------*/
		
		JLabel lblNewLabel = new JLabel("Menu");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 40));

		final JComboBox<String> remove_comboBox = new JComboBox<String>();
		remove_comboBox.setFont(new Font("Arial", Font.PLAIN, 20));

		/*Place Order button and action listener.*/
		JButton  place_order_btn = new JButton("Place Order");
		place_order_btn.setFont(new Font("Arial", Font.PLAIN, 20));
		place_order_btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if( (order_menu_array.size() > 0) || (order_cust_array.size() > 0) ) {
					String orderMain = String.join(",", order_menu_array);
					

					String orderCustom = String.join(",", order_cust_array);
					

					updateOrder();
					updateRemoveComboBox(remove_comboBox);
					
					placeOrder(customerFirstName, customerLastName, orderMain, orderCustom);
					updateInventory();

					order_menu_array.clear();
					order_cust_array.clear();

				} else {
					JOptionPane.showMessageDialog(null,"Cannot submit empty order. Please add items to your order first.");
				}
			}
		});


		JLabel lblNewLabel_1 = new JLabel("Recommendations");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 30));

		JLabel lblNewLabel_2 = new JLabel("Order");
		lblNewLabel_2.setFont(new Font("Arial", Font.BOLD, 30));

		JLabel lblNewLabel_3 = new JLabel("Customizations");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 30));

		final JComboBox<String> entree_comboBox = new JComboBox<String>();
		entree_comboBox.setFont(new Font("Arial", Font.PLAIN, 20));
		entree_comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Turkey Sandwich", "Salami Sandwich", "Tuna Sandwich", "Oven Roast Chicken Sandwich", "Veggie Sandwich", "Ham Sandwich", "Beef Sandwich"}));


		JLabel entree_label = new JLabel("Entrees");
		entree_label.setFont(new Font("Arial", Font.PLAIN, 20));

		final JComboBox<String> sides_comboBox = new JComboBox<String>();
		sides_comboBox.setFont(new Font("Arial", Font.PLAIN, 20));
		sides_comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Baguette", "Fries", "Salad", "Onion Rings"}));

		JLabel Sides_label = new JLabel("Sides");
		Sides_label.setFont(new Font("Arial", Font.PLAIN, 20));

		final JComboBox<String> bev_comboBox = new JComboBox<String>();
		bev_comboBox.setFont(new Font("Arial", Font.PLAIN, 20));
		bev_comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Coke", "Pepsi", "Iced Tea", "Gatorade"}));

		JLabel bev_label = new JLabel("Beverages");
		bev_label.setFont(new Font("Arial", Font.PLAIN, 20));

		final JComboBox<String> dess_comboBox = new JComboBox<String>();
		dess_comboBox.setFont(new Font("Arial", Font.PLAIN, 20));
		dess_comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Chocolate Chip Cookie", "Brownie", "Snickerdoodle Cookie"}));

		dess_label = new JLabel("Desserts");
		dess_label.setFont(new Font("Arial", Font.PLAIN, 20));

		/*Adding an entree to the order.*/
		JButton addEntree_btn = new JButton("Add");
		addEntree_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String entree_Selected = "";
				entree_Selected = entree_comboBox.getSelectedItem().toString();

				if ( isAvailable(MenuItems.getItemId(entree_Selected)) ) {					
					order_menu_array.add( MenuItems.getItemId(entree_Selected) );

					updateOrder();
					updateRemoveComboBox(remove_comboBox);
				} else {
					JOptionPane.showMessageDialog(null,"Sorry, this item is currently out of stock.");
				}
			}
		});
		addEntree_btn.setFont(new Font("Arial", Font.PLAIN, 20));
		
		/*Adding a side to the order.*/
		JButton addSide_btn = new JButton("Add");
		addSide_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sides_Selected = "";
				sides_Selected = sides_comboBox.getSelectedItem().toString();
				
				if( isAvailable(MenuItems.getItemId(sides_Selected)) ) {
					order_menu_array.add( MenuItems.getItemId(sides_Selected) );
					updateOrder();
					updateRemoveComboBox(remove_comboBox);
				} else {
					JOptionPane.showMessageDialog(null,"Sorry, this item is currently out of stock.");
				}

			}
		});
		addSide_btn.setFont(new Font("Arial", Font.PLAIN, 20));

		/*Adding a beverage to the order.*/
		JButton addBev_btn = new JButton("Add");
		addBev_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bev_Selected = "";
				bev_Selected = bev_comboBox.getSelectedItem().toString();
				
				if( isAvailable(MenuItems.getItemId(bev_Selected)) ) {
					order_menu_array.add( MenuItems.getItemId(bev_Selected) );
					updateOrder();
					updateRemoveComboBox(remove_comboBox);
				} else {
					JOptionPane.showMessageDialog(null,"Sorry, this item is currently out of stock.");
				}
			}
		});
		addBev_btn.setFont(new Font("Arial", Font.PLAIN, 20));

		/*Adding a dessert to the order.*/
		JButton addDessert_btn = new JButton("Add");
		addDessert_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dess_Selected = "";
				dess_Selected = dess_comboBox.getSelectedItem().toString();
				
				if( isAvailable(MenuItems.getItemId(dess_Selected)) ) {
					order_menu_array.add( MenuItems.getItemId(dess_Selected) );
					updateOrder();
					updateRemoveComboBox(remove_comboBox);
				} else {
					JOptionPane.showMessageDialog(null,"Sorry, this item is currently out of stock.");
				}
			}
		});
		addDessert_btn.setFont(new Font("Arial", Font.PLAIN, 20));


		/*Lets the customer delete the items in the order.*/
		JButton removeItem_btn = new JButton("Remove Item");
		removeItem_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if( (order_menu_array.size() > 0) || (order_cust_array.size() > 0) ) {
					String removeItem = remove_comboBox.getSelectedItem().toString();

					if( order_menu_array.contains(MenuItems.getItemId(removeItem)) ) {
						order_menu_array.remove( MenuItems.getItemId(removeItem) );
					} else {
						order_cust_array.remove( MenuItems.getItemId(removeItem) );
					}

					updateOrder();

					updateRemoveComboBox(remove_comboBox);
				} else {
					JOptionPane.showMessageDialog(null,"Order already empty.");
				}

			}
		});
		removeItem_btn.setFont(new Font("Arial", Font.PLAIN, 20));
		
		/*Button that return the user to the login screen.*/
		JButton btnNewButton = new JButton("Logout");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CustomerPane.setVisible(false);
				dispose();

				MainWindow.main(null);
			}
		});
		btnNewButton.setFont(new Font("Arial", Font.PLAIN, 20));

		final JComboBox<String> custom_comboBox = new JComboBox<String>();
		custom_comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"turkey", "tuna", "chicken", "ham", "beef", "salami", "ranch", "mayo", "ketchup", "tomato", "lettuce", "veggie patty", "bread", "potato", "onions", "syrup", "tea", "chocolate", "cinammon"}));
		custom_comboBox.setFont(new Font("Arial", Font.PLAIN, 20));
		
		/*Adding a customization to the current order.*/
		JButton addCustom_btn = new JButton("Add");
		addCustom_btn.setFont(new Font("Arial", Font.PLAIN, 20));
		addCustom_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String custom_Selected = "";
				custom_Selected = custom_comboBox.getSelectedItem().toString();
				
				if( isAvailable(MenuItems.getItemId(custom_Selected)) ) {
					order_cust_array.add( MenuItems.getItemId(custom_Selected) );
					updateOrder();
					updateRemoveComboBox(remove_comboBox);
				} else {
					JOptionPane.showMessageDialog(null,"Sorry, this item is currently out of stock.");
				}

			}
		});

		JLabel customization_Label = new JLabel("Customizations");
		customization_Label.setFont(new Font("Arial", Font.PLAIN, 20));

		trends_table = new JTable();

		JLabel lblNewLabel_4 = new JLabel("Trends");
		lblNewLabel_4.setFont(new Font("Arial", Font.BOLD, 40));



		GroupLayout gl_CustomerPane = new GroupLayout(CustomerPane);
		gl_CustomerPane.setHorizontalGroup(
			gl_CustomerPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_CustomerPane.createSequentialGroup()
					.addGroup(gl_CustomerPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_CustomerPane.createSequentialGroup()
							.addGap(57)
							.addGroup(gl_CustomerPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(c_table, GroupLayout.PREFERRED_SIZE, 576, GroupLayout.PREFERRED_SIZE)
								.addComponent(MenuTable, GroupLayout.PREFERRED_SIZE, 587, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_CustomerPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_CustomerPane.createSequentialGroup()
									.addGap(37)
									.addGroup(gl_CustomerPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_CustomerPane.createSequentialGroup()
											.addComponent(entree_comboBox, GroupLayout.PREFERRED_SIZE, 232, GroupLayout.PREFERRED_SIZE)
											.addGap(18)
											.addComponent(addEntree_btn))
										.addComponent(entree_label, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_CustomerPane.createSequentialGroup()
											.addComponent(sides_comboBox, GroupLayout.PREFERRED_SIZE, 232, GroupLayout.PREFERRED_SIZE)
											.addGap(18)
											.addComponent(addSide_btn))
										.addComponent(Sides_label, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
										.addComponent(bev_label, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_CustomerPane.createSequentialGroup()
											.addComponent(bev_comboBox, GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE)
											.addGap(18)
											.addComponent(addBev_btn))
										.addComponent(dess_label, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
										.addComponent(customization_Label)
										.addGroup(gl_CustomerPane.createSequentialGroup()
											.addGroup(gl_CustomerPane.createParallelGroup(Alignment.TRAILING, false)
												.addComponent(custom_comboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(dess_comboBox, 0, 234, Short.MAX_VALUE))
											.addGap(18)
											.addGroup(gl_CustomerPane.createParallelGroup(Alignment.LEADING)
												.addComponent(addCustom_btn)
												.addComponent(addDessert_btn))))
									.addPreferredGap(ComponentPlacement.RELATED, 78, Short.MAX_VALUE))
								.addGroup(gl_CustomerPane.createSequentialGroup()
									.addComponent(removeItem_btn)
									.addGap(2))))
						.addGroup(gl_CustomerPane.createSequentialGroup()
							.addGap(230)
							.addComponent(lblNewLabel_3)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_CustomerPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_CustomerPane.createParallelGroup(Alignment.TRAILING)
							.addGroup(gl_CustomerPane.createSequentialGroup()
								.addGap(230)
								.addComponent(place_order_btn)
								.addGap(54)
								.addComponent(lblNewLabel_2)
								.addGap(311))
							.addGroup(gl_CustomerPane.createSequentialGroup()
								.addGap(4)
								.addComponent(remove_comboBox, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_CustomerPane.createParallelGroup(Alignment.TRAILING)
									.addComponent(rec_table, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
									.addComponent(order_table, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
									.addComponent(trends_table, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE))
								.addGap(93)))
						.addGroup(gl_CustomerPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel_4, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
							.addGap(235))))
				.addGroup(gl_CustomerPane.createSequentialGroup()
					.addGap(105)
					.addGroup(gl_CustomerPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_CustomerPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 1508, Short.MAX_VALUE)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_CustomerPane.createSequentialGroup()
							.addGap(182)
							.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
							.addGap(922)
							.addComponent(lblNewLabel_1)
							.addGap(199))))
		);
		gl_CustomerPane.setVerticalGroup(
			gl_CustomerPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_CustomerPane.createSequentialGroup()
					.addComponent(btnNewButton)
					.addGap(18)
					.addGroup(gl_CustomerPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(lblNewLabel_1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_CustomerPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_CustomerPane.createSequentialGroup()
							.addComponent(entree_label, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addGroup(gl_CustomerPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(entree_comboBox, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(addEntree_btn))
							.addGap(18)
							.addComponent(Sides_label, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_CustomerPane.createParallelGroup(Alignment.LEADING)
								.addComponent(sides_comboBox, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
								.addComponent(addSide_btn))
							.addGap(30)
							.addComponent(bev_label, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_CustomerPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(bev_comboBox, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
								.addComponent(addBev_btn))
							.addGap(18)
							.addComponent(dess_label, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_CustomerPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(dess_comboBox, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
								.addComponent(addDessert_btn))
							.addGap(25)
							.addComponent(customization_Label)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_CustomerPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(custom_comboBox, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addComponent(addCustom_btn)))
						.addGroup(gl_CustomerPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
							.addGroup(gl_CustomerPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(MenuTable, GroupLayout.PREFERRED_SIZE, 436, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_CustomerPane.createSequentialGroup()
									.addComponent(rec_table, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
									.addGap(42)
									.addComponent(lblNewLabel_4, GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
									.addGap(24)
									.addComponent(trends_table, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
									.addGap(158)))))
					.addGap(24)
					.addGroup(gl_CustomerPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblNewLabel_3)
						.addComponent(lblNewLabel_2)
						.addComponent(place_order_btn, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_CustomerPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_CustomerPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(remove_comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(removeItem_btn))
						.addGroup(gl_CustomerPane.createParallelGroup(Alignment.LEADING, false)
							.addGroup(gl_CustomerPane.createSequentialGroup()
								.addComponent(order_table, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGap(56))
							.addComponent(c_table, GroupLayout.PREFERRED_SIZE, 344, GroupLayout.PREFERRED_SIZE)))
					.addGap(46))
		);
		CustomerPane.setLayout(gl_CustomerPane);
	}
}