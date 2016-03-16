package com.example.dmitry.androidrestclient;

import com.example.dmitry.androidrestclient.data.Credit;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by dmitry on 14.02.16.
 */
public interface RestService {
    @GET("credit")
    public Call<List<Credit>> getCredits();

    @GET("credit/{year}")
    public Call<List<Credit>> getCredits(@Path("year") int year);

    @GET("credit/{year}/{month}")
    public Call<List<Credit>> getCredits(@Path("year") int year, @Path("month") int month);

    @GET("credit/{year}/{month}/{day}")
    public Call<List<Credit>> getCredits(@Path("year") int year,
                                         @Path("month") int month,
                                         @Path("day") int day);

    @POST("credit")
    public Call<Void> saveCredit(@Body Credit credit);

    @DELETE("credit/{id}")
    public Call<Void> deleteCredit(@Path("id") String id);
}
