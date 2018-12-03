package model;

import java.util.Date;

public class News {

	private String loc; // Primary Key
	private String fonte;
	private String lingua;
	private String titolo;
	private Date data;
	private String keywords;
	private String locImmagine;
	private Sito hostSito;

	public News() {

	}

	public String getLoc() {
		return loc;
	}
	
	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public String getLingua() {
		return lingua;
	}

	public void setLingua(String lingua) {
		this.lingua = lingua;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getLocImmagine() {
		return locImmagine;
	}

	public void setLocImmagine(String locImmagine) {
		this.locImmagine = locImmagine;
	}

	public Sito getHostSito() {
		return hostSito;
	}

	public void setHostSito(Sito hostSito) {
		this.hostSito = hostSito;
	}

}
