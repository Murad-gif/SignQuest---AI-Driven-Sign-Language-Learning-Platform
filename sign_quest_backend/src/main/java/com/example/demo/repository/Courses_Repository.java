package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Courses;

@Repository
public  interface Courses_Repository extends JpaRepository <Courses, Integer>{
    Courses findByCourseID(Integer courseID);

}
