package p1;
import java.sql.*;
public class DBConnection {
	static Connection con;
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","123456");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return con;
	}
}
