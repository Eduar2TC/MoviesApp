package com.example.moviesapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class MainViewModel : ViewModel(){

    //creation observable state
    //Live data and StateFlow
    private val _state = MutableStateFlow(ViewState())
    val viewState : StateFlow<ViewState> = _state
    init {
        //init state
        viewModelScope.launch{
            _state.value = ViewState(isLoading = true)
            //force delay
            delay(2000)
            _state.value = ViewState(
                isLoading = false,
                movies = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MoviesService::class.java)
                .getMovies()
                .results
            )
        }
    }
    fun onMovieClick(movie: MovieResponseResult)  {
        val movies = _state.value.movies.toMutableList()
        movies.replaceAll(){
            if( it.id == movie.id ){
                movie.copy(is_favorite = !movie.is_favorite)

            }else{
                it
            }
        }
        _state.value = _state.value.copy(movies = movies) // update state
    }
    //status of view
    data class ViewState(
        val movies: List<MovieResponseResult> = emptyList(),
        val isLoading: Boolean = false,
        val error: String = ""
    )
}