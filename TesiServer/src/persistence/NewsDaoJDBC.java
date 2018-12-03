package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import model.News;
import model.Sito;
import persistence.dao.NewsDao;
import persistence.dao.SitoDao;

public class NewsDaoJDBC implements NewsDao {

	private DataSource dataSource;

	public NewsDaoJDBC(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void save(News news) {
		Connection connection = this.dataSource.getConnection();
		try {
			String insert = "insert into news(loc,fonte, lingua, titolo, data, keywords, locImmagine, hostSito) values (?,?,?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(insert);
			statement.setString(1, news.getLoc());
			statement.setString(2, news.getFonte());
			statement.setString(3, news.getLingua());
			statement.setString(4, news.getTitolo());

			long secs = news.getData().getTime();
			statement.setDate(5, new java.sql.Date(secs));

			statement.setString(6, news.getKeywords());
			statement.setString(7, news.getLocImmagine());
			statement.setString(8, news.getHostSito().getHost());

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
	public News findByPrimaryKey(String loc) {
		Connection connection = this.dataSource.getConnection();
		News news = null;
		try {
			PreparedStatement statement;
			String query = "select * from news where loc = ?";
			statement = connection.prepareStatement(query);
			statement.setString(1, loc);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				news = new News();
				news.setLoc(result.getString("loc"));
				news.setFonte(result.getString("fonte"));
				news.setLingua(result.getString("lingua"));
				news.setTitolo(result.getString("titolo"));
				news.setKeywords(result.getString("keywords"));
				news.setLocImmagine(result.getString("locImmagine"));
				news.setData(result.getDate("data"));

				Sito s = new Sito();
				SitoDao sDao = DatabaseManager.getInstance().getDaoFactory().getSitoDAO();
				s = sDao.findByPrimaryKey(result.getString("hostSito"));
				news.setHostSito(s);
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
		return news;
	}

	@Override
	public List<News> findAll() {
		Connection connection = this.dataSource.getConnection();
		List<News> listaNews = new LinkedList<>();
		try {
			News news;
			PreparedStatement statement;
			String query = "select * from news";
			statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				news = new News();
				news.setLoc(result.getString("loc"));
				news.setFonte(result.getString("fonte"));
				news.setLingua(result.getString("lingua"));
				news.setTitolo(result.getString("titolo"));
				news.setKeywords(result.getString("keywords"));
				news.setLocImmagine(result.getString("locImmagine"));
				news.setData(result.getDate("data"));

				Sito s = new Sito();
				SitoDao sDao = DatabaseManager.getInstance().getDaoFactory().getSitoDAO();
				s = sDao.findByPrimaryKey(result.getString("hostSito"));
				news.setHostSito(s);

				listaNews.add(news);
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
		return listaNews;
	}

	@Override
	public void update(News news) {
		Connection connection = this.dataSource.getConnection();
		try {
			String update = "update news SET fonte = ?, lingua = ?, titolo = ?, keywords = ?, locImmagine = ?, data = ?, hostSito = ? WHERE loc=?";
			PreparedStatement statement = connection.prepareStatement(update);

			statement.setString(1, news.getFonte());
			statement.setString(2, news.getLingua());
			statement.setString(3, news.getTitolo());
			statement.setString(4, news.getKeywords());
			statement.setString(5, news.getLocImmagine());

			long data = news.getData().getTime();
			statement.setDate(6, new java.sql.Date(data));

			statement.setString(7, news.getHostSito().getHost());
			statement.setString(8, news.getLoc());

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
	public void delete(News news) {
		Connection connection = this.dataSource.getConnection();
		try {
			String delete = "delete FROM news WHERE loc = ? ";
			PreparedStatement statement = connection.prepareStatement(delete);
			statement.setString(1, news.getLoc());
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
