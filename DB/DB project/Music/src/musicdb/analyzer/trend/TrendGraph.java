package musicdb.analyzer.trend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import musicdb.analyzer.DB;

//import org.openrdf.model.Statement;
//import org.openrdf.model.URI;
//import org.openrdf.model.vocabulary.RDF;
//import org.openrdf.repository.RepositoryException;
//import org.openrdf.repository.RepositoryResult;
//import org.siemens.analyzer.search.SearchManager;
//import org.siemens.analyzer.util.ConnHelper;
//
//import com.franz.agraph.repository.AGRepositoryConnection;
//import com.franz.agraph.repository.AGValueFactory;
//import com.ibm.icu.text.DateFormat;
//import com.ibm.icu.text.SimpleDateFormat;
//import com.ibm.icu.util.Calendar;

public class TrendGraph {

	private int range_start, range_end;

	private String genres;

	private Map<String, String> songNameHash; // id name

	private Map<String, String> songLinkHash; // id link

	private Map<String, List<Integer>> songViewHash; // id view counts

	private int songCount;

	// static String ontBaseURI =
	// "http://www.siemens.com/scr/customer_survey.owl#";
	//
	// private HashMap<String, HashMap<String, Integer>> keywordCountsByMonth;
	//
	// private HashMap<String, Integer> productHash;
	//
	public TrendGraph(int rangeStart, int rangeEnd, String genres) {
		range_start = rangeStart;
		range_end = rangeEnd;
		this.genres = genres;
		System.out.println("start:" + range_start);
		System.out.println("end:" + range_end);
		songNameHash = Collections
				.synchronizedMap(new LinkedHashMap<String, String>());
		songViewHash = Collections
				.synchronizedMap(new LinkedHashMap<String, List<Integer>>());
		songLinkHash = Collections
				.synchronizedMap(new LinkedHashMap<String, String>());
		retrieveTopSongs();
		System.out.println("retrieved");
		// keywordCountsByMonth = new HashMap<String, HashMap<String,
		// Integer>>();
		// retrieveProducts(ontSchemaName);
	}

	private void retrieveTopSongs() {
		Connection con = null, con2 = null;
		Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://localhost:3306/database_project_db2";
		String user = "root";
		String password = "1234";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			//con = DriverManager.getConnection(url, user, password);
			DB db_Connection = new DB();
			con = db_Connection.getConnection();
			st = con.createStatement();
			String queryStr;
			 if (genres == null || genres.equals("")) {

			queryStr = "SELECT * from new_table_1 where views6 > "
					+ range_start + " and views6 < " + range_end
					+ " ORDER BY slope DESC LIMIT 10;";
			System.out.println("Query started: " + queryStr);
			rs = st.executeQuery(queryStr);
			 } else {
				 queryStr = "SELECT * from new_table_1 where views6 > "
							+ range_start + " and views6 < " + range_end
							+ " ORDER BY slope DESC;";
					System.out.println("Query started: " + queryStr);
					rs = st.executeQuery(queryStr);
			// queryStr =
			// "SELECT distinct new_table_1.songId, views1, views2, views3, views4, views5, views6, predict from new_table_1, top_genres_songs where views6 > "
			// + range_start
			// + " and views6 < "
			// + range_end
			// +
			// " and top_genres_songs.songId=new_table_1.songId and top_genres_songs.name IN ("
			// + genres + ") ORDER BY slope DESC LIMIT 10;";
			// // queryStr =
			// //
			// "SELECT distinct new_table_1.songId, views1, views2, views3, views4, views5, views6, predict from new_table_1, top_genres_songs where views6 > "
			// // + range_start
			// // + " and views6 < "
			// // + range_end
			// // +
			// //
			// " and top_genres_songs.songId=new_table_1.songId and top_genres_songs.name IN ("
			// // + genres + ") ORDER BY slope DESC LIMIT 10;";
			// System.out.println("Query started: " + queryStr);
			// rs = st.executeQuery(queryStr);
			 }
			System.out.println("Query completed: " + queryStr);
			PreparedStatement ps = con
					.prepareStatement("SELECT songName, url from songs_new1 where youtubeId=?;");
			PreparedStatement psGenre = con
					.prepareStatement("SELECT * from top_genres_songs where top_genres_songs.songId=?;");
			List<Integer> viewList;
			int index = 0;
			boolean hasGenre = false;
			ResultSet genreRs;
			songCount = 0;
			while (rs.next()) {

				hasGenre = false;
				if (genres != null && !genres.equals("")) {
					psGenre.setString(1, rs.getString(1));
					genreRs = psGenre.executeQuery();
					while (genreRs.next()) {
						if (genres.contains(genreRs.getString("name"))) {
							hasGenre = true;
							break;
						}
					}
				} else {
					hasGenre = true;
				}

				if (!hasGenre) {
					continue;
				}
				
				ps.setString(1, rs.getString(1));
				ResultSet rs2 = ps.executeQuery();
				rs2.next();

				songNameHash.put(rs.getString(1), rs2.getString(1));
				songLinkHash.put(rs.getString(1), rs2.getString(2));

				// songNameHash.put(rs.getString(1), "song namee" + index++);
				// songLinkHash.put(rs.getString(1), "www.youtube.com");

				viewList = new ArrayList<Integer>();
				viewList.add(rs.getInt(2));
				viewList.add(rs.getInt(3));
				viewList.add(rs.getInt(4));
				viewList.add(rs.getInt(5));
				viewList.add(rs.getInt(6));
				viewList.add(rs.getInt(7));
				viewList.add(rs.getInt(8));
				songViewHash.put(rs.getString(1), viewList);
				
				songCount++;
				if (songCount==10){
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public MainChartData getTrendGraphData() {
		Collection<String> keys = songNameHash.keySet();
		Iterator<String> it_keys = keys.iterator();
		Iterator<String> it_keys2 = keys.iterator();
		Iterator<String> it_keys3 = keys.iterator();
		Iterator<String> it_keys4 = keys.iterator();
		// Collection<String> names = songNameHash.values();
		Collection<List<Integer>> views = songViewHash.values();

		MainChartData mcd = new MainChartData();
		mcd.getLabel().add(songNameHash.get(it_keys.next()));
		mcd.getLabel().add(songNameHash.get(it_keys.next()));
		mcd.getLabel().add(songNameHash.get(it_keys.next()));
		mcd.getLabel().add(songNameHash.get(it_keys.next()));
		mcd.getLabel().add(songNameHash.get(it_keys.next()));
		mcd.getLabel().add(songNameHash.get(it_keys.next()));
		mcd.getLabel().add(songNameHash.get(it_keys.next()));
		mcd.getLabel().add(songNameHash.get(it_keys.next()));
		mcd.getLabel().add(songNameHash.get(it_keys.next()));
		mcd.getLabel().add(songNameHash.get(it_keys.next()));

		mcd.getIds().add(it_keys3.next());
		mcd.getIds().add(it_keys3.next());
		mcd.getIds().add(it_keys3.next());
		mcd.getIds().add(it_keys3.next());
		mcd.getIds().add(it_keys3.next());
		mcd.getIds().add(it_keys3.next());
		mcd.getIds().add(it_keys3.next());
		mcd.getIds().add(it_keys3.next());
		mcd.getIds().add(it_keys3.next());
		mcd.getIds().add(it_keys3.next());

		mcd.getLinks().add(songLinkHash.get(it_keys4.next()));
		mcd.getLinks().add(songLinkHash.get(it_keys4.next()));
		mcd.getLinks().add(songLinkHash.get(it_keys4.next()));
		mcd.getLinks().add(songLinkHash.get(it_keys4.next()));
		mcd.getLinks().add(songLinkHash.get(it_keys4.next()));
		mcd.getLinks().add(songLinkHash.get(it_keys4.next()));
		mcd.getLinks().add(songLinkHash.get(it_keys4.next()));
		mcd.getLinks().add(songLinkHash.get(it_keys4.next()));
		mcd.getLinks().add(songLinkHash.get(it_keys4.next()));
		mcd.getLinks().add(songLinkHash.get(it_keys4.next()));

		List<Integer> list0 = songViewHash.get(it_keys2.next());
		List<Integer> list1 = songViewHash.get(it_keys2.next());
		List<Integer> list2 = songViewHash.get(it_keys2.next());
		List<Integer> list3 = songViewHash.get(it_keys2.next());
		List<Integer> list4 = songViewHash.get(it_keys2.next());
		List<Integer> list5 = songViewHash.get(it_keys2.next());
		List<Integer> list6 = songViewHash.get(it_keys2.next());
		List<Integer> list7 = songViewHash.get(it_keys2.next());
		List<Integer> list8 = songViewHash.get(it_keys2.next());
		List<Integer> list9 = songViewHash.get(it_keys2.next());

		// ChartObj chartObj = new ChartObj();
		// chartObj.setLabel("2015-01-22");
		// chartObj.getValues().add(list0.get(0));
		// chartObj.getValues().add(list1.get(0));
		// chartObj.getValues().add(list2.get(0));
		// chartObj.getValues().add(list3.get(0));
		// chartObj.getValues().add(list4.get(0));
		// chartObj.getValues().add(list5.get(0));
		// chartObj.getValues().add(list6.get(0));
		// chartObj.getValues().add(list7.get(0));
		// chartObj.getValues().add(list8.get(0));
		// chartObj.getValues().add(list9.get(0));

		ChartObj chartObj2 = new ChartObj();
		chartObj2.setLabel("2015-02-06");
		if (list0.get(1) == 0) {
			chartObj2.getValues().add(list0.get(0));
		} else {
			chartObj2.getValues().add(list0.get(1));
		}
		if (list1.get(1) == 0) {
			chartObj2.getValues().add(list1.get(0));
		} else {
			chartObj2.getValues().add(list1.get(1));
		}
		if (list2.get(1) == 0) {
			chartObj2.getValues().add(list2.get(0));
		} else {
			chartObj2.getValues().add(list2.get(1));
		}
		if (list3.get(1) == 0) {
			chartObj2.getValues().add(list3.get(0));
		} else {
			chartObj2.getValues().add(list3.get(1));
		}
		if (list4.get(1) == 0) {
			chartObj2.getValues().add(list4.get(0));
		} else {
			chartObj2.getValues().add(list4.get(1));
		}
		if (list5.get(1) == 0) {
			chartObj2.getValues().add(list5.get(0));
		} else {
			chartObj2.getValues().add(list5.get(1));
		}
		if (list6.get(1) == 0) {
			chartObj2.getValues().add(list6.get(0));
		} else {
			chartObj2.getValues().add(list6.get(1));
		}
		if (list7.get(1) == 0) {
			chartObj2.getValues().add(list7.get(0));
		} else {
			chartObj2.getValues().add(list7.get(1));
		}
		if (list8.get(1) == 0) {
			chartObj2.getValues().add(list8.get(0));
		} else {
			chartObj2.getValues().add(list8.get(1));
		}
		if (list9.get(1) == 0) {
			chartObj2.getValues().add(list9.get(0));
		} else {
			chartObj2.getValues().add(list9.get(1));
		}

		ChartObj chartObj3 = new ChartObj();
		chartObj3.setLabel("2015-02-11");
		chartObj3.getValues().add(list0.get(2));
		chartObj3.getValues().add(list1.get(2));
		chartObj3.getValues().add(list2.get(2));
		chartObj3.getValues().add(list3.get(2));
		chartObj3.getValues().add(list4.get(2));
		chartObj3.getValues().add(list5.get(2));
		chartObj3.getValues().add(list6.get(2));
		chartObj3.getValues().add(list7.get(2));
		chartObj3.getValues().add(list8.get(2));
		chartObj3.getValues().add(list9.get(2));

		ChartObj chartObj4 = new ChartObj();
		chartObj4.setLabel("2015-02-26");
		chartObj4.getValues().add(list0.get(3));
		chartObj4.getValues().add(list1.get(3));
		chartObj4.getValues().add(list2.get(3));
		chartObj4.getValues().add(list3.get(3));
		chartObj4.getValues().add(list4.get(3));
		chartObj4.getValues().add(list5.get(3));
		chartObj4.getValues().add(list6.get(3));
		chartObj4.getValues().add(list7.get(3));
		chartObj4.getValues().add(list8.get(3));
		chartObj4.getValues().add(list9.get(3));

		ChartObj chartObj5 = new ChartObj();
		chartObj5.setLabel("2015-02-26");
		chartObj5.getValues().add(list0.get(4));
		chartObj5.getValues().add(list1.get(4));
		chartObj5.getValues().add(list2.get(4));
		chartObj5.getValues().add(list3.get(4));
		chartObj5.getValues().add(list4.get(4));
		chartObj5.getValues().add(list5.get(4));
		chartObj5.getValues().add(list6.get(4));
		chartObj5.getValues().add(list7.get(4));
		chartObj5.getValues().add(list8.get(4));
		chartObj5.getValues().add(list9.get(4));

		ChartObj chartObj6 = new ChartObj();
		chartObj6.setLabel("2015-02-26");
		chartObj6.getValues().add(list0.get(5));
		chartObj6.getValues().add(list1.get(5));
		chartObj6.getValues().add(list2.get(5));
		chartObj6.getValues().add(list3.get(5));
		chartObj6.getValues().add(list4.get(5));
		chartObj6.getValues().add(list5.get(5));
		chartObj6.getValues().add(list6.get(5));
		chartObj6.getValues().add(list7.get(5));
		chartObj6.getValues().add(list8.get(5));
		chartObj6.getValues().add(list9.get(5));

		ChartObj chartObj7 = new ChartObj();
		chartObj7.setLabel("Prediction");
		chartObj7.getValues().add(list0.get(6));
		chartObj7.getValues().add(list1.get(6));
		chartObj7.getValues().add(list2.get(6));
		chartObj7.getValues().add(list3.get(6));
		chartObj7.getValues().add(list4.get(6));
		chartObj7.getValues().add(list5.get(6));
		chartObj7.getValues().add(list6.get(6));
		chartObj7.getValues().add(list7.get(6));
		chartObj7.getValues().add(list8.get(6));
		chartObj7.getValues().add(list9.get(6));

		// ChartObj chartObj8 = new ChartObj();
		// chartObj8.setLabel("Prediction 2");
		// chartObj8.getValues().add(list0.get(6)+1000);
		// chartObj8.getValues().add(list1.get(6)+1000);
		// chartObj8.getValues().add(list2.get(6)+1000);
		// chartObj8.getValues().add(list3.get(6)+1000);
		// chartObj8.getValues().add(list4.get(6)+1000);
		// chartObj8.getValues().add(list5.get(6)+1000);
		// chartObj8.getValues().add(list6.get(6)+1000);
		// chartObj8.getValues().add(list7.get(6)+1000);
		// chartObj8.getValues().add(list8.get(6)+1000);
		// chartObj8.getValues().add(list9.get(6)+1000);

		// mcd.getValues().add(chartObj);
		mcd.getValues().add(chartObj2);
		mcd.getValues().add(chartObj3);
		mcd.getValues().add(chartObj4);
		mcd.getValues().add(chartObj5);
		mcd.getValues().add(chartObj6);
		mcd.getValues().add(chartObj7);
		// mcd.getValues().add(chartObj8);

		return mcd;
	}
	//
	// public MainChartData getTrendGraphDataWeekly() {
	// MainChartData mainChartData = new MainChartData();
	//
	// HashMap<String, HashMap<String, List<String>>> currentResults =
	// SearchManager.currentResults;
	// Calendar minKey = Calendar.getInstance();
	// minKey.set(2025, 12, 20);
	//
	// Calendar maxKey = Calendar.getInstance();
	// maxKey.set(1900, 12, 20);
	//
	// DateFormat dateFormat = new SimpleDateFormat("MMM yyyy W");
	// Set<String> resultSet = currentResults.keySet();
	// for (String result : resultSet) {
	// HashMap<String, List<String>> fieldHashMap = currentResults
	// .get(result);
	// if (fieldHashMap.containsKey("hasKeyWords")
	// && fieldHashMap.containsKey("responseTime")) {
	// List<String> keywordList = fieldHashMap.get("hasKeyWords");
	// String responseTime = fieldHashMap.get("responseTimeLong").get(
	// 0);
	// // System.out.println("data");
	// // System.out.println(fieldHashMap);
	// long responseTimeAsLong = Long.parseLong(responseTime);
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTimeInMillis(responseTimeAsLong);
	// // calendar.add(Calendar.MONTH, -1);
	// String dateKey = calendar.get(Calendar.YEAR) + "_"
	// + calendar.get(Calendar.MONTH) + "_"
	// + calendar.get(Calendar.WEEK_OF_MONTH);
	// // System.out.println("dateKey: " + dateKey);
	// // String dateKey = "" + calendar.get(Calendar.WEEK_OF_YEAR);
	//
	// if (calendar.compareTo(minKey) < 0) {
	// // minKey.clear();
	// // minKey.set(calendar.get(Calendar.YEAR),
	// // calendar.get(Calendar.MONTH),
	// // calendar.get(Calendar.DATE));
	// minKey = Calendar.getInstance();
	// minKey.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
	// minKey.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
	// minKey.set(Calendar.WEEK_OF_MONTH,
	// calendar.get(Calendar.WEEK_OF_MONTH));
	// }
	// if (calendar.compareTo(maxKey) > 0) {
	// // maxKey.clear();
	// // maxKey.set(calendar.get(Calendar.YEAR),
	// // calendar.get(Calendar.MONTH),
	// // calendar.get(Calendar.DATE));
	// maxKey = Calendar.getInstance();
	// maxKey.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
	// maxKey.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
	// maxKey.set(Calendar.WEEK_OF_MONTH,
	// calendar.get(Calendar.WEEK_OF_MONTH));
	// }
	// // System.out.println("datekey: " + dateKey);
	// // System.out.println("min: " + dateFormat.format(minKey));
	// // System.out.println("max: " + dateFormat.format(maxKey));
	// for (String keyword : keywordList) {
	// if (productHash.containsKey(keyword)) {
	// String keywordLocalName = removeURI(keyword);
	// HashMap<String, Integer> keywordHashMap;
	// int count = 0;
	// if (keywordCountsByMonth.containsKey(keywordLocalName)) {
	// keywordHashMap = keywordCountsByMonth
	// .get(keywordLocalName);
	// if (keywordHashMap.containsKey(dateKey)) {
	// count = keywordHashMap.get(dateKey);
	// }
	// } else {
	// keywordHashMap = new HashMap<String, Integer>();
	// }
	// count = count + 1;
	// keywordHashMap.put(dateKey, count);
	// keywordCountsByMonth.put(keywordLocalName,
	// keywordHashMap);
	// }
	// }
	// }
	// }
	//
	// // System.out.println(keywordCountsByMonth);
	//
	// Set<String> keywordCountSet = keywordCountsByMonth.keySet();
	// for (String keyword : keywordCountSet) {
	// if (productHash.containsKey(keyword)) {
	// mainChartData.getLabel().add(keyword);
	// }
	// }
	//
	// Calendar currentDate = Calendar.getInstance();
	// currentDate.set(Calendar.YEAR, minKey.get(Calendar.YEAR));
	// currentDate.set(Calendar.MONTH, minKey.get(Calendar.MONTH));
	// currentDate.set(Calendar.WEEK_OF_MONTH,
	// minKey.get(Calendar.WEEK_OF_MONTH));
	// while (currentDate.compareTo(maxKey) <= 0) {
	// ChartObj chartObj = new ChartObj();
	// chartObj.setLabel(dateFormat.format(currentDate));
	// Set<String> keySet = keywordCountsByMonth.keySet();
	// for (String keyword : keySet) {
	// String key = currentDate.get(Calendar.YEAR) + "_"
	// + currentDate.get(Calendar.MONTH) + "_"
	// + currentDate.get(Calendar.WEEK_OF_MONTH);
	// int count = 0;
	// if (keywordCountsByMonth.get(keyword).containsKey(key)) {
	// count = keywordCountsByMonth.get(keyword).get(key);
	// }
	// chartObj.getValues().add(count);
	// }
	// currentDate.add(Calendar.WEEK_OF_MONTH, 1);
	// mainChartData.getValues().add(chartObj);
	// }
	//
	// // while (currentDate.compareTo(maxKey) <= 0) {
	// // mainChartData.getLabel().
	// // currentDate.add(Calendar.MONTH, 1);
	// // }
	// // mainChartData.getKeywordRecords().put("Date", dateRecord);
	//
	// // int minValue, maxValue;
	// // Set<String> keywordCountSet = keywordCountsByMonth.keySet();
	// // for (String keyword : keywordCountSet) {
	// // mainChartData.getLabel().add(keyword);
	// // minValue = 999999;
	// // maxValue = 000000;
	// //
	// // HashMap<String, Integer> monthCountHash = keywordCountsByMonth
	// // .get(keyword);
	// // currentDate = Calendar.getInstance();
	// // currentDate.set(minKey.get(Calendar.YEAR),
	// // minKey.get(Calendar.MONTH), minKey.get(Calendar.DATE));
	// // // System.out.println(dateFormat.format(date));
	// // // System.out.println(dateFormat.format(minKey));
	// // // System.out.println(dateFormat.format(maxKey));
	// // while (currentDate.compareTo(maxKey) <= 0) {
	// // String key = currentDate.get(Calendar.YEAR) + "_"
	// // + currentDate.get(Calendar.MONTH);
	// // int count = 0;
	// // if (monthCountHash.containsKey(key)) {
	// // count = monthCountHash.get(key);
	// // }
	// // keywordRecord.getValues().add(count);
	// // if (count < minValue) {
	// // minValue = count;
	// // } else if (count > maxValue) {
	// // maxValue = count;
	// // }
	// // currentDate.add(Calendar.MONTH, 1);
	// // }
	// // keywordRecord.setMinValue(minValue);
	// // keywordRecord.setMaxValue(maxValue);
	// // trendGraphData.getKeywordRecords().put(keyword, keywordRecord);
	// // trendGraphData.getKeywordNames().add(keyword);
	// // }
	//
	// return mainChartData;
	// }
	//
	// public MainChartData getTrendGraphDataMonthly() {
	// MainChartData mainChartData = new MainChartData();
	//
	// HashMap<String, HashMap<String, List<String>>> currentResults =
	// SearchManager.currentResults;
	// Calendar minKey = Calendar.getInstance();
	// minKey.set(2015, 12, 20);
	//
	// Calendar maxKey = Calendar.getInstance();
	// maxKey.set(1900, 12, 20);
	//
	// DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
	// Set<String> resultSet = currentResults.keySet();
	// for (String result : resultSet) {
	// HashMap<String, List<String>> fieldHashMap = currentResults
	// .get(result);
	// if (fieldHashMap.containsKey("hasKeyWords")
	// && fieldHashMap.containsKey("responseTime")) {
	// List<String> keywordList = fieldHashMap.get("hasKeyWords");
	// String responseTime = fieldHashMap.get("responseTimeLong").get(
	// 0);
	// long responseTimeAsLong = Long.parseLong(responseTime);
	// Calendar calendar = Calendar.getInstance();
	// Calendar tempCalendar = Calendar.getInstance();
	// tempCalendar.setTimeInMillis(responseTimeAsLong);
	// // tempCalendar.add(Calendar.MONTH, -1);
	// calendar.set(Calendar.YEAR, tempCalendar.get(Calendar.YEAR));
	// calendar.set(Calendar.MONTH, tempCalendar.get(Calendar.MONTH));
	// calendar.set(Calendar.DAY_OF_MONTH,
	// tempCalendar.get(Calendar.DAY_OF_MONTH));
	// String dateKey = calendar.get(Calendar.YEAR) + "_"
	// + calendar.get(Calendar.MONTH);
	//
	// // System.out.println("data");
	// // System.out.println(fieldHashMap);
	// // System.out.println("key: " + dateKey);
	//
	// if (calendar.compareTo(minKey) < 0) {
	// // minKey.clear();
	// // minKey.set(calendar.get(Calendar.YEAR),
	// // calendar.get(Calendar.MONTH),
	// // calendar.get(Calendar.DATE));
	// minKey = Calendar.getInstance();
	// minKey.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
	// minKey.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
	// minKey.set(Calendar.DAY_OF_MONTH,
	// calendar.get(Calendar.DAY_OF_MONTH));
	// // minKey.set(calendar.get(Calendar.YEAR),
	// // calendar.get(Calendar.MONTH));
	// }
	// if (calendar.compareTo(maxKey) > 0) {
	// // maxKey.clear();
	// // maxKey.set(calendar.get(Calendar.YEAR),
	// // calendar.get(Calendar.MONTH),
	// // calendar.get(Calendar.DATE));
	// maxKey = Calendar.getInstance();
	// maxKey.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
	// maxKey.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
	// maxKey.set(Calendar.DAY_OF_MONTH,
	// calendar.get(Calendar.DAY_OF_MONTH));
	// // maxKey.set(calendar.get(Calendar.YEAR),
	// // calendar.get(Calendar.MONTH));
	// }
	// // System.out.println("datekey: " + dateKey);
	// // System.out.println("min: " + dateFormat.format(minKey));
	// // System.out.println("max: " + dateFormat.format(maxKey));
	// for (String keyword : keywordList) {
	// if (productHash.containsKey(keyword)) {
	// String keywordLocalName = removeURI(keyword);
	// HashMap<String, Integer> keywordHashMap;
	// int count = 0;
	// if (keywordCountsByMonth.containsKey(keywordLocalName)) {
	// keywordHashMap = keywordCountsByMonth
	// .get(keywordLocalName);
	// if (keywordHashMap.containsKey(dateKey)) {
	// count = keywordHashMap.get(dateKey);
	// }
	// } else {
	// keywordHashMap = new HashMap<String, Integer>();
	// }
	// count = count + 1;
	// keywordHashMap.put(dateKey, count);
	// keywordCountsByMonth.put(keywordLocalName,
	// keywordHashMap);
	// }
	// }
	// }
	// }
	//
	// // System.out.println("keyword hash");
	// // System.out.println(keywordCountsByMonth);
	// Set<String> keywordCountSet = keywordCountsByMonth.keySet();
	// // mainChartData.getLabel().add(" - ");
	// for (String keyword : keywordCountSet) {
	// if (productHash.containsKey(keyword)) {
	// mainChartData.getLabel().add(keyword);
	// }
	// }
	// // mainChartData.getLabel().add(" - ");
	//
	// Calendar currentDate = Calendar.getInstance();
	// currentDate.set(Calendar.YEAR, minKey.get(Calendar.YEAR));
	// currentDate.set(Calendar.MONTH, minKey.get(Calendar.MONTH));
	// currentDate.set(Calendar.DAY_OF_MONTH,
	// minKey.get(Calendar.DAY_OF_MONTH));
	// while (currentDate.compareTo(maxKey) <= 0) {
	// ChartObj chartObj = new ChartObj();
	// chartObj.setLabel(dateFormat.format(currentDate));
	// Set<String> keySet = keywordCountsByMonth.keySet();
	// for (String keyword : keySet) {
	// String key = currentDate.get(Calendar.YEAR) + "_"
	// + currentDate.get(Calendar.MONTH);
	// int count = 0;
	// if (keywordCountsByMonth.get(keyword).containsKey(key)) {
	// count = keywordCountsByMonth.get(keyword).get(key);
	// }
	// chartObj.getValues().add(count);
	// }
	// currentDate.add(Calendar.MONTH, 1);
	// mainChartData.getValues().add(chartObj);
	// }
	// // System.out.println("chart data");
	// // System.out.println(mainChartData.getValues().toString());
	// if (mainChartData.getValues().size() == 1) {
	// ChartObj chartObj = new ChartObj();
	// chartObj.setLabel(dateFormat.format(currentDate));
	// Set<String> keySet = keywordCountsByMonth.keySet();
	// for (String keyword : keySet) {
	// String key = currentDate.get(Calendar.YEAR) + "_"
	// + currentDate.get(Calendar.MONTH);
	// int count = 0;
	// if (keywordCountsByMonth.get(keyword).containsKey(key)) {
	// count = keywordCountsByMonth.get(keyword).get(key);
	// }
	// chartObj.getValues().add(count);
	// }
	// mainChartData.getValues().add(chartObj);
	// }
	//
	// // while (currentDate.compareTo(maxKey) <= 0) {
	// // mainChartData.getLabel().
	// // currentDate.add(Calendar.MONTH, 1);
	// // }
	// // mainChartData.getKeywordRecords().put("Date", dateRecord);
	//
	// // int minValue, maxValue;
	// // Set<String> keywordCountSet = keywordCountsByMonth.keySet();
	// // for (String keyword : keywordCountSet) {
	// // mainChartData.getLabel().add(keyword);
	// // minValue = 999999;
	// // maxValue = 000000;
	// //
	// // HashMap<String, Integer> monthCountHash = keywordCountsByMonth
	// // .get(keyword);
	// // currentDate = Calendar.getInstance();
	// // currentDate.set(minKey.get(Calendar.YEAR),
	// // minKey.get(Calendar.MONTH), minKey.get(Calendar.DATE));
	// // // System.out.println(dateFormat.format(date));
	// // // System.out.println(dateFormat.format(minKey));
	// // // System.out.println(dateFormat.format(maxKey));
	// // while (currentDate.compareTo(maxKey) <= 0) {
	// // String key = currentDate.get(Calendar.YEAR) + "_"
	// // + currentDate.get(Calendar.MONTH);
	// // int count = 0;
	// // if (monthCountHash.containsKey(key)) {
	// // count = monthCountHash.get(key);
	// // }
	// // keywordRecord.getValues().add(count);
	// // if (count < minValue) {
	// // minValue = count;
	// // } else if (count > maxValue) {
	// // maxValue = count;
	// // }
	// // currentDate.add(Calendar.MONTH, 1);
	// // }
	// // keywordRecord.setMinValue(minValue);
	// // keywordRecord.setMaxValue(maxValue);
	// // trendGraphData.getKeywordRecords().put(keyword, keywordRecord);
	// // trendGraphData.getKeywordNames().add(keyword);
	// // }
	//
	// return mainChartData;
	// }
	//
	// private void retrieveProducts(String ontSchemaName) {
	// if (productHash == null) {
	// ConnHelper connManager = new ConnHelper();
	// AGRepositoryConnection conn = connManager
	// .getConnection(ontSchemaName);
	//
	// AGValueFactory valueFactory = conn.getValueFactory();
	//
	// URI productURI = valueFactory.createURI(ontBaseURI + "Product");
	// productHash = new HashMap<String, Integer>();
	// try {
	// RepositoryResult<Statement> productKeywordStatements = conn
	// .getStatements(null, RDF.TYPE, productURI, false);
	//
	// for (Statement productStatement : productKeywordStatements
	// .asList()) {
	// productHash.put(removeURI(productStatement.getSubject()
	// .stringValue()), 0);
	// }
	// } catch (RepositoryException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }
	//
	// private String removeURI(String key) {
	// String localName = key;
	// int index = key.indexOf("#");
	// if (index > 0) {
	// localName = key.substring(index + 1);
	// }
	// return localName;
	// }

}
