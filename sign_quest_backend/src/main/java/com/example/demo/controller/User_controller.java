package com.example.demo.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.Courses_Service;
import com.example.demo.service.User_Service;

import jakarta.servlet.Servlet;

import com.example.demo.dto.UserDto;
import com.example.demo.model.Courses;
import com.example.demo.model.ResetPassword;
import com.example.demo.model.UserCourseProgress;
import com.example.demo.model.User_model;
import com.example.demo.model.userRoles;
import com.example.demo.repository.Courses_Repository;
import com.example.demo.repository.ResetToken_repository;
import com.example.demo.repository.User_repository;
import com.example.demo.repository.user_Roles_Repository;
import com.example.demo.security.AuthenticationDto;
import com.example.demo.security.generateJWT;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController

public class User_controller {

    @Autowired
    User_Service userService;
    @Autowired

    Courses_Service courses;
    @Autowired

    user_Roles_Repository roles_Repository;
    @Autowired

    generateJWT generateJWT;

    @Autowired

    ResetToken_repository resetToken_repository;

    @Autowired

    private AuthenticationManager authenticationManager;
    @Autowired

    private User_repository userRepository;

    

    @PostMapping("/addUser")
    public ResponseEntity<Optional<User_model>> addUser(@RequestBody UserDto userDto) {
        if (userDto.getEmail() == null ||
                userDto.getPassword() == null) {

            System.out.println(userDto.toString());
            return new ResponseEntity<>(Optional.ofNullable(null), HttpStatus.BAD_REQUEST);

        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        logger.info("Email received: {}", userDto);

        userRoles roles = roles_Repository.findByName("USER").get();

        User_model newUser = new User_model();
        newUser.setName(userDto.getName());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(encoder.encode(userDto.getPassword()));
        newUser.setUserroles(Collections.singletonList(roles));
        userService.addUser(newUser);
           
        return new ResponseEntity<>(Optional.ofNullable(newUser), HttpStatus.CREATED);

    }





    @RequestMapping("/profile/{email}")
    public User_model getProfile(@PathVariable("email") String email) {
        return userRepository.findByEmail(email);

    }

    @RequestMapping("/listOfroles")
    public List<userRoles> getCourses() {
        return roles_Repository.findAll();

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDto> login(@RequestBody UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getEmail(),
                        userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = generateJWT.token(authentication);
        return new ResponseEntity<>(new AuthenticationDto(token), HttpStatus.OK);
    }

    @PostMapping("/forgotPassword")
    public String checkForgotPassword(@RequestBody UserDto userDto) {
        User_model user = userRepository.findByEmail(userDto.getEmail());
        String output = "";
        if (user != null) {
            output = userService.sendResetEmail(user);
        }

        return "Reset Email has been sent";

    }

    @GetMapping("/resetPassword/{token}")
    public String resetPasswordForm(@PathVariable("token") String token, Model model) {
        ResetPassword reset = resetToken_repository.findByToken(token);
        if (reset != null && userService.hasExipred(reset.getExpiryDate())) {
            model.addAttribute("email", reset.getUser().getEmail());
            return reset.getUser().getEmail();
        }
        return "link expired";
    }

    Logger logger = LoggerFactory.getLogger(User_controller.class);

    @PostMapping("/resetPassword")
    public String passwordResetProcess(@RequestBody UserDto userDto) {
        User_model user = userRepository.findByEmail(userDto.getEmail());
        logger.info("User: {}", userDto);

        if (user != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            user.setPassword(encoder.encode(userDto.getPassword()));
            userRepository.save(user);
            return "password changed";
        } else {
            return "Error";

        }
    }

    @PutMapping("/updateUser/{email}")
    public ResponseEntity<Optional<User_model>> updateReview(@RequestBody UserDto userDto,
            @PathVariable("email") String email) {

        User_model user = userRepository.findByEmail(email);
        user.setName(userDto.getName());
        user.setEmail(email);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        user.setPassword(encoder.encode(userDto.getPassword()));

        userRepository.save(user);

        return new ResponseEntity<>(Optional.ofNullable(user), HttpStatus.CREATED);

    }

}
