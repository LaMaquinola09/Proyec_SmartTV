package com.example.myappsmart_tv.repository
import android.util.Log
import com.example.myappsmart_tv.model.MovieResponse
import com.example.myappsmart_tv.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository {
    private val api = RetrofitInstance.api

    fun getPopularMovies(apiKey: String, onResult: (MovieResponse?) -> Unit) {
        api.getPopularMovies(apiKey).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    Log.e("MovieRepository", "Error: ${response.code()} ${response.message()}")
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e("MovieRepository", "Failure: ${t.message}")
                onResult(null)
            }
        })
    }
}
