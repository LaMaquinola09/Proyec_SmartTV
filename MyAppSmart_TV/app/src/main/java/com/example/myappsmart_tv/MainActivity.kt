package com.example.myappsmart_tv

import MoviesScreen
import android.os.Bundle
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    Scaffold(
        topBar = { TopAppBar(title = { Text("Plataforma de Películas") }) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "menu",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("menu") { MainMenu(navController, onExitClick) }
            composable("movies") { MoviesScreen(navController) }
            composable("details/{movieId}") { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId")
                movieId?.let {
                    MovieDetailsScreen(
                        movieId = it,
                        onBackClick = { navController.navigateUp() }
                    )
                }
            }
            composable("about") { AboutScreen(navController) }
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
                style = MaterialTheme.typography.h3.copy(fontSize = 50.sp),
                color = MaterialTheme.colors.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "2024\" PLATAFORMA DE PELICULA",
                style = MaterialTheme.typography.h5.copy(fontSize = 30.sp),
                color = MaterialTheme.colors.onPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("movies") },
                modifier = Modifier
                    .width(300.dp) // Ajusta el ancho del botón
                    .padding(10.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Green,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Lista de Películas",
                    fontSize = 24.sp // Aumenta el tamaño de la fuente
                )
            }


            Button(
                onClick = { navController.navigate("about") },
                modifier = Modifier
                    .width(300.dp)
                    .padding(10.dp),
                colors = ButtonDefaults.buttonColors(
                     backgroundColor = Color.Blue,

                     contentColor = Color.White
                )
            ){
                Text(
                    text = "Acerca de Nosotros",
                    fontSize = 24.sp // Aumenta el tamaño de la fuente
                )
            }


            Button(
                onClick = {
                    onExitClick() // Manejamos el evento de salida desde aquí
                },
                modifier = Modifier
                    .width(300.dp)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Salir",
                    fontSize = 24.sp // Aumenta el tamaño de la fuente
                )
            }
        }
    }
}

