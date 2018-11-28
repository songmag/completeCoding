package DataBases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/*
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
*/
public class DBConnect{	
	protected String address = "jdbc:mariadb://localhost:3306/dooropenservice";
	protected String user = "root";
	protected final String password = "920821";
	public final int LOGIN_OK = 1;
	public final int NO_DATA = 2;
	public final int LOGIN_FAIL = 3;
	public final int DUPLICATE_ID = 4;
	public final int SUCCESS = 5;
	protected final String SIGNUPSQL = "insert into member (id,password,name) values (?,?,?)";
	protected final String SIGNINSQL = "update member set flag = 1 where id =? and password = ? and flag = 0";
	protected final String LOGOUTSQL = "update member set flag = 0 where id = ?";
	protected final String COMPANYSQL = "Select * from company where company.company in (Select company from connect_company where id = ?)";
	protected final String CONNECTSQL = "insert into connect_company values(?,?)";
	protected final String DUPLICATESQL = "Select id from member where id = ?";
	protected final String ERRORCHECKSQL = "select id,password,flag from member where id = ?";
	
	
	protected Connection conn;
	
	/*protected DBConnect()
	{
		JFrame frame;
		JPanel panel;
		frame = new JFrame();
		panel = new JPanel();
		JLabel lDatabase,lPassword ;
		lDatabase = new JLabel("DB name :");
		lPassword = new JLabel("password :");
		JTextField database = new JTextField();
		JPasswordField password = new JPasswordField();
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		panel.add(database);
		panel.add(password);
		panel.add(lPassword);
		panel.add(lDatabase);
		panel.setBackground(Color.WHITE);
		JButton click = new JButton("Login");
	}*/
	protected boolean connection() {
	// TODO Auto-generated method stub
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(address, user, password);
			return true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	protected boolean closeConnection()  {
		if (conn != null)
			try {
				conn.close();
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
	}
}
