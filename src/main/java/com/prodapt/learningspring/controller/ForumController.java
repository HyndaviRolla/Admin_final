package com.prodapt.learningspring.controller;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.prodapt.learningspring.entity.Comment;
 
import com.prodapt.learningspring.controller.binding.AddCommentForm;
import com.prodapt.learningspring.controller.binding.AddPostForm;
import com.prodapt.learningspring.controller.exception.PostNotFoundException;
import com.prodapt.learningspring.controller.exception.ResourceNotFoundException;
import com.prodapt.learningspring.entity.LikeRecord;
import com.prodapt.learningspring.entity.LikeId;
import com.prodapt.learningspring.entity.Post;
import com.prodapt.learningspring.entity.ReportedComment;
import com.prodapt.learningspring.entity.ReportedPost;
import com.prodapt.learningspring.entity.User;
import com.prodapt.learningspring.model.RegistrationForm;
 
import com.prodapt.learningspring.repository.CommentRepository;
import com.prodapt.learningspring.repository.LikeCRUDRepository;
import com.prodapt.learningspring.repository.LikeCountRepository;
import com.prodapt.learningspring.repository.PostRepository;
import com.prodapt.learningspring.repository.ReportedCommentRepository;
import com.prodapt.learningspring.repository.ReportedPostRepository;
import com.prodapt.learningspring.repository.UserRepository;
import com.prodapt.learningspring.service.DomainUserService;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;

@Controller
@RequestMapping("/forum")
public class ForumController {
  
  @Autowired
  private UserRepository userRepository;
 
  
  @Autowired
  private ReportedPostRepository reportedPostRepository;
  
  @Autowired
  private CommentRepository commentRepository;
  
  @Autowired
  private PostRepository postRepository;
  
  @Autowired
  private DomainUserService domainUserService;
  
  
  @Autowired
  private ReportedCommentRepository reportedCommentRepository;

 
  @Autowired
  private LikeCRUDRepository likeCRUDRepository;
  private List<User> userList;
  
  @PostConstruct
  public void init() {
    userList = new ArrayList<>();
  }
  
  @GetMapping("/post/form")
  public String getPostForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
    AddPostForm postForm = new AddPostForm();
    User author = domainUserService.getByName(userDetails.getUsername()).get();
    postForm.setUserId(author.getId());
    model.addAttribute("postForm", postForm);
    return "forum/postForm";
  }
  
  @PostMapping("/post/add")
  public String addNewPost(@ModelAttribute("postForm") AddPostForm postForm, BindingResult bindingResult, RedirectAttributes attr) throws ServletException {
    if (bindingResult.hasErrors()) {
      System.out.println(bindingResult.getFieldErrors());
      attr.addFlashAttribute("org.springframework.validation.BindingResult.post", bindingResult);
      attr.addFlashAttribute("post", postForm);
      return "redirect:/forum/post/form";
    }
    Optional<User> user = userRepository.findById(postForm.getUserId());
    if (user.isEmpty()) {
      throw new ServletException("Something went seriously wrong and we couldn't find the user in the DB");
    }
    Post post = new Post();
    post.setAuthor(user.get());
    post.setContent(postForm.getContent());
    postRepository.save(post);
    
    return String.format("redirect:/forum/post/%d", post.getId());
  }
 
  @GetMapping("/post/{id}")
  public String postDetail(@PathVariable int id, Model model, @AuthenticationPrincipal UserDetails userDetails,RedirectAttributes ar) {
      Optional<Post> postOptional = postRepository.findById(id);

      if (postOptional.isPresent()) {
          Post post = postOptional.get();
          model.addAttribute("post", post);
          model.addAttribute("likerName", userDetails.getUsername());
          int numLikes = likeCRUDRepository.countByLikeIdPost(post);
          model.addAttribute("likeCount", numLikes);
          return "forum/postDetail";
      }  
      else {
     
       return "redirect:/forum/post/form";   
      }
  }

 
  @PostMapping("/post/{id}/like")
  public String postLike(@PathVariable int id, String likerName, RedirectAttributes attr) {
    LikeId likeId = new LikeId();
    likeId.setUser(userRepository.findByName(likerName).get());
    likeId.setPost(postRepository.findById(id).get());
    LikeRecord like = new LikeRecord();
    like.setLikeId(likeId);
    likeCRUDRepository.save(like);
    return String.format("redirect:/forum/post/%d", id);
  }
  @GetMapping("/post/{postId}/comment/addForm")  
  public String getCommentForm(@PathVariable int postId, Model model) {
     
      Optional<Post> post = postRepository.findById(postId);
      if (post.isEmpty()) {
          return "redirect:/forum";
      }

      model.addAttribute("post", post.get());
      model.addAttribute("comment", new Comment());
      return "forum/postDetail";
  }
  
  @PostMapping("/post/{postId}/comment/add")
  public String addCommentToPost(@ModelAttribute("commentForm") AddCommentForm commentForm,
                                 @PathVariable("postId") int postId,
                                 BindingResult bindingResult,
                                 RedirectAttributes attr,
                                 @AuthenticationPrincipal UserDetails userDetails) throws ResourceNotFoundException, ServletException {
    if (bindingResult.hasErrors()) {
      attr.addFlashAttribute("org.springframework.validation.BindingResult.commentForm", bindingResult);
      attr.addFlashAttribute("commentForm", commentForm);
      return String.format("redirect:/forum/post/%d", postId);
    }

    Optional<Post> post = postRepository.findById(postId);
    if (post.isEmpty()) {
      throw new ResourceNotFoundException("No post with the requested ID");
    }

    Optional<User> user = userRepository.findByName(userDetails.getUsername());
    if (user.isEmpty()) {
      throw new ServletException("Something went seriously wrong and we couldn't find the user in the DB");
    }

    Comment comment = new Comment();
    comment.setPost(post.get());
    comment.setUser(user.get());
    comment.setContent(commentForm.getContent());
  comment.setDate(new Date());
  
    commentRepository.save(comment);

    return String.format("redirect:/forum/post/%d", postId);
  }

  @GetMapping("/register")
  public String getRegistrationForm(Model model) {
    if (!model.containsAttribute("registrationForm")) {
      model.addAttribute("registrationForm", new RegistrationForm());
    }
    return "forum/register";
  }
  @PostMapping("/register")
  public String register(@ModelAttribute("registrationForm") RegistrationForm registrationForm, 
  BindingResult bindingResult, 
  RedirectAttributes attr) {
    if (bindingResult.hasErrors()) {
      attr.addFlashAttribute("org.springframework.validation.BindingResult.registrationForm", bindingResult);
      attr.addFlashAttribute("registrationForm", registrationForm);
      return "redirect:/register";
    }
    if (!registrationForm.isValid()) {
      attr.addFlashAttribute("message", "Passwords must match");
      attr.addFlashAttribute("registrationForm", registrationForm);
      return "redirect:/register";
    }
    System.out.println(domainUserService.save(registrationForm.getUsername(), registrationForm.getPassword(),registrationForm.getRole()));
    attr.addFlashAttribute("result", "Registration success!");
    return "redirect:/login";
  }
 
}
