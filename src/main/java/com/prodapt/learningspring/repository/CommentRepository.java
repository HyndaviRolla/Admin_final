package com.prodapt.learningspring.repository;

import com.prodapt.learningspring.entity.Comment;
import com.prodapt.learningspring.entity.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	void deleteByPost(Post post);

	@Query(value = "SELECT COUNT(*) FROM comment WHERE comment.user_id = ?1 AND DATE(comment.date) = DATE(NOW());", nativeQuery = true)
	int CountCommentsCreatedTodayBy(int id);
	@Query(value = "SELECT COUNT(*) FROM comment WHERE comment.user_id = ?1 AND YEAR(comment.date) = YEAR(NOW());", nativeQuery = true)
	int CountCommentsCreatedYearBy(int id);

}
