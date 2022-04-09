package com.example.new_firebase;

public class User {
    private String uid,userName,rollNumber,email;
    private int userType;
    public User()
    {

    }
    public User(String uid,String userName, String rollNumber, String email,int userType) {
        this.uid = uid;
        this.userName = userName;
        this.rollNumber = rollNumber;
        this.email = email;
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
