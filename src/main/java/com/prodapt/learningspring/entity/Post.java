package com.prodapt.learningspring.entity;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.prodapt.learningspring.entity.Comment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
@Entity
public class Post {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id; 

	private String content;

	@ManyToOne 
	@JoinColumn(name = "author_id", referencedColumnName = "id")
	private User author;
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	public List<Comment> comments;
	@Column(nullable = false)
	@CreationTimestamp
	private Date postDateTime;
	@Column(name = "report_count")
	private int reportCount = 0;


	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public Date getPostDateTime() {
		return postDateTime;
	}
	public void setPostDateTime(Date postDateTime) {
		this.postDateTime = postDateTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public int getReportCount() {
		return reportCount;
	}
	public void setReportCount(int reportCount) {
		this.reportCount = reportCount;
	}
 
}
