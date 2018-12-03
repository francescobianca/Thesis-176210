package controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import persistence.DatabaseManager;
import persistence.UtenteCredenziali;
import persistence.dao.UtenteDao;

public class Login extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		System.out.println("Sono nella servlet di login");

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
		String password = "";

		try {
			JSONObject json = new JSONObject(sb.toString());
			email = json.getString("email");
			password = json.getString("password");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		/*System.out.println(email);
		System.out.println(password);*/
		
		HttpSession session = req.getSession();

		UtenteDao dao = DatabaseManager.getInstance().getDaoFactory().getUtenteDAO();
		UtenteCredenziali utente = dao.findByPrimaryKeyCredential(email);

		if (utente != null) {
			if (password.equals(utente.getPassword())) {
				session.setAttribute("email", email);
				session.setAttribute("nome", utente.getNome());
				session.setAttribute("cognome", utente.getCognome());
				
				resp.getWriter().print("Ok");
			} else {
				resp.getWriter().print("DatiErrati");
			}
		} else {
			resp.getWriter().print("DatiErrati");
		}
			

	}

}
