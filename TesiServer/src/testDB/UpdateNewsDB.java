package testDB;

import java.io.IOException;

import org.xml.sax.SAXException;

import model.News;
import model.Sito;
import persistence.DatabaseManager;
import persistence.dao.NewsDao;
import persistence.dao.SitoDao;
import utility.ParserGoogleSM;

public class UpdateNewsDB {

	public static void main(String[] args) {
		
		ParserGoogleSM parser = new ParserGoogleSM();
		
		NewsDao nDao = DatabaseManager.getInstance().getDaoFactory().getNewsDAO();
		
		SitoDao sDao = DatabaseManager.getInstance().getDaoFactory().getSitoDAO();
		Sito s = sDao.findByPrimaryKey("www.ilmattino.it");
		
		try {
			parser.parseSitemap("sitemapNews.xml");
			News news;
			for (int i=0; i<parser.sizeSM(); i++) {
				
				news = new News();
				if (parser.getLocNews(i) != null) 
					news.setLoc(parser.getLocNews(i));
				if (parser.getFonteNews(i) != null)
					news.setFonte(parser.getFonteNews(i));
				if (parser.getLinguaNews(i) != null)
					news.setLingua(parser.getLinguaNews(i));
				if (parser.getTitoloNews(i) != null) 
					news.setTitolo(parser.getTitoloNews(i));
				if (parser.getDataNews(i) != null)
					news.setData(parser.getDataNews(i));
				if (parser.getKeywords(i) != null)
					news.setKeywords(parser.getKeywords(i));
				if (parser.getLocImmagine(i) != null)
					news.setLocImmagine(parser.getLocImmagine(i));
				news.setHostSito(s);
				
				
				nDao.save(news);
				
			}
			
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
