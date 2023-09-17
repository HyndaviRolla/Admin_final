package com.prodapt.learningspring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ReportedComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long commentId;

	private String commentContent;
	 
	private boolean isDeleted=false; 
	   private String reportReason;

	@ManyToOne
	 @JoinColumn(name = "user_id")
	    private User user;
    
public String getReportReason() {
	return reportReason;
}

public void setReportReason(String reportReason) {
	this.reportReason = reportReason;
}

public User getUser() {
	return user;
}

public void setUser(User user) {
	this.user = user;
}
	public ReportedComment() {
	}
	public ReportedComment(Long commentId, String commentContent,String reportReason,User user) {
		this.commentId = commentId;
		this.commentContent = commentContent;
		this.reportReason= reportReason;
        this.user=user;
	}  

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	  

}
