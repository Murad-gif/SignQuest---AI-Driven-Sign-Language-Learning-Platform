package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User_model;


@Repository
public interface User_repository extends JpaRepository<User_model, Integer> {
    @Query("SELECT u FROM User_model u WHERE u.email = ?1")
    public User_model findByEmail(String email);
}