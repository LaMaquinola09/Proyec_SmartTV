package com.example.myappsmart_tv

import AcercadeNosotro
import ListaPelicula
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MyApp { finishAffinity() }
            }
        }
    }
}

@Composable
fun MyApp(onExitClick: () -> Unit) {
    val navController = rememberNavController()
    Scaffold { paddingValues ->  // Aquí usamos 'paddingValues'
        NavHost(
            navController = navController,
            startDestination = "menu",
            modifier = Modifier.padding(paddingValues) // Añadimos el padding aquí
        ) {
            composable("menu") { MainMenu(navController, onExitClick) }
            composable("movies") { ListaPelicula(navController) }
            composable("details/{movieId}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")
                movieId?.let {
                    DetallesPelicula(
                        movieId = it,
                        onBackClick = { navController.navigateUp() }
                    )
                }
            }
            composable("about") { AcercadeNosotro(navController) }
        }
    }
}

@Composable
fun MainMenu(navController: NavHostController, onExitClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.pantalla), // Reemplaza con el ID de tu imagen de fondo
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "BIENVENIDOS A",
                style = MaterialTheme.typography.h6.copy(fontSize = 50.sp),
                color = MaterialTheme.colors.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "DaysiStream",
                style = MaterialTheme.typography.h5.copy(fontSize = 45.sp),
                color = MaterialTheme.colors.onPrimary
            )
            Text(
                text = "Tu plataforma de peliculas",
                style = MaterialTheme.typography.h6.copy(fontSize = 20.sp),
                color = MaterialTheme.colors.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Carrusel de imágenes
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Asegúrate de tener las imágenes en drawable
                val images = listOf(
                    R.drawable.imagen1, // Reemplaza con tus imágenes
                    R.drawable.peli1,
                    R.drawable.peli2,
                    R.drawable.peli3,
                    R.drawable.imagen2
                )

                items(images) { imageResId ->
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones en fila
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botón que navega al menú de películas
                Button(
                    onClick = { navController.navigate("movies") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Yellow,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Peliculas",
                        fontSize = 20.sp // Ajusta el tamaño de la fuente según sea necesario
                    )
                }

                // Botón Acerca de Nosotros
                Button(
                    onClick = { navController.navigate("about") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Acerca de Nosotros",
                        fontSize = 20.sp // Ajusta el tamaño de la fuente según sea necesario
                    )
                }

                // Botón de salir
                Button(
                    onClick = {
                        onExitClick() // Manejamos el evento de salida desde aquí
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Salir",
                        fontSize = 20.sp // Ajusta el tamaño de la fuente según sea necesario
                    )
                }
            }
        }
    }
}
