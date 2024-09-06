package com.example.demo.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resetPasword")

public class ResetPassword {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;
    @JsonIgnore

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user", referencedColumnName = "id")
    private User_model user;

    private LocalDateTime expiryDate;
}