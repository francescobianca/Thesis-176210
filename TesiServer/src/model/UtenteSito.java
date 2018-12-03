package model;

public class UtenteSito {

	private Utente utente;
	private Sito sito;

	public UtenteSito() {

	}

	public UtenteSito(Utente utente, Sito sito) {
		this.utente = utente;
		this.sito = sito;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Sito getSito() {
		return sito;
	}

	public void setSito(Sito sito) {
		this.sito = sito;
	}

}
