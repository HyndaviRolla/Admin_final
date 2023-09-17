package com.prodapt.learningspring.controller.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    public String handlePostNotFoundException(Model model, PostNotFoundException ex) {
        model.addAttribute("message", "Post is deleted");
        return "forum/postDetail"; 
    }
}
