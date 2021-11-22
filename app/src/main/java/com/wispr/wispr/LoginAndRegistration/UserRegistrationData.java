package com.wispr.wispr.LoginAndRegistration;

public class UserRegistrationData {

    private String username;
    private String displayName;
    private String faculty;
    private String department;
    private String school;
    private String password;

    public UserRegistrationData(String username, String displayName) {
        this.username = username;
        this.displayName = displayName;
    }

    public UserRegistrationData(String username, String displayName, String faculty, String department, String school, String password) {
        this.username = username;
        this.displayName = displayName;
        this.faculty = faculty;
        this.department = department;
        this.school = school;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
