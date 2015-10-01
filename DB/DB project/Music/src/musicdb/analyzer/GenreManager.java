package musicdb.analyzer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class GenreManager {

	public DataTableObj getCountryList() {
		
		Connection con = null;
		java.sql.Statement st = null;
		ResultSet rs = null;

		System.out.println("hereeee");
		String url = "jdbc:mysql://localhost:3306/database_project_db2";
		String user = "root";
		String password = "1234";

		DataTableObj dataTableObj = new DataTableObj();

		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			 
			//con = DriverManager.getConnection(url, user, password);
			DB db_Connection = new DB();
			con = db_Connection.getConnection();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM database_project_db2.top_genres");
			
			String genre = "";
			
			while (rs.next()) {
				genre = rs.getString("name");
				dataTableObj.getAaData().add(genre);
				// productList.add(predicate.stringValue());
			}
			
			rs.close();
			st.close();
			con.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dataTableObj;
	}

}
