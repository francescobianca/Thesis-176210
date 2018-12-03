package persistence.dao;

import java.util.List;

import model.Sito;

public interface SitoDao {

	public void save(Sito sito); // Create

	public Sito findByPrimaryKey(String host); // Retrieve

	public List<Sito> findAll();

	public void update(Sito sito); // Update

	public void delete(Sito sito); // Delete

}
