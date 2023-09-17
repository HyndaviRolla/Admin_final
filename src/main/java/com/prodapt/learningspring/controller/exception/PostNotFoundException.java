package com.prodapt.learningspring.controller.exception;
 
public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) {
        super(message);
    }
}