package sitemaps.statistics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// Classe di utilità che mi serve a stabilire con esattezza se l'xml rispetta i requisiti di una google news sitemap.

public class DomParserXML {

	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;

	private File inputFile;
	private Document doc;

	public DomParserXML() {
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void parseSitemap(String sitemap) throws SAXException, IOException {
		// Path su cui salvo l'xml nel disco.
		String path_folder = "xmlFile/";
		String path_xml = path_folder + "temp.xml";

		// Mi arriva l'url della sitemap --> Passo1: Scaricare il file.
		URL url = new URL(sitemap);
		saveFile(url, path_xml);

		inputFile = new File(path_xml);
		doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
	}

	// Verifico se è un sitemap index
	public boolean isSitemapIndex(String sitemap) throws SAXException, IOException {

		NodeList nList = doc.getElementsByTagName("sitemapindex");

		//System.out.println(sitemap + " " + nList.getLength());

		if (nList.getLength() > 0 && nList != null)
			return true;

		else
			return false;

	}

	// Modifica senza rifare il download e senza i gz
	public boolean isGoogle(String sitemap) throws SAXException, IOException {

		// Questo è il pattern da rispettare se devo verificare che si tratti di una
		// sitemap news.
		String pattern = "http://www.google.com/schemas/sitemap-news/0.9";

		// E' nell'urlset che c'è il path che devo andare a controllare.
		NodeList nList = doc.getElementsByTagName("urlset");
		Node nNode = nList.item(0);

		try {
			if (nNode.getAttributes().getNamedItem("xmlns:news") != null)
				if (nNode.getAttributes().getNamedItem("xmlns:news").getTextContent().equals(pattern))
					return true;
		} catch (NullPointerException e) {

		}

		return false;
	}

	// Passo anche il path del file già in memoria
	public boolean isGoogleNewsSitemapWithoutDownload(String sitemap, String filename)
			throws SAXException, IOException {

		File inputFile;
		Document doc;

		// Questo è il pattern da rispettare se devo verificare che si tratti di una
		// sitemap news.
		String pattern = "http://www.google.com/schemas/sitemap-news/0.9";

		inputFile = new File(filename);

		doc = dBuilder.parse(inputFile);

		doc.getDocumentElement().normalize();

		// E' nell'urlset che c'è il path che devo andare a controllare.
		NodeList nList = doc.getElementsByTagName("urlset");
		Node nNode = nList.item(0);

		try {
			if (nNode.getAttributes().getNamedItem("xmlns:news") != null)
				if (nNode.getAttributes().getNamedItem("xmlns:news").getTextContent().equals(pattern))
					return true;
		} catch (NullPointerException e) {

		}

		return false;

	}

	// Ricevuto l'url si scarica il source dell'xml e verifica se è news sitemap
	public boolean isGoogleNewsSitemap(String sitemap) throws SAXException, IOException {

		File inputFile;
		Document doc;

		// Questo è il pattern da rispettare se devo verificare che si tratti di una
		// sitemap news.
		String pattern = "http://www.google.com/schemas/sitemap-news/0.9";

		// Prendo l'estensione
		String ext = FilenameUtils.getExtension(sitemap);

		// Path su cui salvo l'xml nel disco.
		String path_folder = "xmlFile/";
		String path_xml;

		// Mi arriva l'url della sitemap --> Passo1: Scaricare il file.
		URL url = new URL(sitemap);

		// Controllo sull'estensione --> Se è gz devo decomprimere il file.
		if (ext.equals("gz")) {

			path_xml = path_folder + "tempGZ.xml";

			decompressGzipFile(url, path_xml);

			inputFile = new File(path_xml);
			doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			// E' nell'urlset che c'è il path che devo andare a controllare.
			NodeList nList = doc.getElementsByTagName("urlset");
			Node nNode = nList.item(0);

			try {
				if (nNode.getAttributes().getNamedItem("xmlns:news") != null
						|| nNode.getAttributes().getNamedItem("xmlns:n") != null)
					if (nNode.getAttributes().getNamedItem("xmlns:news").getTextContent().equals(pattern)
							|| nNode.getAttributes().getNamedItem("xmlns:n").getTextContent().equals(pattern))
						return true;
			} catch (NullPointerException e) {

			}

		} else {

			// Non è un file .gz
			path_xml = path_folder + "temp.xml";

			saveFile(url, path_xml);

			inputFile = new File(path_xml);
			doc = dBuilder.parse(inputFile);

			doc.getDocumentElement().normalize();

			// E' nell'urlset che c'è il path che devo andare a controllare.
			NodeList nList = doc.getElementsByTagName("urlset");
			Node nNode = nList.item(0);

			try {
				if (nNode.getAttributes().getNamedItem("xmlns:news") != null)
					if (nNode.getAttributes().getNamedItem("xmlns:news").getTextContent().equals(pattern))
						return true;
			} catch (NullPointerException e) {

			}
		}

		return false;

	}

	// Altro metodo isGoogleNewsSitemap con il parametro filename in modo da poterlo
	// salvare su disco.
	public boolean isGoogleNewsSitemap(String sitemap, String filename) throws SAXException, IOException {

		File inputFile;
		Document doc;

		// Questo è il pattern da rispettare se devo verificare che si tratti di una
		// sitemap news.
		String pattern = "http://www.google.com/schemas/sitemap-news/0.9";

		// Prendo l'estensione
		String ext = FilenameUtils.getExtension(sitemap);

		// Path su cui salvo l'xml nel disco.
		String path_xml;

		// Mi arriva l'url della sitemap --> Passo1: Scaricare il file.
		URL url = new URL(sitemap);

		// Controllo sull'estensione --> Se è gz devo decomprimere il file.
		if (ext.equals("gz")) {

			path_xml = filename;

			decompressGzipFile(url, path_xml);

			inputFile = new File(path_xml);
			doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			// E' nell'urlset che c'è il path che devo andare a controllare.
			NodeList nList = doc.getElementsByTagName("urlset");
			Node nNode = nList.item(0);

			try {
				if (nNode.getAttributes().getNamedItem("xmlns:news") != null
						|| nNode.getAttributes().getNamedItem("xmlns:n") != null)
					if (nNode.getAttributes().getNamedItem("xmlns:news").getTextContent().equals(pattern)
							|| nNode.getAttributes().getNamedItem("xmlns:n").getTextContent().equals(pattern))
						return true;
			} catch (NullPointerException e) {

			}

		} else {

			// Non è un file .gz
			path_xml = filename;

			saveFile(url, path_xml);

			inputFile = new File(path_xml);
			doc = dBuilder.parse(inputFile);

			doc.getDocumentElement().normalize();

			// E' nell'urlset che c'è il path che devo andare a controllare.
			NodeList nList = doc.getElementsByTagName("urlset");
			Node nNode = nList.item(0);

			try {
				if (nNode.getAttributes().getNamedItem("xmlns:news") != null)
					if (nNode.getAttributes().getNamedItem("xmlns:news").getTextContent().equals(pattern))
						return true;
			} catch (NullPointerException e) {

			}
		}

		return false;

	}

	private static void decompressGzipFile(URL gzipFile, String newFile) {

		try {
			InputStream fis = gzipFile.openStream();
			GZIPInputStream gis = new GZIPInputStream(fis);
			FileOutputStream fos = new FileOutputStream(newFile);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = gis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			// close resources
			fos.close();
			gis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void saveFile(URL file, String newFile) throws IOException {

		/*InputStream fis = file.openStream();
		FileOutputStream fos = new FileOutputStream(newFile);
		byte[] buffer = new byte[1024];
		int len;
		while ((len = fis.read(buffer)) != -1) {
			fos.write(buffer, 0, len);
		}
		// close resources
		fos.close();*/
		
		File f = new File(newFile);
		FileUtils.copyURLToFile(file, f);
		
	}

}
