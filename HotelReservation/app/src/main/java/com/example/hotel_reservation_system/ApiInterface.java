package com.example.hotel_reservation_system;

import java.util.List;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.mime.TypedInput;

public interface ApiInterface {

    @GET("/getListOfHotels")
    public void getHotelsList(Callback<List<HotelListData>> callback);

    @POST("/reserveHotel")
    public void reserveHotel(@Body TypedInput body, Callback<Confirmation> callback);
}
