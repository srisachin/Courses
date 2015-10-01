package musicdb.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import musicdb.analyzer.GsonHelper;
import musicdb.analyzer.trend.ChartObj;
import musicdb.analyzer.trend.MainChartData;
import musicdb.analyzer.trend.TrendGraph;

/**
 * Servlet implementation class TrendGraphServlet
 */
//@WebServlet("/TrendGraphServlet")
public class TrendGraphServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TrendGraphServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("trend graph get");
		
		System.out.println(request.getParameter("range_start"));
		System.out.println(request.getParameter("range_end"));
		System.out.println(request.getParameter("genres"));

		int rangeStart = Integer.parseInt(request.getParameter("range_start"));
		int rangeEnd = Integer.parseInt(request.getParameter("range_end"));
		String genres = request.getParameter("genres");
	
		TrendGraph trendGraph = new TrendGraph(rangeStart, rangeEnd, genres);
		MainChartData trendGraphData = trendGraph.getTrendGraphData();

		System.out.println("trend");
		// if (mode.equalsIgnoreCase("monthly")) {
		// trendGraphData = trendGraph.getTrendGraphDataMonthly();
		// } else {
		// trendGraphData = trendGraph.getTrendGraphDataWeekly();
		// }

		// String jsonStr = GsonHelper.createJSONString(trendGraphData);
		String jsonStr = GsonHelper.createJSONString(trendGraphData);

		System.out.println(jsonStr);

		PrintWriter out = response.getWriter();
		out.println(jsonStr);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
