package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import model.Sito;
import persistence.dao.SitoDao;

public class SitoDaoJDBC implements SitoDao {

	private DataSource dataSource;

	public SitoDaoJDBC(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void save(Sito sito) {
		Connection connection = this.dataSource.getConnection();
		try {
			String insert = "insert into sito(host, googlesm, icon) values (?,?,?)";
			PreparedStatement statement = connection.prepareStatement(insert);
			statement.setString(1, sito.getHost());
			statement.setString(2, sito.getGoogleSitemap());
			statement.setString(3, sito.getIcon());
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
	public Sito findByPrimaryKey(String host) {
		Connection connection = this.dataSource.getConnection();
		Sito sito = null;
		try {
			PreparedStatement statement;
			String query = "select * from sito where host = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, host);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				sito = new Sito();
				sito.setHost(result.getString("host"));
				sito.setGoogleSitemap(result.getString("googlesm"));
				sito.setIcon(result.getString("icon"));
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
		return sito;
	}

	@Override
	public List<Sito> findAll() {
		Connection connection = this.dataSource.getConnection();
		List<Sito> siti = new LinkedList<>();
		try {
			Sito sito;
			PreparedStatement statement;
			String query = "select * from sito";
			statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				sito = new Sito();
				sito.setHost(result.getString("host"));
				sito.setGoogleSitemap(result.getString("googlesm"));
				sito.setIcon(result.getString("icon"));

				siti.add(sito);
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
		return siti;
	}

	@Override
	public void update(Sito sito) {
		Connection connection = this.dataSource.getConnection();
		try {
			String update = "update sito SET googlesm = ?, icon = ? WHERE host=?";
			PreparedStatement statement = connection.prepareStatement(update);
			statement.setString(1, sito.getGoogleSitemap());
			statement.setString(2, sito.getIcon());
			statement.setString(3, sito.getHost());
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
	public void delete(Sito sito) {
		Connection connection = this.dataSource.getConnection();
		try {
			String delete = "delete FROM sito WHERE host = ? ";
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, sito.getHost());
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
