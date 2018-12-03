package controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import model.Utente;
import persistence.DatabaseManager;
import persistence.dao.UtenteDao;

public class Registrazione extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		System.out.println("Sono nella servlet di registrazione");

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

		String nome = "";
		String cognome = "";
		String email = "";
		String password = "";

		try {
			JSONObject json = new JSONObject(sb.toString());
			nome = json.getString("nome");
			cognome = json.getString("cognome");
			email = json.getString("email");
			password = json.getString("password");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		UtenteDao utenteDao = DatabaseManager.getInstance().getDaoFactory().getUtenteDAO();

		Utente u = utenteDao.findByPrimaryKey(email);

		if (u != null)
			resp.getWriter().print("UtentePresente");
		else {
			u = new Utente(email, nome, cognome);
			utenteDao.save(u);
			utenteDao.setPassword(u, password);

			resp.getWriter().print("Ok");
		}

	}

}
