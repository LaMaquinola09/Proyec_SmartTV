import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import coil.request.CachePolicy
import com.example.myappsmart_tv.model.Movie
import com.example.myappsmart_tv.repository.MovieRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

// Función para crear un ImageLoader personalizado
fun createImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .crossfade(true)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .okHttpClient {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        }
        .build()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListaPelicula (navController: NavHostController) {
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
                if (movies != null && movies!!.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3), // Cambia a 3 columnas para más imágenes en fila
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                    ) {
                        items(movies!!.take(20)) { movie ->
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
    Card(
        modifier = Modifier
            .padding(8.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { navController.navigate("details/${movie.id}") }, // Hace que toda la tarjeta sea clickeable
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp) // Reduce el padding para hacer las tarjetas más compactas
        ) {
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
                    .fillMaxWidth() // Hace que la imagen llene el ancho disponible
                    .aspectRatio(0.7f) // Mantiene la proporción de un póster
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.2f))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movie.title,
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón dentro de la tarjeta (opcional, si quieres tener un botón adicional)
            Button(
                onClick = { navController.navigate("details/${movie.id}") },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                modifier = Modifier
                    .fillMaxWidth() // Hace que el botón llene todo el ancho
                    .height(40.dp)
            ) {
                Text(
                    text = "Ver Detalles",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
}
