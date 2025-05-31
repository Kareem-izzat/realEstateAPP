package com.example.realestate.models;


public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String gender;

    private String phone;

    // Constructor
    public User(String email, String firstName, String lastName, String password,
                String gender, String phone) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.gender = gender;

        this.phone = phone;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }



    public String getPhone() {
        return phone;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



    public void setPhone(String phone) {
        this.phone = phone;
    }

    // For debugging or profile display
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + getFullName() + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
