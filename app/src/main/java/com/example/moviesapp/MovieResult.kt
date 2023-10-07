package com.example.moviesapp

data class MovieResult(
    val page: Int,
    val results: List<MovieResponseResult>,
    val total_pages: Int,
    val total_results: Int
)