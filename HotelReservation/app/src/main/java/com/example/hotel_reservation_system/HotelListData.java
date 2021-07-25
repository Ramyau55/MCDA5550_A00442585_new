package com.example.hotel_reservation_system;

public class HotelListData {

    String hotel_name;
    String price;
    String availability;

    public HotelListData(String hotel_name, String price, String availability) {
        this.hotel_name = hotel_name;
        this.price = price;
        this.availability = availability;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public String getPrice() {
        return price;
    }

    public String getAvailability() {
        return availability;
    }
}
