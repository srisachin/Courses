package musicdb.analyzer.arc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import musicdb.analyzer.DB;

public class LinkManager2 {

	private HashMap<String, Integer> countryHash;
	private HashMap<String, Integer> genreHash;

	public LinkManager2() {

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

			// arcDiagramData.addNode("196", 1);
			// arcDiagramData.addNode("197", 1);
			// arcDiagramData.addNode("198", 1);
			// arcDiagramData.addNode("199", 1);
			// arcDiagramData.addNode("200", 1);
			// arcDiagramData.addNode("210", 1);

			genreHash = new HashMap<String, Integer>();
			countryHash = new HashMap<String, Integer>();

			// decadeHash.put(196, 0);
			// decadeHash.put(197, 1);
			// decadeHash.put(198, 2);
			// decadeHash.put(199, 3);
			// decadeHash.put(200, 4);
			// decadeHash.put(210, 5);

			int index = 0;

			//con = DriverManager.getConnection(url, user, password);
			DB db_Connection = new DB();
			con = db_Connection.getConnection();
			st = con.createStatement();
			// rs =
			// st.executeQuery("SELECT * FROM database_project_db2.genre_summary order by count desc limit 20;");
			// rs =
			// st.executeQuery("SELECT * FROM database_project_db2.genre_country_1 order by count desc limit 100;");
			// rs = st.executeQuery("SELECT * FROM database_project_db2.genre_country_1 where name='Rock Music' or name='Hip hop music' or name='Alternative rock' or name='Electronic music' or name='Blues' or name='Jazz' or name='Country music' or name='Pop music' or name='Indie rock' or name='Punk rock' or name='Folk music' or name='Religious music' or name='Heavy metal' or name='Dance music' or name='Rock and roll' or name='Christian music' order by count desc limit 200;");
			rs = st.executeQuery("SELECT * FROM database_project_db2.genre_country_1 where (name='Rock Music' or name='Hip hop music' or name='Alternative rock' or name='Electronic music' or name='Blues' or name='Jazz' or name='Country music' or name='Pop music' or name='Indie rock' or name='Punk rock' or name='Folk music' or name='Religious music' or name='Heavy metal' or name='Dance music' or name='Rock and roll' or name='Christian music') and count>500  order by count desc;");
			String genre, country;
			double count;
			int countryIndex, genreIndex, e_count;
			while (rs.next()) {
				genre = rs.getString(1);
				country = rs.getString(2);
				count = rs.getDouble(5) + 1.5;
				e_count = rs.getInt(3);

				if (countryHash.containsKey(country)) {
					countryIndex = countryHash.get(country);
				} else {
					countryIndex = index;
					countryHash.put(country, countryIndex);
					arcDiagramData.addNode(country, 0);
					index++;
				}

				if (genreHash.containsKey(genre)) {
					genreIndex = genreHash.get(genre);
				} else {
					genreIndex = index;
					genreHash.put(genre, index);
					arcDiagramData.addNode(genre, 1);
					index++;
				}

				arcDiagramData
						.addLink(countryIndex, genreIndex, count, e_count);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return arcDiagramData;

	}

}
