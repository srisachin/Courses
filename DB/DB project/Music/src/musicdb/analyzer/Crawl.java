package musicdb.analyzer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Crawl {

	public static void main(String[] args) {
		Connection con = null, con2 = null;
		Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://localhost:3306/database_project_db2";
		String user = "root";
		String password = "1234";

		try {
			//con = DriverManager.getConnection(url, user, password);
			//con2 = DriverManager.getConnection(url, user, password);
			DB db_Connection = new DB();
			con = db_Connection.getConnection();
			DB db_Connection2 = new DB();
			con2 = db_Connection2.getConnection();
			st = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
					java.sql.ResultSet.CONCUR_READ_ONLY);
			System.out.println("xdvfsdfs");
			st.setFetchSize(Integer.MIN_VALUE);
			System.out.println("aaaaa");
			rs = st.executeQuery("SELECT youtubeId from songs;");

			PreparedStatement ps = con2
					.prepareStatement("SELECT * from crawls_new where songId=?;");
			PreparedStatement ps_insert = con2
					.prepareStatement("Insert into crawl_new values (?,?,?,?,?,?,?);");
			System.out.println("sdfsd");
			int views1, views2, views3, views4, views5, views6;
			String id;
			boolean write = false;
			while (rs.next()) {
				id = rs.getString(1);
				if (write) {
					views1 = 0;
					views2 = 0;
					views3 = 0;
					views4 = 0;
					views5 = 0;
					views6 = 0;
					ps.setString(1, rs.getString(1));
					ResultSet e = ps.executeQuery();
					while (e.next()) {
						if (e.getInt(2) != 0) {
							views1 = e.getInt(2);
							continue;
						} else if (e.getInt(3) != 0) {
							views2 = e.getInt(3);
							continue;
						} else if (e.getInt(4) != 0) {
							views3 = e.getInt(4);
							continue;
						} else if (e.getInt(5) != 0) {
							views4 = e.getInt(5);
							continue;
						} else if (e.getInt(6) != 0) {
							views5 = e.getInt(6);
							continue;
						} else if (e.getInt(7) != 0) {
							views6 = e.getInt(7);
							continue;
						}
					}
					ps_insert.setString(1, rs.getString(1));
					ps_insert.setInt(2, views1);
					ps_insert.setInt(3, views2);
					ps_insert.setInt(4, views3);
					ps_insert.setInt(5, views4);
					ps_insert.setInt(6, views5);
					ps_insert.setInt(7, views6);

					ps_insert.executeUpdate();

					// con2.commit();

				}
				
				if (id.equals("2CSsyJKSfbs")) {
					write = true;
				}
			}

			ps.close();
			ps_insert.close();
			rs.close();
			con2.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
