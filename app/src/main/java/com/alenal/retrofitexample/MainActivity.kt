package com.alenal.retrofitexample

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alenal.retrofitexample.model.MovieRetrofit
import com.alenal.retrofitexample.model.MovieSimple
import com.alenal.retrofitexample.network.GetMovieService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val baseUrl = "https://api.themoviedb.org"
    val baseImageUrl="https://image.tmdb.org/t/p/w500/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSimple.setOnClickListener(this)
        btnRetrofitGson.setOnClickListener(this)

/*        btnRetrofitGson.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

            }
        })*/
    }

    override fun onClick(v: View?) {
        when (v) {
            btnSimple -> onBtnSimpleClick()
            btnRetrofitGson -> obBtnRetrofitGsonClick()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun obBtnRetrofitGsonClick() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val getMovieService = retrofit.create(GetMovieService::class.java)

        val callForMovie = getMovieService.getMovie()

        callForMovie.enqueue(object : Callback<MovieRetrofit.Movie> {
            override fun onFailure(call: Call<MovieRetrofit.Movie>, t: Throwable) {}

            override fun onResponse(call: Call<MovieRetrofit.Movie>, response: Response<MovieRetrofit.Movie>) {
                val movie = response.body()
                if (movie == null) return
                textView.text = "${movie.title} - ${movie.genres[0].name}"
                Picasso.get()
                    .load("$baseImageUrl${movie.image}")
                    .into(imageOfMovie)
            }
        })



/*        val movie = MovieRetrofit.Movie("Put here a title", listOf(), "")
        val firstGenreName = movie.genres[0].name*/
    }

    @SuppressLint("SetTextI18n")
    private fun onBtnSimpleClick() {
        val url = URL(getString(R.string.url))
        thread {
            val urlConnection = url.openConnection()
            val jsonString = urlConnection.inputStream.bufferedReader().readText()
            /* runOnUiThread {
                    textView.text = jsonString
               }*/
            val movie = convertJsonStringToMovieObject(jsonString)
            runOnUiThread {
                textView.text = "${movie.title} - ${movie.genre}"
            }
        }
    }

    private fun convertJsonStringToMovieObject(jsonString: String): MovieSimple {
        val json = JSONObject(jsonString)

        // Get title
        val nameOfJsonField = "title"
        val title = json[nameOfJsonField].toString()

        // Get Genres
        val jsonArrayOfGenres = json.getJSONArray("genres")
        val firstGenreObject = jsonArrayOfGenres.getJSONObject(0)
        val firstGenreName = firstGenreObject["name"].toString()
        return MovieSimple(title, firstGenreName)
    }
}


