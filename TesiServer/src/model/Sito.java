package model;

public class Sito {

	private String host;
	private String googleSitemap;
	private String icon;

	public Sito() {

	}

	public Sito(String host, String googleSitemap, String icon) {
		this.host = host;
		this.googleSitemap = googleSitemap;
		this.icon = icon;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getGoogleSitemap() {
		return googleSitemap;
	}

	public void setGoogleSitemap(String googleSitemap) {
		this.googleSitemap = googleSitemap;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return " Sito: " + host + " googleSitemap: " + googleSitemap + " icon: " + icon;
	}

}
