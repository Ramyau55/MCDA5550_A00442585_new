package com.example.hotel_reservation_system;

import retrofit.RestAdapter;

public class Api {

    public static ApiInterface getClient() {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://hotelreservationonaws-env.eba-tifqwmf2.ca-central-1.elasticbeanstalk.com/") //Set the Root URL
                .build();

        ApiInterface api = adapter.create(ApiInterface.class);
        return api;
    }
}
