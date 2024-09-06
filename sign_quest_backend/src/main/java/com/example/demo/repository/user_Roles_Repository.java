package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Courses;
import com.example.demo.model.UserCourseProgress;
import com.example.demo.model.userRoles;

@Repository
public interface user_Roles_Repository extends JpaRepository<userRoles, Integer> {
    Optional<userRoles> findByName(String name);
    	       List<userRoles> findAll();


}
