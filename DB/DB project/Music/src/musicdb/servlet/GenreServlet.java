package musicdb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import musicdb.analyzer.DataTableObj;
import musicdb.analyzer.GenreManager;
import musicdb.analyzer.GsonHelper;

/**
 * Servlet implementation class OntServlet
 */
//@WebServlet("/GenreServlet")
public class GenreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GenreServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResp	onse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("genre servlet get");
		String ontName = "CustomerSurvey";
		String sEcho = request.getParameter("sEcho");
//		System.out.println("sEcho " + sEcho);
		GenreManager genreManager = new GenreManager();
		DataTableObj dataTableObj = genreManager.getCountryList();

		String json = GsonHelper.createJSONString(dataTableObj);

//		System.out.println(json);
		PrintWriter out = response.getWriter();
		out.println(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("genre servlet get");
		String ontName = "CustomerSurvey";
		String sEcho = request.getParameter("sEcho");
		System.out.println("sEcho " + sEcho);
		GenreManager genreManager = new GenreManager();
		DataTableObj dataTableObj = genreManager.getCountryList();

		String json = GsonHelper.createJSONString(dataTableObj);

		System.out.println(json);
		PrintWriter out = response.getWriter();
		out.println(json);
	}

}
