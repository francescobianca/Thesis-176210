package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.News;
import model.Sito;
import persistence.DatabaseManager;
import persistence.dao.SitoDao;

public class CaricaNews extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("sono nella servlet carica news");

		StringBuilder sb = new StringBuilder();
		BufferedReader reader = req.getReader();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
		} finally {
			reader.close();
		}
		String email = "";
		try {
			JSONObject json = new JSONObject(sb.toString());
			email = json.getString("email");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		System.out.println(email);

		Connection connection = DatabaseManager.getInstance().getDaoFactory().getDataSource().getConnection();
		ResultSet setSitiUtente = null;

		try {
			String query = " select * from utentesito where \"emailutente\" = ? ";

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, email);
			setSitiUtente = statement.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ArrayList<String> sitiSeguitiUtente = new ArrayList<>();

		try {

			while (setSitiUtente.next()) {
				String s = setSitiUtente.getString("hostsito");
				sitiSeguitiUtente.add(s);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		ResultSet setNews = null;
		ArrayList<News> notizie = new ArrayList<>();

		for (String host : sitiSeguitiUtente) {

			connection = DatabaseManager.getInstance().getDaoFactory().getDataSource().getConnection();

			try {

				String query = " select * from news where \"hostsito\" = ? ";

				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, host);
				setNews = statement.executeQuery();

				try {

					News news;
					SitoDao sDao = DatabaseManager.getInstance().getDaoFactory().getSitoDAO();

					while (setNews.next()) {
						String loc = setNews.getString("loc");
						String fonte = setNews.getString("fonte");
						String lingua = setNews.getString("lingua");
						String titolo = setNews.getString("titolo");
						String keywords = setNews.getString("keywords");
						String locImmagine = setNews.getString("locImmagine");

						long secs = setNews.getDate("data").getTime();

						Sito sito = sDao.findByPrimaryKey(setNews.getString("hostSito"));

						news = new News();
						news.setLoc(loc);
						news.setFonte(fonte);
						news.setLingua(lingua);
						news.setTitolo(titolo);
						news.setData(new java.util.Date(secs));
						news.setKeywords(keywords);
						news.setLocImmagine(locImmagine);
						news.setHostSito(sito);

						notizie.add(news);

					}

				} catch (SQLException e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		String risposta = (new JSONArray(notizie).toString());
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.getWriter().print(risposta);
		resp.getWriter().flush();
		resp.getWriter().close();
		return;

	}

}
