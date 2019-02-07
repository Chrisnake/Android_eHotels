package com.example.android.ehotelsapp;

public class UserDetails
{
    private String userID;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userQRID;


    public UserDetails()
    {

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserQRid()
    {
        return userQRID;
    }

    public void setUserQRid(String userQRid)
    {
        this.userQRID = userQRid;
    }
}
