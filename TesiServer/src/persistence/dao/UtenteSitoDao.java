package persistence.dao;

import java.util.List;

import model.UtenteSito;;

public interface UtenteSitoDao {

	public void save(UtenteSito utenteSito); // Create

	public UtenteSito findByPrimaryKey(String email, String host); // Retrieve

	public List<UtenteSito> findAll();

	public void update(UtenteSito utenteSito); // Update

	public void delete(UtenteSito utenteSito); // Delete

}
