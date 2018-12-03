package persistence;

import persistence.dao.NewsDao;
import persistence.dao.SitoDao;
import persistence.dao.UtenteDao;
import persistence.dao.UtenteSitoDao;

class PostgresDAOFactory extends DAOFactory {

	private static DataSource dataSource;

	// --------------------------------------------

	static {
		try {
			Class.forName("org.postgresql.Driver").newInstance();
			
			// dbLocale
			dataSource = new DataSource("jdbc:postgresql://localhost:5432/AppTesi","postgres", "postgres");

			// dbRemoto
			/*dataSource = new DataSource("jdbc:postgresql://packy.db.elephantsql.com:5432/hvkahtzn", "hvkahtzn",
					"Lk4jd8cKUMF4Jox30NF5R4I5cWERb6vh");*/
			

		} catch (Exception e) {
			System.err.println("PostgresDAOFactory.class: failed to load MySQL JDBC driver\n" + e);
			e.printStackTrace();
		}
		
	}
	
	@Override
	public UtenteDao getUtenteDAO() {
		return new UtenteDaoJDBC(dataSource);
	}

	@Override
	public SitoDao getSitoDAO() {
		return new SitoDaoJDBC(dataSource);
	}

	@Override
	public NewsDao getNewsDAO() {
		return new NewsDaoJDBC(dataSource);
	}

	@Override
	public UtenteSitoDao getUtenteSitoDAO() {
		return new UtenteSitoDaoJDBC(dataSource);
	}
	
	@Override
	public UtilDao getUtilDAO() {
		return new UtilDao(dataSource);
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

}