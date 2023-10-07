package com.example.moviesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.moviesapp.ui.theme.MoviesAppTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//MVVM - Model View ViewModel
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesAppTheme {
                val viewModel: MainViewModel = viewModel()
                val state by viewModel.viewState.collectAsState()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        //top bar
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                    text = "Movies" , textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
                                )
                                },
                            )
                        }
                    ) { padding ->
                        //loading data
                        if( state.isLoading ){
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Loading...",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                CircularProgressIndicator()
                            }

                        }
                        //list is not empty
                        if ( state.movies.isNotEmpty() ){
                            LazyVerticalGrid(
                                modifier = Modifier.padding(padding), //padding from scaffold
                                columns = GridCells.Adaptive(120.dp), //default size of cell
                                verticalArrangement = Arrangement.spacedBy(8.dp), //space between cells
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(8.dp),
                                content = {
                                    items( state.movies ) { movie ->
                                        MovieItem(
                                            movie = movie,
                                            onClick = { viewModel.onMovieClick(movie) }
                                        )
                                    }
                                },
                            )
                        }

                    }
                }
            }
        }
    }
@Composable
    private fun MovieItem(movie: MovieResponseResult, onClick: () -> Unit ) {
        Column(
            modifier = Modifier.clickable(onClick = onClick
            )
        ) {
            Box{
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w185/${movie.poster_path}",
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.66f) //adjust image size in screen
                        .clip(RoundedCornerShape(5.0.dp))

                )
                if (movie.is_favorite)
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = movie.title,
                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                        tint = Color.Red
                    )
            }
            Text(
                text = movie.title,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
            )
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MoviesAppTheme {
        Greeting("Android")
    }
}