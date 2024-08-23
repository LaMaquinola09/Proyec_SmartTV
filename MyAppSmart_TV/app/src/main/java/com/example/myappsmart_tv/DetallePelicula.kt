package com.example.myappsmart_tv

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
fun DetallesPelicula (movieId: String, onBackClick: () -> Unit) {
    val apiKey = "993b2122d505abf4d1ead097120645fb"
    val repository = MovieRepository()
    var movie by remember { mutableStateOf<Movie?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .crossfade(true)
        .okHttpClient {
            OkHttpClient.Builder()
                .sslSocketFactory(createInsecureSslSocketFactory(), createInsecureTrustManager())
                .hostnameVerifier { _, _ -> true }
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

                // Organizar la imagen y los datos en una fila (horizontal)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Imagen en el lado izquierdo
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .crossfade(true)
                                .size(Size.ORIGINAL)
                                .build(),
                            imageLoader = imageLoader
                        ),
                        contentDescription = it.title,
                        modifier = Modifier
                            .weight(1f) // La imagen toma 1/3 del espacio horizontal
                            .height(400.dp)
                            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Datos de la película en el lado derecho
                    Column(
                        modifier = Modifier
                            .weight(2f) // Los datos toman 2/3 del espacio horizontal
                            .padding(8.dp)
                    ) {
                        // Título
                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.h4.copy(
                                fontSize = 26.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Descripción
                        Text(
                            text = it.overview,
                            style = MaterialTheme.typography.body1.copy(
                                fontSize = 16.sp,
                                color = Color.Gray
                            ),
                            maxLines = 5, // Limita a 5 líneas
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Popularidad
                        Text(
                            text = "Popularidad: ${it.popularity}",
                            style = MaterialTheme.typography.body1.copy(
                                fontSize = 16.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Fecha de lanzamiento
                        Text(
                            text = "Fecha de lanzamiento: ${it.release_date}",
                            style = MaterialTheme.typography.body1.copy(
                                fontSize = 16.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
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
