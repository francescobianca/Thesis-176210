package persistence.dao;

import java.util.List;

import model.News;

public interface NewsDao {
	
	public void save(News news); // Create

	public News findByPrimaryKey(String loc); // Retrieve

	public List<News> findAll();

	public void update(News news); // Update

	public void delete(News news); // Delete

}
