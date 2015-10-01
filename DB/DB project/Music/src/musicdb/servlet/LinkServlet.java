package musicdb.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import musicdb.analyzer.GsonHelper;
import musicdb.analyzer.arc.ArcDiagramData;
import musicdb.analyzer.arc.LinkManager;
import musicdb.analyzer.arc.LinkManager2;
import musicdb.analyzer.arc.LinkManager3;
import musicdb.analyzer.arc.LinkManager4;

/**
 * Servlet implementation class LinkServlet
 */
//@WebServlet("/LinkServlet")
public class LinkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LinkServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("link get");
		String schemaOntName = request.getParameter("schema_ont");

		LinkManager3 linkManager = new LinkManager3();
		ArcDiagramData arcDiagramData = linkManager.getData();

		String jsonStr = GsonHelper.createJSONString(arcDiagramData);

		BufferedWriter writer = null;
		try {
			// create a temporary file
			File logFile = new File("InputJSONdec.txt");

			// This will output the full path where the file will be written
			// to...
			System.out.println(logFile.getCanonicalPath());

			writer = new BufferedWriter(new FileWriter(logFile));
			writer.write(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}
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
