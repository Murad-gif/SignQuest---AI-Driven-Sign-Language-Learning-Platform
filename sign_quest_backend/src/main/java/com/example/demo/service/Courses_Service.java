package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Courses;
import com.example.demo.repository.Courses_Repository;

@Service

public class Courses_Service {
    	@Autowired

    Courses_Repository courses_Repository;
    public List<Courses> getAllAnswers() {
      return courses_Repository.findAll();
    }
}
