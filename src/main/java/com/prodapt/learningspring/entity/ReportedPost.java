package com.prodapt.learningspring.entity;
  
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.prodapt.learningspring.entity.*;
 

	@Entity
	public class ReportedPost {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long reportid;
	    private int postId;
	    private String postContent;
	    private boolean isDeleted;

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

		public boolean isDeleted() {
			return isDeleted;
		}

		public void setDeleted(boolean isDeleted) {
			this.isDeleted = isDeleted;
		}
	
		
	    public User getReportingUser() {
			return user;
		}

		public void setReportingUser(User user) {
			this.user = user;
		}
		public ReportedPost() {
	      
	    }
 

		public ReportedPost( int postId, String postContent,String reportReason,User user) {
		 
			this.postId = postId;
			this.postContent = postContent;
			 
			this.reportReason = reportReason;
			this.user = user;
		}

		public Long getReportid() {
			return reportid;
		}

		public void setReportid(Long reportid) {
			this.reportid = reportid;
		}

		public int getPostId() {
			return postId;
		}

		public void setPostId(int postId) {
			this.postId = postId;
		}

		public String getPostContent() {
			return postContent;
		}

		public void setPostContent(String postContent) {
			this.postContent = postContent;
		}

		     
	    
	}







