package com.example.moviesapp

import retrofit2.http.GET

interface MoviesService {
    @GET("discover/movie?api_key=3f4ca4f3a9750da53450646ced312397")
    suspend fun getMovies(): MovieResult
}