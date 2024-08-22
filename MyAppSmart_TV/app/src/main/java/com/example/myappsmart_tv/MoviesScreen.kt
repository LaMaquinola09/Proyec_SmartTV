import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myappsmart_tv.model.Movie
import com.example.myappsmart_tv.repository.MovieRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

fun createImageLoader(context: android.content.Context): ImageLoader {
    return ImageLoader.Builder(context)
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
}

@Composable
fun MoviesScreen(navController: NavHostController) {
    val apiKey = "993b2122d505abf4d1ead097120645fb"
    val repository = MovieRepository()
    var movies by remember { mutableStateOf<List<Movie>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val imageLoader = createImageLoader(LocalContext.current)

    LaunchedEffect(Unit) {
        repository.getPopularMovies(apiKey) { movieResponse ->
            if (movieResponse != null) {
                movies = movieResponse.results
                errorMessage = null
            } else {
                errorMessage = "No se pudieron cargar las películas."
                Log.e("MoviesScreen", errorMessage ?: "Error desconocido")
            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Películas") },
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = Color.Black,
                elevation = 4.dp,
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar al menú")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
            } else if (errorMessage != null) {
                Text(text = errorMessage ?: "", color = MaterialTheme.colors.error, modifier = Modifier.padding(16.dp))
            } else {
                if (movies != null && movies!!.size >= 10) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = 0.1f))
                    ) {
                        items(movies!!.take(10)) { movie ->
                            MovieItem(movie = movie, navController = navController, imageLoader = imageLoader)
                        }
                    }
                } else {
                    Text(
                        text = "No hay suficientes películas disponibles para mostrar.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, navController: NavHostController, imageLoader: ImageLoader) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.White)
            .border(0.dp, Color.Black)
            .padding(8.dp)
            .shadow(0.dp, RoundedCornerShape(8.dp))
    ) {
        // Imagen de la película usando Coil
        val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                imageLoader = imageLoader
            ),
            contentDescription = movie.title,
            modifier = Modifier
                .size(120.dp)
                .background(Color.Gray.copy(alpha = 0.2f))
                .padding(4.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        Button(
            onClick = {
                navController.navigate("details/${movie.id}")
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            modifier = Modifier
                .height(40.dp)
                .padding(top = 4.dp)
                .width(150.dp)
        ) {
            Text(
                text = "Ver Detalles",
                fontSize = 16.sp,
                color = Color.Black
            )
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
