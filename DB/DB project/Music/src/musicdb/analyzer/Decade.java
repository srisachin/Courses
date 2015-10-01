package musicdb.analyzer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Decade {
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
			rs = st.executeQuery("SELECT youtubeId, decade from songs, new_table_1 where songs.youtubeId=new_table_1.songID;");

			PreparedStatement ps = con2
					.prepareStatement("SELECT * from genres where songId=?;");
			// PreparedStatement ps_check = con2
			// .prepareStatement("SELECT * from genre_decade where genre=?;");
			PreparedStatement ps_insert1 = con2
					.prepareStatement("Insert into genre_decade values (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE genre_decade.196=genre_decade.196+1;");
			PreparedStatement ps_insert2 = con2
					.prepareStatement("Insert into genre_decade values (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE genre_decade.197=genre_decade.197+1;");
			PreparedStatement ps_insert3 = con2
					.prepareStatement("Insert into genre_decade values (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE genre_decade.198=genre_decade.198+1;");
			PreparedStatement ps_insert4 = con2
					.prepareStatement("Insert into genre_decade values (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE genre_decade.199=genre_decade.199+1;");
			PreparedStatement ps_insert5 = con2
					.prepareStatement("Insert into genre_decade values (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE genre_decade.200=genre_decade.200+1;");
			PreparedStatement ps_insert6 = con2
					.prepareStatement("Insert into genre_decade values (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE genre_decade.210=genre_decade.210+1;");
			
			// PreparedStatement ps_update1 = con2
			// .prepareStatement("Update genre_decade set genre_decade.196 = genre_decade.196 + 1 where genre=?;");
			// PreparedStatement ps_update2 = con2
			// .prepareStatement("Update genre_decade set genre_decade.197 = genre_decade.197 + 1 where genre=?;");
			// PreparedStatement ps_update3 = con2
			// .prepareStatement("Update genre_decade set genre_decade.198 = genre_decade.198 + 1 where genre=?;");
			// PreparedStatement ps_update4 = con2
			// .prepareStatement("Update genre_decade set genre_decade.199 = genre_decade.199 + 1 where genre=?;");
			// PreparedStatement ps_update5 = con2
			// .prepareStatement("Update genre_decade set genre_decade.200 = genre_decade.200 + 1 where genre=?;");
			// PreparedStatement ps_update6 = con2
			// .prepareStatement("Update genre_decade set genre_decade.210 = genre_decade.210 + 1 where genre=?;");

			System.out.println("sdfsd");
			int views1, views2, views3, views4, views5, views6;
			String genre;
			System.out.println("asd");
			while (rs.next()) {
				String songId = rs.getString(1);
				int decade = rs.getInt(2);

				if (decade == 195 || decade == 196 || decade == 197
						|| decade == 198 || decade == 199 || decade == 200
						|| decade == 210) {

					ps.setString(1, songId);
					ResultSet set = ps.executeQuery();

					while (set.next()) {
						genre = set.getString("name");
						// ps_check.setString(1, genre);
						// ResultSet existingTuples = ps_check.executeQuery();
						// if (existingTuples.next()) {
						// if (decade==196){
						// ps_update1.setString(1, genre);
						// ps_update1.executeUpdate();
						// } else if (decade==197){
						// ps_update2.setString(1, genre);
						// ps_update2.executeUpdate();
						// } else if (decade==198){
						// ps_update3.setString(1, genre);
						// ps_update3.executeUpdate();
						// } else if (decade==199){
						// ps_update4.setString(1, genre);
						// ps_update4.executeUpdate();
						// } else if (decade==200){
						// ps_update5.setString(1, genre);
						// ps_update5.executeUpdate();
						// } else if (decade==210){
						// ps_update6.setString(1, genre);
						// ps_update6.executeUpdate();
						// }
						//
						// } else {
						// System.out.println(genre);
						if (decade==196){
							ps_insert1.setString(1, genre);
							ps_insert1.setInt(2, 1);
							ps_insert1.setInt(3, 0);
							ps_insert1.setInt(4, 0);
							ps_insert1.setInt(5, 0);
							ps_insert1.setInt(6, 0);
							ps_insert1.setInt(7, 0);
							
							ps_insert1.executeUpdate();
						} else if (decade==197){
							ps_insert2.setString(1, genre);
							ps_insert2.setInt(2, 0);
							ps_insert2.setInt(3, 1);
							ps_insert2.setInt(4, 0);
							ps_insert2.setInt(5, 0);
							ps_insert2.setInt(6, 0);
							ps_insert2.setInt(7, 0);
							
							ps_insert2.executeUpdate();
						}  else if (decade==198){
							ps_insert3.setString(1, genre);
							ps_insert3.setInt(2, 0);
							ps_insert3.setInt(3, 0);
							ps_insert3.setInt(4, 1);
							ps_insert3.setInt(5, 0);
							ps_insert3.setInt(6, 0);
							ps_insert3.setInt(7, 0);
							
							ps_insert3.executeUpdate();
						}  else if (decade==199){
							ps_insert4.setString(1, genre);
							ps_insert4.setInt(2, 0);
							ps_insert4.setInt(3, 0);
							ps_insert4.setInt(4, 0);
							ps_insert4.setInt(5, 1);
							ps_insert4.setInt(6, 0);
							ps_insert4.setInt(7, 0);
							
							ps_insert4.executeUpdate();
						}  else if (decade==200){
							ps_insert5.setString(1, genre);
							ps_insert5.setInt(2, 0);
							ps_insert5.setInt(3, 0);
							ps_insert5.setInt(4, 0);
							ps_insert5.setInt(5, 0);
							ps_insert5.setInt(6, 1);
							ps_insert5.setInt(7, 0);
							
							ps_insert5.executeUpdate();
						}  else if (decade==210){
							ps_insert6.setString(1, genre);
							ps_insert6.setInt(2, 0);
							ps_insert6.setInt(3, 0);
							ps_insert6.setInt(4, 0);
							ps_insert6.setInt(5, 0);
							ps_insert6.setInt(6, 0);
							ps_insert6.setInt(7, 1);
							
							ps_insert6.executeUpdate();
						}
						
						// if (decade == 196) {
						//
						// } else if (decade == 197) {
						// ps_insert.setInt(3, 1);
						// } else if (decade == 198) {
						// ps_insert.setInt(4, 1);
						// } else if (decade == 199) {
						// ps_insert.setInt(5, 1);
						// } else if (decade == 200) {
						// ps_insert.setInt(6, 1);
						// } else if (decade == 210) {
						// ps_insert.setInt(7, 1);
						// }

						
					}
				} else {
					continue;
				}
				// }
			}
			ps.close();
			ps_insert1.close();
			ps_insert2.close();
			ps_insert3.close();
			ps_insert4.close();
			ps_insert5.close();
			ps_insert6.close();
			rs.close();
			con2.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
