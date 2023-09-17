package com.prodapt.learningspring.repository;
 
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.prodapt.learningspring.entity.ReportedComment;
import com.prodapt.learningspring.entity.ReportedPost;
import com.prodapt.learningspring.entity.User;

public interface ReportedPostRepository extends CrudRepository<ReportedPost,Integer> {

	List<ReportedPost> findByPostId(int postId);

	boolean existsByUserAndPostId(User User, int postId);	
	
}
