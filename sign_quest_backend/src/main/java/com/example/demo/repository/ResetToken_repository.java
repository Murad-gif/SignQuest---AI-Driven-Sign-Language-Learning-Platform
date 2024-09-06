package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ResetPassword;

@Repository
public interface ResetToken_repository extends JpaRepository <ResetPassword, Integer> {
 ResetPassword findByToken(String token);
}
