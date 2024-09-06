package com.example.demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)

public class User_model implements Serializable

{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)

    private List<UserCourseProgress> user_Course_Progresses = new ArrayList<>();
    @JsonIgnore

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ResetPassword> resetPassword;

    public void addUserCourse(UserCourseProgress user_Course_Progress) {
        user_Course_Progresses.add(user_Course_Progress);
        user_Course_Progress.setUsers(this);
    }

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<userRoles> userroles = new ArrayList<>();

    public User_model(String email, String password, String name, List<userRoles> userroles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.userroles = userroles;
    }

    public User_model(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User_model orElseThrow(Object object) {
        return null;
    }

}