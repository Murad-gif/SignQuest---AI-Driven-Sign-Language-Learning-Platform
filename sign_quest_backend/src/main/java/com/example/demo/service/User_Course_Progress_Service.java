package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.User_course_progres_repository;
import com.example.demo.model.Courses;
import com.example.demo.model.UserCourseProgress;

@Service
public class User_Course_Progress_Service {
  @Autowired
  User_course_progres_repository user_course_progres_repository;

  public List<Courses> getCourseSugegstions(String email, boolean hasCompletedCourse) {
    return (List<Courses>) user_course_progres_repository.findCoursesByUsers_EmailAndHasCompletedCourse(email,
        hasCompletedCourse);

  }

}
