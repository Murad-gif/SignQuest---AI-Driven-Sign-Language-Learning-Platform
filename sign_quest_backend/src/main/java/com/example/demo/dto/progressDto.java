package com.example.demo.dto;

public class progressDto {
  boolean hasCompletedCourse;
  int courseId;
  String email;
  int coursePercentage;



public progressDto(boolean hasCompletedCourse, int courseId, String email,int coursePercentage) {
    super();

    this.hasCompletedCourse = hasCompletedCourse;
    this.courseId = courseId;
    this.email = email;
    this.coursePercentage=coursePercentage;
}
public boolean isHasCompletedCourse() {
    return hasCompletedCourse;
}
public void setHasCompletedCourse(boolean hasCompletedCourse) {
    this.hasCompletedCourse = hasCompletedCourse;
}
public int getCourseId() {
    return courseId;
}
public void setCourseId(int courseId) {
    this.courseId = courseId;
}

public int getCoursePercentage() {
    return coursePercentage;
}
public void setCoursePercentage(int coursePercentage) {
    this.coursePercentage = coursePercentage;
}
public String getEmail() {
    return email;
}
public void setEmail(String email) {
    this.email = email;
}
@Override
public String toString() {
    return "progressDto [hasCompletedCourse=" + hasCompletedCourse + ", courseId=" + courseId + ", email=" + email
            + ", coursePercentage=" + coursePercentage + "]";
}






}
