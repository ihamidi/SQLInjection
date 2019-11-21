package sqli_mysql;

import java.awt.EventQueue;
import java.util.*;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
/**
 * 
 * 
 */
/**
 * User Registration using Swing
 * @author javaguides.net
 * @author Izhak Hamidi (ADAPTED CODE TO WORK AS A USER LOG IN FORM AS WELL)
 */
public class UserLogin extends JFrame {
    private static final long serialVersionUID = 1;
    private JPanel contentPane;
//    private JTextField firstname;
//    private JTextField lastname;
    private JTextField email;
    private JTextField username;
//    private JTextField mob;
    private JPasswordField passwordField;
    private JButton btnNewButton;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserLogin frame = new UserLogin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */

    public UserLogin() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\User\\Desktop\\STDM.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewUserRegister = new JLabel("New User Register");
        lblNewUserRegister.setFont(new Font("Times New Roman", Font.PLAIN, 42));
        lblNewUserRegister.setBounds(362, 52, 325, 50);
        contentPane.add(lblNewUserRegister);





        email = new JTextField();



        username = new JTextField();
        username.setFont(new Font("Tahoma", Font.PLAIN, 32));
        username.setBounds(707, 151, 228, 50);
        contentPane.add(username);
        username.setColumns(10);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblUsername.setBounds(542, 159, 99, 29);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblPassword.setBounds(542, 245, 99, 24);
        contentPane.add(lblPassword);



        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        passwordField.setBounds(707, 235, 228, 50);
        contentPane.add(passwordField);

        btnNewButton = new JButton("login");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                
            	String userName = toAscii(username.getText());
            	String password = toAscii(passwordField.getText());


                //String msg = "" + firstName;
                //msg += " \n";

                

                try {
                	Class.forName("com.mysql.jdbc.Driver");
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/swing_demo", "root", "password");

                    String query = "SELECT * FROM account WHERE user_name = '"+
                            userName+"' AND password = '"+password+"' ";
                    
                    //running the query through a sanitization process;
                    query=querySanitize(query);
                    //using tokenization technique to futther strengthen security
                    query=queryTokenization(query);
                    Statement sta = connection.createStatement();
                    
                    ResultSet x = sta.executeQuery(query);
                    //System.out.println(x.getObject(3));
                    if (x.next()) {
                        JOptionPane.showMessageDialog(btnNewButton, "Succesful login. Welcome: "+x.getString(1));
                        
                    }
                    else {
                        JOptionPane.showMessageDialog(btnNewButton, "Incorrect username or password, no login");
                    }
                    
                    connection.close();
                    //if all else fails than exit upon exception
                } catch (Exception exception) {
                	//Catching error based SQL Injection attacks;
                    JOptionPane.showMessageDialog(btnNewButton, "Error Detected, System Exiting");
                    System.exit(-1);
                }
            }
        });
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 22));
        btnNewButton.setBounds(399, 447, 259, 74);
        contentPane.add(btnNewButton);
    }
    /**
     * Method by Izhak Hamidi, Adding sanitization to query.
     * @param qry
     * @return sanitized qry
     */
    public static String querySanitize(String qry)
    {
    	//sanitizing against boolean based SQL injection
    	if(qry.contains("--") || qry.contains("OR"))
    		return "SELECT * FROM account WHERE 0=1;";
    	
		return qry;
		
    	
    }
    /**
     * Izhak Hamidis implementation
     * Implementing Query Tokenization technique from:
	 * https://pdfs.semanticscholar.org/8de9/490c4beca0d03b4db51d0cc1ad640ed096f3.pdf
     * @return TOKENIZED QUERY
     */
    public static String queryTokenization(String qry)
    {    	
    	String delimiter =" ";
    	StringTokenizer basequery=new StringTokenizer("SELECT * FROM account "
    			+ "WHERE user_name = 'username' AND password = 'password'", delimiter);
    	StringTokenizer tk= new StringTokenizer(qry,delimiter);
    	//limit the number of words in sql statement to 12
    	String[]tokens= new String[basequery.countTokens()];
    	//CHECK TO SEE IF THERE ARE EXTRA WORDS IN TEH SQL STATEMENT
    	try {
    		int i=0;
	    	while(tk.hasMoreTokens())
	    	{
	    		tokens[i]=tk.nextToken();
	    		i++;
	    	}
    	}
    	catch (IndexOutOfBoundsException e)
    	{
    		return "SELECT * FROM account WHERE 0=1;";
    	}
    	
		return qry;
		
    	
    }

	/**
	 * Converts an input into number to prevent SQL injeciton attacks directly into a query
	 * @author Izhak Hamidi
	 * @param input the input to be converted into numbers
	 * @return the input as Ascii numbers
	 */
	public String toAscii(String input) {
		String convertString="";
		char[] convert=input.toCharArray();
		for(int i =0;i<convert.length;i++)
		{
			convertString+=String.format("%03d", (int)convert[i]);
		}
		System.out.print(convertString);
		return convertString;
	}
	
}