package com.prodapt.learningspring.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Comment {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(columnDefinition = "TEXT")
	private String content;

	public Date date;
	@Column(name = "report_count")
	private int reportCount = 0;
	 @Column(name = "reported")
	    private boolean reported = false;
	    public boolean isReported() {
	        return reported;
	    }

	    public void setReported(boolean reported) {
	        this.reported = reported;
	    }
	 
	public Comment() {

	}

	public Comment(Post post, User user, String content) {
		this.post = post;
		this.user = user;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Comment comment = (Comment) o;
		return Objects.equals(id, comment.id);
	}

	public int getReportCount() {
		return reportCount;
	}

	public void setReportCount(int reportCount) {
		this.reportCount = reportCount;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
