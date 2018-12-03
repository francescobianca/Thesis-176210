package testDB;

import model.Sito;
import persistence.DAOFactory;
import persistence.DatabaseManager;
import persistence.UtilDao;
import persistence.dao.SitoDao;

public class MainJDBC {

	public static void main(String args[]) {

		/*
		 * DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL); UtilDao
		 * util = factory.getUtilDAO();
		 * 
		 * util.dropDatabase();
		 * 
		 * util.createDatabase();
		 */

		// Carico un pò di siti.

		Sito s1 = new Sito();
		s1.setHost("www.sportmediaset.mediaset.it");
		s1.setGoogleSitemap("http://www.sportmediaset.mediaset.it/seo/sitemap_articoli_ultimigiorni_googlenews.xml");

		Sito s2 = new Sito();
		s2.setHost("tg24.sky.it");
		s2.setGoogleSitemap("https://tg24.sky.it/sitemap.gnews.xml");

		Sito s3 = new Sito();
		s3.setHost("www.ilmattino.it");
		s3.setGoogleSitemap("https://www.ilmattino.it/?sez=XML&p=MapNews");

		SitoDao sDao = DatabaseManager.getInstance().getDaoFactory().getSitoDAO();

		sDao.save(s1);
		sDao.save(s2);
		sDao.save(s3);

	}

}