package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UtilDao {

	private DataSource dataSource;

	public UtilDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void dropDatabase() {

		Connection connection = dataSource.getConnection();
		try {
			String delete = "drop SEQUENCE if EXISTS sequenza_id;" + "drop table if exists utentesito;"
					+ "drop table if exists news;" + "drop table if exists sito;" + "drop table if exists utente;";
			PreparedStatement statement = connection.prepareStatement(delete);

			statement.executeUpdate();
			System.out.println("Executed drop database");

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

	public void createDatabase() {

		Connection connection = dataSource.getConnection();
		try {

			String create = "create SEQUENCE sequenza_id;" + "create table utente(\"email\" VARCHAR(255) primary key,"
					+ "nome VARCHAR(255),cognome VARCHAR(255)," + "password VARCHAR(255));"

					+ "create table sito(\"host\" VARCHAR(255) primary key,"
					+ "googlesm VARCHAR(255), icon VARCHAR(255) );"

					+ "create table news(loc VARCHAR(255) primary key,"
					+ "fonte VARCHAR(255),lingua VARCHAR(255),titolo VARCHAR(255),"
					+ "keywords VARCHAR(255),locImmagine VARCHAR(255),"
					+ "data DATE, hostSito VARCHAR(255) REFERENCES sito(\"host\") );"

					+ "create table utentesito(emailUtente VARCHAR(255) REFERENCES utente(\"email\"),"
					+ "hostSito VARCHAR(255) REFERENCES sito(\"host\"), PRIMARY KEY(emailUtente,hostSito));";

			PreparedStatement statement = connection.prepareStatement(create);

			statement.executeUpdate();
			System.out.println("Executed create database");

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

	public void resetDatabase() {

		Connection connection = dataSource.getConnection();
		try {
			String delete = "delete FROM utente";
			PreparedStatement statement = connection.prepareStatement(delete);

			statement.executeUpdate();

			delete = "delete FROM sito";
			statement = connection.prepareStatement(delete);

			statement.executeUpdate();

			delete = "delete FROM news";
			statement = connection.prepareStatement(delete);

			statement.executeUpdate();

			delete = "delete FROM utentesito";
			statement = connection.prepareStatement(delete);

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