package com.example.demo.EventListeners;

import org.springframework.context.ApplicationEvent;

import com.example.demo.model.User_model;

public class User_Creation_Event extends ApplicationEvent{
    private final User_model user;
    public User_Creation_Event(Object source, User_model user) {
        super(source);
        //TODO Auto-generated constructor stub
        this.user = user;
    }

    public User_model getUser(){
        return user;
    }
    
}