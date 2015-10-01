package musicdb.analyzer.arc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import musicdb.analyzer.DB;

public class LinkManager {

	private HashMap<Integer, Integer> decadeHash;
	private HashMap<String, Integer> genreHash;

	public LinkManager() {

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

			arcDiagramData.addNode("196", 1);
			arcDiagramData.addNode("197", 1);
			arcDiagramData.addNode("198", 1);
			arcDiagramData.addNode("199", 1);
			arcDiagramData.addNode("200", 1);
			arcDiagramData.addNode("210", 1);

			genreHash = new HashMap<String, Integer>();
			decadeHash = new HashMap<Integer, Integer>();

			decadeHash.put(196, 0);
			decadeHash.put(197, 1);
			decadeHash.put(198, 2);
			decadeHash.put(199, 3);
			decadeHash.put(200, 4);
			decadeHash.put(210, 5);

			int index = 6;

			//con = DriverManager.getConnection(url, user, password);
			
			DB db_Connection = new DB();
			con = db_Connection.getConnection();
			
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM database_project_db2.genre_summary order by count desc limit 20;");
			// rs =
			// st.executeQuery("SELECT * FROM database_project_db2.genre_country_1 order by count desc limit 1000;");
			String genre;
			int decade, count;
			while (rs.next()) {
				genre = rs.getString(1);
				decade = rs.getInt(2);
				count = rs.getInt(3);

				if (decade == 196 || decade == 197 || decade == 198
						|| decade == 199 || decade == 200 || decade == 210) {
					if (genreHash.containsKey(genre)) {
						Integer value = genreHash.get(genre);
						arcDiagramData.addLink(decadeHash.get(decade), value,
								0, count);
					} else {
						genreHash.put(genre, index);
						arcDiagramData.addNode(genre, 2);
						arcDiagramData.addLink(decadeHash.get(decade), index,0,
								count);
						index++;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return arcDiagramData;

	}

}
