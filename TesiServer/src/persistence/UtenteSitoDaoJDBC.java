package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import model.Sito;
import model.Utente;
import model.UtenteSito;
import persistence.dao.SitoDao;
import persistence.dao.UtenteDao;
import persistence.dao.UtenteSitoDao;

public class UtenteSitoDaoJDBC implements UtenteSitoDao {

	private DataSource dataSource;

	public UtenteSitoDaoJDBC(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void save(UtenteSito utenteSito) {
		Connection connection = this.dataSource.getConnection();
		try {
			String insert = "insert into utentesito(emailUtente,hostSito) values (?,?)";
			PreparedStatement statement = connection.prepareStatement(insert);
			statement.setString(1, utenteSito.getUtente().getEmail());
			statement.setString(2, utenteSito.getSito().getHost());

			statement.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

	@Override
	public UtenteSito findByPrimaryKey(String email, String host) {
		Connection connection = this.dataSource.getConnection();
		UtenteSito utenteSito = null;
		try {
			PreparedStatement statement;
			String query = "select * from utentesito where emailUtente = ? and  hostSito = ? ";
			statement = connection.prepareStatement(query);
			statement.setString(1, email);
			statement.setString(2, host);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				utenteSito = new UtenteSito();

				Utente u = new Utente();
				UtenteDao uDao = DatabaseManager.getInstance().getDaoFactory().getUtenteDAO();
				u = uDao.findByPrimaryKey(result.getString("emailUtente"));

				utenteSito.setUtente(u);

				Sito s = new Sito();
				SitoDao sDao = DatabaseManager.getInstance().getDaoFactory().getSitoDAO();
				s = sDao.findByPrimaryKey(result.getString("hostSito"));

				utenteSito.setSito(s);
			}

		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return utenteSito;
	}

	@Override
	public List<UtenteSito> findAll() {
		Connection connection = this.dataSource.getConnection();
		List<UtenteSito> listaUtenteSito = new LinkedList<>();
		try {
			UtenteSito utenteSito;
			PreparedStatement statement;
			String query = "select * from utentesito";
			statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				utenteSito = new UtenteSito();

				Utente u = new Utente();
				UtenteDao uDao = DatabaseManager.getInstance().getDaoFactory().getUtenteDAO();
				u = uDao.findByPrimaryKey(result.getString("emailUtente"));

				utenteSito.setUtente(u);

				Sito s = new Sito();
				SitoDao sDao = DatabaseManager.getInstance().getDaoFactory().getSitoDAO();
				s = sDao.findByPrimaryKey(result.getString("hostSito"));

				utenteSito.setSito(s);

				listaUtenteSito.add(utenteSito);
			}
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		return listaUtenteSito;
	}

	@Override
	public void update(UtenteSito utenteSito) {

	}

	@Override
	public void delete(UtenteSito utenteSito) {
		Connection connection = this.dataSource.getConnection();
		try {
			String delete = "delete FROM utentesito WHERE emailUtente = ? and hostSito = ? ";
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, utenteSito.getUtente().getEmail());
			statement.setString(2, utenteSito.getSito().getHost());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
	}

}
