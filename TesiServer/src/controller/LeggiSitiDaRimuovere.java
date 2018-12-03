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

import persistence.DatabaseManager;

public class LeggiSitiDaRimuovere extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		System.out.println("Sono nella servlet leggi siti da rimuovere");

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
		ResultSet setSiti = null;

		try {
			String query = " select * from utentesito where \"emailutente\" = ? ";

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, email);
			setSiti = statement.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String s = null;
		ArrayList<String> hostSitiDaEliminare = new ArrayList<>();
		try {

			while (setSiti.next()) {
					
				s = setSiti.getString("hostsito");
				System.out.println(s);
				hostSitiDaEliminare.add(s);
	
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		String risposta = (new JSONArray(hostSitiDaEliminare).toString());
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.getWriter().print(risposta);
		resp.getWriter().flush();
		resp.getWriter().close();
		return;
		
	}

}
