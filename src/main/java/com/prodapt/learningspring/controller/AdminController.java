package com.prodapt.learningspring.controller;

import java.security.Principal;
import com.prodapt.learningspring.SecurityConfiguration;

import java.util.List;
import java.util.Optional;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.prodapt.learningspring.entity.Comment;
import com.prodapt.learningspring.entity.Post;
import com.prodapt.learningspring.entity.ReportedComment;
import com.prodapt.learningspring.entity.ReportedPost;
import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.repository.CommentRepository;
import com.prodapt.learningspring.repository.LikeCRUDRepository;
import com.prodapt.learningspring.repository.PostRepository;
import com.prodapt.learningspring.repository.ReportedCommentRepository;
import com.prodapt.learningspring.repository.ReportedPostRepository;
import com.prodapt.learningspring.repository.UserRepository;
import com.prodapt.learningspring.service.DomainUserService;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/Admin")

public class AdminController {
	@Autowired
	private UserRepository userRepository; 
	@Autowired
	private SecurityConfiguration securityConfiguration;
	@Autowired
	private CommentRepository commentRepository;	  
	@Autowired
	private PostRepository postRepository;
 
	@Autowired
	private ReportedPostRepository reportedPostRepository;
	@Autowired
	private DomainUserService domainUserService;
	@Autowired
	private ReportedCommentRepository reportedCommentRepository;
	@Autowired
	private LikeCRUDRepository likeCRUDRepository;

	@GetMapping("/reported-entries")
	public String getReportedEntries(Model model) {
		Iterable<ReportedComment> reportedComments = reportedCommentRepository.findAll();
		Iterable<ReportedPost> reportedPosts = reportedPostRepository.findAll();

		model.addAttribute("reportedComments", reportedComments);
		model.addAttribute("reportedPosts", reportedPosts);

		return "reportedEntries";
	}
@PostMapping("/post/{postId}/comment/report")	
public String reportComment(@PathVariable int postId, @RequestParam("commentId") Long commentId,
                            @RequestParam("commentContent") String commentContent,@RequestParam("reportReason") String reportReason,
                            @AuthenticationPrincipal UserDetails userDetails) {

    String username = userDetails.getUsername();
    User user = userRepository.findByName(username).orElse(null);

    if (user != null) {
        Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isPresent()) {
           if(!comment.get().getUser().equals(user)) {
            if (!reportedCommentRepository.existsByUserAndCommentId(user, commentId)) {
                comment.get().setReportCount(comment.get().getReportCount() + 1);
                commentRepository.save(comment.get());

                if (comment.get().getReportCount() >= 5) {
                    commentRepository.deleteById(comment.get().getId());
                    Optional<ReportedComment> reportedComment = reportedCommentRepository.findById(comment.get().getId());
                    if (reportedComment.isPresent()) {
                        reportedComment.get().setDeleted(true);
                        reportedCommentRepository.save(reportedComment.get());
                    }
                }
                
                ReportedComment reportedComment = new ReportedComment(commentId, commentContent,reportReason, user);
                reportedCommentRepository.save(reportedComment);
            }
        }
    }
}
    return "redirect:/forum/post/{postId}";
}

	@PostMapping("/post/{postId}/report")
	public String reportPost(@PathVariable int postId, Model model, @RequestParam("postContent") String postContent, @RequestParam("reportReason") String reportReason,
	        @AuthenticationPrincipal UserDetails userDetails) {

	    String username = userDetails.getUsername();
	    User user = userRepository.findByName(username).orElse(null);

	    if (user != null) {
	        Optional<Post> post = postRepository.findById(postId);
	        if (post.isPresent()) {
	       if(!post.get().getAuthor().equals(user)) {
	            if (!reportedPostRepository.existsByUserAndPostId(user, postId)) {
	                post.get().setReportCount(post.get().getReportCount() + 1);
	                postRepository.save(post.get());
	                if (post.get().getReportCount() >= 5) {
	                    postRepository.deleteById(post.get().getId());
	                    Optional<ReportedPost> reportedPost = reportedPostRepository.findById(post.get().getId());
	                    if (reportedPost.isPresent()) {
	                        reportedPost.get().setDeleted(true);
	                        reportedPostRepository.save(reportedPost.get());
	                    }
	                } 
	                ReportedPost reportedPost = new ReportedPost(postId, postContent, reportReason, user);
	                reportedPostRepository.save(reportedPost);
	            }
	        }
	    }
	    }
	    return "redirect:/forum/post/{postId}";
	} 
	@PostMapping("/ignore/post")
	public String ignoreReportedPost(@RequestParam("postId") Integer postId) {
	    // Check if the comment with the given commentId exists in the reportedComment entity
	    Optional<ReportedPost> reportedPostOptional = reportedPostRepository.findById(postId);
	    
	    if (reportedPostOptional.isPresent()) {
	        ReportedPost reportedPost = reportedPostOptional.get();
	      
	        reportedPostRepository.delete(reportedPost);
	    }
	    
	    // Redirect to a suitable page (e.g., a page showing the list of reported comments)
	    return "redirect:/Admin/reported-entries"; // Adjust the URL as needed
	}
	@PostMapping("/ignore")
	public String ignoreReportedComment(@RequestParam("commentId") Long commentId) {
	    // Check if the comment with the given commentId exists in the reportedComment entity
	    Optional<ReportedComment> reportedCommentOptional = reportedCommentRepository.findById(commentId);
	    
	    if (reportedCommentOptional.isPresent()) {
	        ReportedComment reportedComment = reportedCommentOptional.get();
	      
	        reportedCommentRepository.delete(reportedComment);
	    }
	    
	    // Redirect to a suitable page (e.g., a page showing the list of reported comments)
	    return "redirect:/Admin/reported-entries"; // Adjust the URL as needed
	}

//	  	@PostMapping("/post/{postId}/comment/report")	
//	  	public String reportComment(@PathVariable int postId, @RequestParam("commentId") Long commentId, @RequestParam("commentContent") String commentContent) {
//	  		 
//	 		Optional<Comment> comment = commentRepository.findById(commentId);
//	 
//	 		if (comment.isPresent()) {
//	 			comment.get().setReportCount(comment.get().getReportCount() + 1);
//	 			commentRepository.save(comment.get());
//	 
//				if (comment.get().getReportCount() >= 5) {
//
//					commentRepository.deleteById( comment.get().getId());
//					Optional<ReportedComment> reportedComment = reportedCommentRepository.findById( comment.get().getId());
//					if (reportedComment.isPresent()) {
//				reportedComment.get().setDeleted(true);
//						reportedCommentRepository.save(reportedComment.get());
//					}
//				}
//				ReportedComment reportedComment = new ReportedComment(commentId, commentContent);			reportedCommentRepository.save(reportedComment);
//			}
//		    		return "redirect:/forum/post/{postId}";
//		}
// 

	@PostMapping("/delete-comment")
	public String deleteReportedComment(RedirectAttributes ar, @RequestParam("commentId") Long commentId) {
		Optional<ReportedComment> reportedComment = reportedCommentRepository.findById(commentId);

		if (reportedComment.isPresent()) {

			reportedComment.get().setDeleted(true);
			reportedCommentRepository.save(reportedComment.get());

			Long actualCommentId = reportedComment.get().getCommentId();
			commentRepository.deleteById(actualCommentId);
			ar.addFlashAttribute("Commentmessage","Comment is deleted");
		}

		return "redirect:/Admin/reported-entries";
	}

	@Transactional
	@PostMapping("/delete-post")
	public String deleteReportedPost(@RequestParam("postId") int postId, RedirectAttributes ar) {

		List<ReportedPost> reportedPosts = reportedPostRepository.findByPostId(postId);

		if (!reportedPosts.isEmpty()) {

			for (ReportedPost reportedPost : reportedPosts) {
				reportedPost.setDeleted(true);
				reportedPostRepository.save(reportedPost);
				ar.addFlashAttribute("message","Post is deleted");
			}
		}

		Optional<Post> postOptional = postRepository.findById(postId);
		if (postOptional.isPresent()) {
			Post post = postOptional.get();

			likeCRUDRepository.deleteByLikeIdPost(post);

			commentRepository.deleteByPost(post);

			postRepository.deleteById(postId);
		}

		return "redirect:/Admin/reported-entries";
	}

	@GetMapping("/analytics")
	public String getAnalytics(Model model, @AuthenticationPrincipal UserDetails userDetails) {

		String username = userDetails.getUsername();
		User user = userRepository.findByName(username).get();
		if (user != null) {

			int postCountToday = postRepository.CountPostsCreatedTodayBy(user.getId());
			int postCountMonth = postRepository.CountPostsCreatedMonthBy(user.getId());
			int postCountYear = postRepository.CountPostsCreatedYearBy(user.getId());
			int commentCountToday = commentRepository.CountCommentsCreatedTodayBy(user.getId());
			int commentCountYear = commentRepository.CountCommentsCreatedYearBy(user.getId());
			model.addAttribute("postCountToday", postCountToday);
			model.addAttribute("postCountMonth", postCountMonth);
			model.addAttribute("postCountYear", postCountYear);

			model.addAttribute("commentCountToday", commentCountToday);
			model.addAttribute("commentCountYear", commentCountYear);
		}
		return "analytics";
	}
	
	
	 @GetMapping("/login")
	    public String adminLoginPage() {
	        return "admin-login"; // Return the name of the HTML file for the admin login page
	    }

	//    @PostMapping("/delete-post")
	//   public String deleteReportedPost(@RequestParam("postId") int postId) {
	//
	//	      List<ReportedPost> reportedPosts = reportedPostRepository.findByPostId(postId);
	//
	//	      if (!reportedPosts.isEmpty()) {
	//	         
	//	          for (ReportedPost reportedPost : reportedPosts) {
	//	              reportedPost.setDeleted(true);
	//	              reportedPostRepository.save(reportedPost);
	//	          }
	//	      }
	//
	//	      Optional<Post> postOptional = postRepository.findById(postId);
	//	      if (postOptional.isPresent()) {
	//	          Post post = postOptional.get();
	//	          postRepository.deleteById(postId);
	//	      }
	//		  
	//		  return "redirect:/Admin/reported-entries";
	//		  
	//	  }
	//	  
	 
//	 
//	    @PostMapping("/post/{postId}/report")
//	    public String reportPost(@PathVariable int postId, Model model, @RequestParam("postContent") String postContent,@RequestParam("reportReason") String reportReason,
//	            @AuthenticationPrincipal UserDetails userDetails) {
//
//	        String username = userDetails.getUsername();
//	        User user = userRepository.findByName(username).orElse(null);
//
//	        if (user != null) {
//	          
//	            if (!reportedPostByUserRepository.existsByUserAndReportedPost_PostId(user, postId)) {
//	                Optional<Post> post = postRepository.findById(postId);
//	                if (post.isPresent()) {
//	                    post.get().setReportCount(post.get().getReportCount() + 1);
//	                    postRepository.save(post.get());
//	                    if (post.get().getReportCount() >= 5) {
//	                        postRepository.deleteById(post.get().getId());
//	                        Optional<ReportedPost> reportedPost = reportedPostRepository.findById(post.get().getId());
//	                        if (reportedPost.isPresent()) {
//	                            reportedPost.get().setDeleted(true);
//	                            reportedPostRepository.save(reportedPost.get());
//	                        }
//	                    }
//	                    ReportedPost reportedPost = new ReportedPost(postId, postContent,reportReason);
//	                    reportedPostRepository.save(reportedPost);
//
//	                    ReportedPostsByUser reportedPostByUser = new ReportedPostsByUser();
//	                    reportedPostByUser.setUser(user);
//	                    reportedPostByUser.setReportedPost(reportedPost);
//	                    reportedPostByUserRepository.save(reportedPostByUser);
//	                }
//	            }
//	        }
//	        return "redirect:/forum/post/{postId}";
//	    }
}
