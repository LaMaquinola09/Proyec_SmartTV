package com.example.myappsmart_tv.model

data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val release_date: String,
    val poster_path: String,
    val backdrop_path: String,
    val vote_average: Double,
    val popularity: Double
)
