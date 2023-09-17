package com.prodapt.learningspring.repository;
 
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prodapt.learningspring.entity.Post;
 

public interface PostRepository extends CrudRepository<Post, Integer>{
	@Query(value = "SELECT COUNT(*) FROM post WHERE post.author_id = ?1 AND DATE(post.post_date_time) = DATE(NOW());", nativeQuery = true)
	int CountPostsCreatedTodayBy(int id);

	@Query(value = "SELECT COUNT(*) FROM post WHERE post.author_id = ?1 AND MONTH(post.post_date_time) = MONTH(NOW());", nativeQuery = true)
	int CountPostsCreatedMonthBy(int id);

	@Query(value = "SELECT COUNT(*) FROM post WHERE post.author_id = ?1 AND YEAR(post.post_date_time) = YEAR(NOW());", nativeQuery = true)
	int CountPostsCreatedYearBy(int id);
	  
}
