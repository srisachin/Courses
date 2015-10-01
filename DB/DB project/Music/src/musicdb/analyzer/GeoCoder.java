package musicdb.analyzer;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.XMLReaderFactory;

public class GeoCoder {

	 public static void main(String[] args) {
	        java.sql.Connection con = null, con2 = null;
	        java.sql.Statement st = null;
	        ResultSet rs = null;

	        String url = "jdbc:mysql://localhost:3306/Database_Project_DB";
	        String user = "root";
	        String password = "1234";
	        
	        String newSongCountry = getNewSongCountry("new jersey");
	        System.out.println(newSongCountry);

//	        try {
//	            con = DriverManager.getConnection(url, user, password);
//	            con2 = DriverManager.getConnection(url, user, password);
//	            st = con2.createStatement();
//	            java.sql.PreparedStatement stat = con.prepareStatement(
//	                    "SELECT * FROM Database_Project_DB.Songs",
//	                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//	            stat.setFetchSize(Integer.MIN_VALUE);
//	            rs = stat.executeQuery();
//	            String youtubeId = "";
//	            String songCountry = "", newSongCountry = "";
//	            // java.sql.PreparedStatement st = con.prepareStatement(
//	            // "INSERT INTO Database_Project_DB.Songs2 (youtubeId) VALUES ('"
//	            // + youtubeId + "');");
//	            // rs =
//	            // stmt.executeQuery("SELECT * FROM Database_Project_DB.Songs;");
//	            while (rs.next()) {
//	                youtubeId = rs.getString("youtubeId");
//	                songCountry = rs.getString("songCountry");
//	                // System.out.println("song name: " + songName);
//
//	                newSongCountry = getNewSongCountry(songCountry);
//
//	                st.executeUpdate("UPDATE Database_Project_DB.Songs2 SET songCountry='"
//	                        + newSongCountry
//	                        + "' WHERE youtubeId='"
//	                        + youtubeId
//	                        + "');");
//	                // st.executeUpdate();
//	            }
//	            st.close();
//	            rs.close();
//	            stat.close();
//	            con.close();
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
	    }
	 private static String getNewSongCountry(String songCountry) {
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db;
	        String key = "Fmjtd%7Cluu82q6al1%2C2g%3Do5-94t2d6";
	        try {
	        	
//	        	Document doc = db.parse(new URL(url).openStream());
	        	
	            db = dbf.newDocumentBuilder();
	            songCountry = songCountry.replace(" ", "%20");
	            String url = "http://open.mapquestapi.com/geocoding/v1/address?key="
	                    + key + "&location=" + songCountry + "&outFormat=xml";
	            System.out.println(url);
	           Document doc = db.parse(new URL(url).openStream());
//	//
	            TransformerFactory factory = TransformerFactory.newInstance();
////	            Transformer xform = factory.newTransformer();
////	            xform.transform(new DOMSource(doc), new StreamResult(System.out));
//
//	            org.w3c.dom.Element docElement = doc.getDocumentElement();
//	            //NodeList childNodes = docElement.getChildNodes();
//
//	            // NodeList adminArea1 =
//	            NodeList elementsByTagName = docElement.getElementsByTagName("response");

//	            org.w3c.dom.Node placeNode = providedLocation.item(0);
	            // if (placeNode != null) {
	            // NamedNodeMap placeAttributes = placeNode.getAttributes();
	            //
	            // Node latNode = placeAttributes.getNamedItem("lat");
	            // String lat = latNode.getNodeValue();
	            //
	            // Node lonNode = placeAttributes.getNamedItem("lon");
	            // String lon = lonNode.getNodeValue();
	            //
	            // output.write(address + "\t" + lat + "\t" + lon + "\n");
	            // // System.out.println("\n lat lot " + lat + " " + lon);
	            // }
	            //
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return null;
	    }
}
