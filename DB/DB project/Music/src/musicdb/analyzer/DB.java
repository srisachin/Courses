package musicdb.analyzer;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {

	public DB() {

	}

	public Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/database_project_db2";
			String user = "root";
			String password = "1234";
			con = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}
}
