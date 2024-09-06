package com.example.demo.EventListeners;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.demo.model.Courses;
import com.example.demo.model.User_model;
import com.example.demo.model.UserCourseProgress;
import com.example.demo.repository.Courses_Repository;
import com.example.demo.repository.User_course_progres_repository;

import jakarta.transaction.Transactional;

@Component
public class UpdateProgressOnUserCreation {
    private UserCourseProgress user_course_progress;
    private Courses_Repository courses_Repository;
    private User_course_progres_repository user_course_progres_repository;
    private User_Creation_Event user_Creation_Event;

    public UpdateProgressOnUserCreation(Courses_Repository courses_Repository,
            User_course_progres_repository user_course_progres_repository) {
        this.courses_Repository = courses_Repository;
        this.user_course_progres_repository = user_course_progres_repository;
    }

    @EventListener
    @Transactional
    public void courseProgressOnUserSignUP(UpdateProgressOnUserCreation updateProgressOnUserCreation) {
        User_model newUser = user_Creation_Event.getUser();
        List<Courses> Allcourses = courses_Repository.findAll();

        for (Courses course :Allcourses ){
user_course_progress.setUsers(newUser);
user_course_progress.setCourses(course);
user_course_progress.setHasCompletedCourse(false);
 user_course_progres_repository.save(user_course_progress);

        }
    }

}