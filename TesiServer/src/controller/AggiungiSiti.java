package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Sito;
import model.Utente;
import model.UtenteSito;
import persistence.DatabaseManager;
import persistence.dao.SitoDao;
import persistence.dao.UtenteDao;
import persistence.dao.UtenteSitoDao;

public class AggiungiSiti extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		System.out.println("Sono nella servlet aggiungi siti");

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
		JSONArray sitesJSON = null;
		ArrayList<String> sites = new ArrayList<>();

		try {
			JSONObject json = new JSONObject(sb.toString());
			email = json.getString("email");
			sitesJSON = json.getJSONArray("sites");
			for (int i = 0; i<sitesJSON.length(); i++)
				sites.add((String) sitesJSON.get(i));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// Ora per l'utente devo aggiungere questi siti ai suoi preferiti.
		UtenteDao uDao = DatabaseManager.getInstance().getDaoFactory().getUtenteDAO();
		SitoDao sDao = DatabaseManager.getInstance().getDaoFactory().getSitoDAO();
		
		UtenteSitoDao usDao = DatabaseManager.getInstance().getDaoFactory().getUtenteSitoDAO();
		
		for (String site : sites) {
			
			Sito s = sDao.findByPrimaryKey(site);
			Utente u = uDao.findByPrimaryKey(email);
			
			UtenteSito utente_sito = new UtenteSito(u, s);
			usDao.save(utente_sito);
			
		}
		
	}

}
