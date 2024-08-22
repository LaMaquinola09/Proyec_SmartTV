package com.example.myappsmart_tv

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class TeamMember(
    val name: String,
    val grade: String,
    val group: String,
    val photoResId: Int
)

@Composable
fun AboutScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(Color(0xFFF5F5F5)) // Fondo gris claro
    ) {
        TopAppBar(
            title = { Text("Acerca de") },
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = Color.White,
            elevation = 4.dp,
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar al menú")
                }
            }
        )

        // Lista de miembros del equipo
        val team = listOf(
            TeamMember("Victor Antonio Gomez Santiz", "9", "B", R.drawable.victor) // Reemplaza con tu imagen en drawable
            // Agrega más miembros del equipo si es necesario
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp) // Ajustar espaciado para mejor presentación
        ) {
            items(team) { member ->
                TeamMemberItem(member)
            }
        }

        // Información del proyecto
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp) // Reducir el espaciado vertical
                .fillMaxWidth()
        ) {
            Text(
                text = "Información del Proyecto",
                style = MaterialTheme.typography.h6.copy(fontSize = 20.sp),
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp) // Reducir el espaciado inferior
            )
            Text("Asignatura: Desarrollo de Software", style = MaterialTheme.typography.body1, color = Color.Black)
            Text("Profesor: Armando", style = MaterialTheme.typography.body2, color = Color.Gray)
            Text("Cuatrimestre: 9", style = MaterialTheme.typography.body2, color = Color.Gray)
            Text("Año: 2024", style = MaterialTheme.typography.body2, color = Color.Gray)
        }
    }
}

@Composable
fun TeamMemberItem(member: TeamMember) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = member.photoResId), // Usa el ID de imagen correcto
                contentDescription = member.name,
                modifier = Modifier
                    .size(80.dp) // Tamaño reducido para más eficiencia de espacio
                    .clip(MaterialTheme.shapes.medium), // Redondear bordes
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = member.name,
                    style = MaterialTheme.typography.h6,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Grado: ${member.grade}",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
                Text(
                    text = "Grupo: ${member.group}",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAboutScreen() {
    AboutScreen(navController = rememberNavController()) // Proporciona un controlador de navegación ficticio para la vista previa
}
