package com.example.demo.model;

import java.io.Serializable;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "user_course_progress")
@EntityListeners(AuditingEntityListener.class)

public class UserCourseProgress implements Serializable {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="progressId")
	Integer progressId;

    
	@Column(name="hasCompletedCourse")
	boolean hasCompletedCourse;


	@Column(name="coursePercentage")
	Integer coursePercentage;

    @ManyToOne( fetch = FetchType.EAGER)
	@JoinColumn(name = "courses")
	@JsonIgnore
	private Courses courses;



    @ManyToOne( fetch = FetchType.EAGER)
	@JoinColumn(name = "users")
	@JsonIgnore
	private User_model users;


    
    public UserCourseProgress(boolean hasCompletedCourse, User_model users, Courses courses,Integer coursePercentage) {
        this.hasCompletedCourse = hasCompletedCourse;
        this.users = users;
        this.courses = courses;
		this.coursePercentage = coursePercentage;
    }



}
