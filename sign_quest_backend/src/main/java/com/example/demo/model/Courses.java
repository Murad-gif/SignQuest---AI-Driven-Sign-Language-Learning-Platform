package com.example.demo.model;


import java.io.Serializable;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="coursesSign")
@EntityListeners(AuditingEntityListener.class)
public class Courses implements Serializable {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "courseid")
	Integer courseID;

    
    @Column(name="course_name")
	String courseName;
}


