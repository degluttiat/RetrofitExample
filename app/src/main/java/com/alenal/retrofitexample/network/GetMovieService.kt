package com.alenal.retrofitexample.network

import com.alenal.retrofitexample.model.MovieRetrofit
import retrofit2.Call
import retrofit2.http.GET

interface GetMovieService {
    @GET("/3/movie/550?api_key=c599629553579897d7941c4dbb0e7940")
    fun getMovie(): Call<MovieRetrofit.Movie>
}