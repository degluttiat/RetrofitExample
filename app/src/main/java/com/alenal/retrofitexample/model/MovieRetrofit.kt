package com.alenal.retrofitexample.model

import com.google.gson.annotations.SerializedName

object MovieRetrofit {
    data class Movie(
        val title: String,
        val genres: List<Genre>,
        @SerializedName("poster_path") val image: String
    )

    data class Genre(val name: String)
}