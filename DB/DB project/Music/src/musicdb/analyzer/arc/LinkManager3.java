package musicdb.analyzer.arc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import musicdb.analyzer.DB;

public class LinkManager3 {

	private HashMap<String, Integer> countryHash;
	private HashMap<Integer, Integer> decadeHash;

	public LinkManager3() {

	}

	public ArcDiagramData getData() {
		Connection con = null, con2 = null;
		Statement st = null;
		ResultSet rs = null;

		ArcDiagramData arcDiagramData = new ArcDiagramData();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/database_project_db2";
			String user = "root";
			String password = "1234";

			arcDiagramData.addNode("196", 0);
			arcDiagramData.addNode("197", 0);
			arcDiagramData.addNode("198", 0);
			arcDiagramData.addNode("199", 0);
			arcDiagramData.addNode("200", 0);
			// arcDiagramData.addNode("210", 1);

			decadeHash = new HashMap<Integer, Integer>();
			countryHash = new HashMap<String, Integer>();

			// decadeHash.put(196, 0);
			// decadeHash.put(197, 1);
			// decadeHash.put(198, 2);
			// decadeHash.put(199, 3);
			// decadeHash.put(200, 4);
			// decadeHash.put(210, 5);

			int index = 5;

			//con = DriverManager.getConnection(url, user, password);
			DB db_Connection = new DB();
			con = db_Connection.getConnection();
			st = con.createStatement();
			// rs =
			// st.executeQuery("SELECT * FROM database_project_db2.genre_summary order by count desc limit 20;");
			// rs =
			// st.executeQuery("SELECT * FROM database_project_db2.genre_country_1 order by count desc limit 100;");
			// rs =
			// st.executeQuery("SELECT * FROM database_project_db2.genre_country_1 where name='Rock Music' or name='Hip hop music' or name='Alternative rock' or name='Electronic music' or name='Blues' or name='Jazz' or name='Country music' or name='Pop music' or name='Indie rock' or name='Punk rock' or name='Folk music' or name='Religious music' or name='Heavy metal' or name='Dance music' or name='Rock and roll' or name='Christian music' order by count desc limit 200;");
			rs = st.executeQuery("SELECT * FROM database_project_db2.top_country;");
			String country;
			double count;
			int countryIndex, decade196, decade197, decade198, decade199, decade200, genreIndex, e_count;
			double d196_n, d197_n, d198_n, d199_n, d200_n;
			while (rs.next()) {
				country = rs.getString(1);
				decade196 = rs.getInt(2);
				decade197 = rs.getInt(3);
				decade198 = rs.getInt(4);
				decade199 = rs.getInt(5);
				decade200 = rs.getInt(6);
				
				d196_n = rs.getDouble(8) + 1.5;
				d197_n = rs.getDouble(9) + 1.5;
				d198_n = rs.getDouble(10) + 1.5;
				d199_n = rs.getDouble(11) + 1.5;
				d200_n = rs.getDouble(12) + 1.5;

				if (countryHash.containsKey(country)) {
					countryIndex = countryHash.get(country);
				} else {
					countryIndex = index;
					countryHash.put(country, countryIndex);
					arcDiagramData.addNode(country, 1);
					index++;
				}

				// if (decadeHash.containsKey(genre)) {
				// genreIndex = decadeHash.get(genre);
				// } else {
				// genreIndex = index;
				// decadeHash.put(genre, index);
				// arcDiagramData.addNode(genre, 1);
				// index++;
				// }

				arcDiagramData.addLink(countryIndex, 0, d196_n, decade196);
				arcDiagramData.addLink(countryIndex, 1, d197_n, decade197);
				arcDiagramData.addLink(countryIndex, 2, d198_n, decade198);
				arcDiagramData.addLink(countryIndex, 3, d199_n, decade199);
				arcDiagramData.addLink(countryIndex, 4, d200_n, decade200);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return arcDiagramData;

	}

}
