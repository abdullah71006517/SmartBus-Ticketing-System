package com.example.demo1.Model;




public class UserData {
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String userType;

    public UserData(String username, String fullName, String phone, String email, String userType) {
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.userType = userType;
    }

    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getUserType() { return userType; }
}
