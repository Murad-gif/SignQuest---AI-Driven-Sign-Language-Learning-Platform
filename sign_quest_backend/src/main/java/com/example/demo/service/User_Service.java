package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import com.example.demo.repository.Courses_Repository;
import com.example.demo.repository.ResetToken_repository;
import com.example.demo.repository.User_course_progres_repository;
import com.example.demo.repository.User_repository;
import jakarta.transaction.Transactional;
import org.springframework.mail.MailSender;
import com.example.demo.dto.progressDto;
import com.example.demo.model.Courses;
import com.example.demo.model.ResetPassword;
import com.example.demo.model.User_model;
import com.example.demo.model.UserCourseProgress;
import org.springframework.mail.javamail.JavaMailSender;



@Service
public class User_Service  {

  @Autowired
  private User_repository user_repository;

  @Autowired
  private Courses_Repository courses_Repository;

  @Autowired
  private User_course_progres_repository user_course_progres_repository;
  @Autowired

  private ApplicationEventPublisher eventPublisher;

	@Autowired

   JavaMailSender mailSender;


  @Autowired
ResetToken_repository resetToken_repository;





  @Autowired
  public User_Service(User_repository user_repository, Courses_Repository courses_Repository,
      User_course_progres_repository user_course_progres_repository,
      ApplicationEventPublisher eventPublisher) {
    this.user_repository = user_repository;
    this.courses_Repository = courses_Repository;
    this.user_course_progres_repository = user_course_progres_repository;
    this.eventPublisher = eventPublisher;
  }

  public void addUser(User_model newUser) {
    User_model savedUser = user_repository.save(newUser);

    List<Courses> allCourses = courses_Repository.findAll();
    for (Courses course : allCourses) {
      System.out.println(course);
      UserCourseProgress user_course_progress = new UserCourseProgress();
      user_course_progress.setUsers(savedUser);
      user_course_progress.setCourses(course);
      user_course_progress.setHasCompletedCourse(false);

      user_course_progres_repository.save(user_course_progress);
    }
  }

public String sendResetEmail(User_model user ) {

  try {
    String resetLink = generateResetPassLink(user);

    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom("deliveroo3212@gmail.com");
    msg.setTo(user.getEmail());

    msg.setSubject("This is your reset password email");
    msg.setText("Hello \n\n" + "change your password here  :" + resetLink + " . \n\n"
        + "Kind Regards \n" + "SignQuest Team");

    mailSender.send(msg);

    return user.getEmail();
  } catch (Exception e) {
    e.printStackTrace();
    return user.getEmail();
  }}





  
  public String generateResetPassLink(User_model user) {
		UUID uuid = UUID.randomUUID();
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime expiryDateTime = currentDateTime.plusMinutes(30);
		ResetPassword resetPassword = new ResetPassword();
		resetPassword.setUser(user);
		resetPassword.setToken(uuid.toString());
		resetPassword.setExpiryDate(expiryDateTime);
		ResetPassword token = resetToken_repository.save(resetPassword);
		if (token != null) {
			String endpointUrl = "http://localhost:3000/changePassword";
			return endpointUrl + "/" + resetPassword.getToken();
		}
		return "";
	}


	public boolean hasExipred(LocalDateTime expiryDateTime) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		return expiryDateTime.isAfter(currentDateTime);
	}

}