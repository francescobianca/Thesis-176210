package utility;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParserGoogleSM {

	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;

	private File inputFile;
	private Document doc;
	private NodeList nList;

	public ParserGoogleSM() {
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void parseSitemap(String googleSM) throws SAXException, IOException {
		inputFile = new File(googleSM);
		doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();

		nList = doc.getElementsByTagName("url");
	}

	public int sizeSM() {
		return nList.getLength();
	}

	public String getLocNews(int index) {

		Node nNode = nList.item(index);

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			try {
				return eElement.getElementsByTagName("loc").item(0).getTextContent();
			} catch (NullPointerException e) {

			}
		}

		return null;

	}

	public String getFonteNews(int index) {

		Node nNode = nList.item(index);

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			try {
				return eElement.getElementsByTagName("news:name").item(0).getTextContent();
			} catch (NullPointerException e) {

			}
		}

		return null;
	}

	public String getLinguaNews(int index) {

		Node nNode = nList.item(index);

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			try {
				return eElement.getElementsByTagName("news:language").item(0).getTextContent();
			} catch (NullPointerException e) {

			}
		}

		return null;
	}

	public String getTitoloNews(int index) {

		Node nNode = nList.item(index);

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			try {
				return eElement.getElementsByTagName("news:title").item(0).getTextContent();
			} catch (NullPointerException e) {

			}

		}

		return null;
	}

	public Date getDataNews(int index) {

		Node nNode = nList.item(index);

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			String date = eElement.getElementsByTagName("news:publication_date").item(0).getTextContent();

			DateFormat[] format = new DateFormat[2];
			format[0] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+mm:ss");
			format[1] = new SimpleDateFormat("yyyy-MM-dd");

			if (date != null)
				for (int i = 0; i < format.length;) {
					try {
						return format[i].parse(date);
					} catch (ParseException e) {
						i++;
					}
				}

		}

		return null;
	}

	public String getKeywords(int index) {

		Node nNode = nList.item(index);

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			try {
				return eElement.getElementsByTagName("news:keywords").item(0).getTextContent();
			} catch (NullPointerException e) {

			}
		}

		return null;
	}

	public String getLocImmagine(int index) {

		Node nNode = nList.item(index);

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			try {
				return eElement.getElementsByTagName("image:loc").item(0).getTextContent();
			} catch (NullPointerException e) {

			}
		}

		return null;
	}

}
