package com.example.android.ehotelsapp;

public class UserBookings
{
    private String userID;
    private String hotel;
    private String price;
    private String dateIn;
    private String dateOut;
    private String roomType;
    private String idQR;
    private String userRoomNumber;

    public UserBookings(String id, String userHotel, String userPrice, String userIn, String userOut, String userRoom, String userQR, String userNumber)
    {
        userID = id;
        hotel = userHotel;
        price = userPrice;
        dateIn = userIn;
        dateOut = userOut;
        roomType = userRoom;
        idQR = userQR;
        userRoomNumber = userNumber;

    }
    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDateIn() {
        return dateIn;
    }

    public void setDateIn(String dateIn) {
        this.dateIn = dateIn;
    }

    public String getDateOut() {
        return dateOut;
    }

    public void setDateOut(String dateOut) {
        this.dateOut = dateOut;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getIdQR() {
        return idQR;
    }

    public void setIdQR(String idQR) {
        this.idQR = idQR;
    }

    public String getUserRoomNumber()
    {
        return userRoomNumber;
    }

    public void setUserRoomNumber(String userRoomNumber)
    {
        this.userRoomNumber = userRoomNumber;
    }

}
