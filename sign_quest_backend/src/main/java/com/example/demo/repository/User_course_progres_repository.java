package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Courses;
import com.example.demo.model.UserCourseProgress;
import com.example.demo.model.User_model;


@Repository
public interface User_course_progres_repository extends JpaRepository<UserCourseProgress, Integer> {
	      List<Courses> findCoursesByUsers_EmailAndHasCompletedCourse(String Email, boolean hasCompletedCourse);


 @Query("SELECT DISTINCT uc.courses.courseName FROM UserCourseProgress uc WHERE uc.users.email = :email AND uc.hasCompletedCourse = :hasCompletedCourse")
    List<String> findDistinctCourseNamesByUserEmailAndCompletionStatus(@Param("email") String email, @Param("hasCompletedCourse") boolean hasCompletedCourse);


    @Query("SELECT ucp FROM UserCourseProgress ucp WHERE ucp.users = :users AND ucp.courses = :courses")
    Optional<UserCourseProgress> findByUserAndCourse(@Param("users") User_model user, @Param("courses") Courses courses);

}