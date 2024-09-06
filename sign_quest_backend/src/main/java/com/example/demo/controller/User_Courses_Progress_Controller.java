package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.progressDto;
import com.example.demo.model.Courses;
import com.example.demo.model.UserCourseProgress;
import com.example.demo.model.User_model;
import com.example.demo.repository.Courses_Repository;
import com.example.demo.repository.User_course_progres_repository;
import com.example.demo.repository.User_repository;
import com.example.demo.service.User_Course_Progress_Service;

@RestController
public class User_Courses_Progress_Controller {
  @Autowired
  User_Course_Progress_Service user_Course_Progress_Service;
  @Autowired

  User_course_progres_repository user_course_progres_repository;
  @Autowired
  Courses_Repository courses_Repository;

  @Autowired
  User_repository user_repository;

  @RequestMapping("/getCoursesSuggestion/{email}")
  public Iterable<String> getAllReview(@PathVariable("email") String email) {

    return user_course_progres_repository.findDistinctCourseNamesByUserEmailAndCompletionStatus(email, false);

  }

  @RequestMapping("/tesetApi")
  public List<UserCourseProgress> getCourses() {
    return user_course_progres_repository.findAll();

  }

  Logger logger = LoggerFactory.getLogger(User_Courses_Progress_Controller.class);

  @PostMapping("/progress")
  public ResponseEntity<Optional<UserCourseProgress>> addReview(@RequestBody progressDto progressDto) {

    Courses courses = courses_Repository.findByCourseID(progressDto.getCourseId());

    User_model user = user_repository.findByEmail(progressDto.getEmail());

    UserCourseProgress userCourseProgress = new UserCourseProgress(progressDto.isHasCompletedCourse(),
        user, courses, progressDto.getCoursePercentage());
    logger.info("User: {}", user);

    user_course_progres_repository.save(userCourseProgress);

    return new ResponseEntity<>(Optional.ofNullable(userCourseProgress), HttpStatus.CREATED);

  }

  @PostMapping("/progressandUpdate")
  public ResponseEntity<UserCourseProgress> addOrUpdateProgress(@RequestBody progressDto progressDto) {
    Courses courses = courses_Repository.findByCourseID(progressDto.getCourseId());
    User_model user = user_repository.findByEmail(progressDto.getEmail());

    Optional<UserCourseProgress> progress = user_course_progres_repository.findByUserAndCourse(user, courses);
    logger.info("Updated user course progress: {}", progress);

    UserCourseProgress userCourseProgress = progress.get();
    userCourseProgress.setHasCompletedCourse(progressDto.isHasCompletedCourse());
    userCourseProgress.setCoursePercentage(progressDto.getCoursePercentage());
    logger.info("Updated user course progress: {}", userCourseProgress);
    user_course_progres_repository.save(userCourseProgress);
    return new ResponseEntity<>(userCourseProgress, HttpStatus.OK);

  }

}
