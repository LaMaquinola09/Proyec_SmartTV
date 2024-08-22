package com.example.myappsmart_tv

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.myappsmart_tv.model.Movie
import com.example.myappsmart_tv.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Composable
fun MovieDetailsScreen(movieId: String, onBackClick: () -> Unit) {
    val apiKey = "993b2122d505abf4d1ead097120645fb"
    val repository = MovieRepository()
    var movie by remember { mutableStateOf<Movie?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Configurar cliente HTTP que omite validación SSL (solo para pruebas)
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .crossfade(true)
        .okHttpClient {
            OkHttpClient.Builder()
                .sslSocketFactory(createInsecureSslSocketFactory(), createInsecureTrustManager())
                .hostnameVerifier { _, _ -> true } // Omite la verificación del hostname
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }
        .build()

    LaunchedEffect(movieId) {
        withContext(Dispatchers.IO) {
            repository.getPopularMovies(apiKey) { movieResponse ->
                val selectedMovie = movieResponse?.results?.find { it.id.toString() == movieId }
                if (selectedMovie != null) {
                    movie = selectedMovie
                    errorMessage = null
                } else {
                    errorMessage = "No se pudo cargar la información de la película."
                }
                isLoading = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "Detalles de película") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            backgroundColor = MaterialTheme.colors.secondaryVariant,
            contentColor = Color.White
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        } else if (errorMessage != null) {
            Text(text = errorMessage ?: "", color = Color.Red, modifier = Modifier.padding(16.dp))
        } else {
            movie?.let {
                val imageUrl = "https://image.tmdb.org/t/p/w500${it.poster_path}"
                Log.d("MovieDetailsScreen", "Image URL: $imageUrl")

                Column(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .crossfade(true)
                                .size(Size.ORIGINAL) // Puedes ajustar el tamaño aquí
                                .build(),
                            imageLoader = imageLoader
                        ),
                        contentDescription = it.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Título: ${it.title}", style = MaterialTheme.typography.h5)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Descripción: ${it.overview}", style = MaterialTheme.typography.body1)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Campos adicionales: Popularidad y Fecha de lanzamiento
                    Text(text = "Popularidad: ${it.popularity}", style = MaterialTheme.typography.body1)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Fecha de lanzamiento: ${it.release_date}", style = MaterialTheme.typography.body1)
                }
            }
        }
    }
}

// Funciones para omitir validación SSL
fun createInsecureSslSocketFactory() = SSLContext.getInstance("TLS").apply {
    init(null, arrayOf(createInsecureTrustManager()), SecureRandom())
}.socketFactory

fun createInsecureTrustManager(): X509TrustManager = object : X509TrustManager {
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
}
