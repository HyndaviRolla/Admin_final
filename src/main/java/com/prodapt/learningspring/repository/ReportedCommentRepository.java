 
package com.prodapt.learningspring.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.prodapt.learningspring.entity.ReportedComment;
import com.prodapt.learningspring.entity.User;

public interface ReportedCommentRepository extends CrudRepository<ReportedComment, Long> {

	Optional<ReportedComment> findByCommentIdAndUserId(Long commentId, Long loggedInUserId);

	boolean existsByUserAndCommentId(User user, Long commentId);
	
}
