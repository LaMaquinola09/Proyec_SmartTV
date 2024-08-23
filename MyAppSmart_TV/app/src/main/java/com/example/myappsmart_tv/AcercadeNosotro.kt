import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myappsmart_tv.R

data class TeamMember(
    val name: String,
    val grade: String,
    val group: String,
    val photoResId: Int,
    val bio: String // Nuevo campo para la biografía
)

@Composable
fun AcercadeNosotro (navController: NavController) {
    var selectedMember by remember { mutableStateOf<TeamMember?>(null) } // Estado para controlar el modal

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Fondo gris claro
    ) {
        TopAppBar(
            title = { Text("Acerca de") },
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = Color.White,
            elevation = 1.dp,
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar al menú")
                }
            }
        )

        // Lista de miembros del equipo
        val team = listOf(
            TeamMember("Victor Antonio Gomez Santiz", "9", "B", R.drawable.victor, "Estudiante de la Carrera de Desarrollo y Gestión de Software")
            // Agrega más miembros del equipo si es necesario
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp) // Ajustar espaciado para mejor presentación
        ) {
            items(team) { member ->
                TeamMemberItem(member = member, onClick = { selectedMember = member }) // Al hacer clic, muestra el modal
            }
        }

        // Modal que muestra los detalles del miembro seleccionado
        if (selectedMember != null) {
            MemberDetailModal(member = selectedMember!!, onDismiss = { selectedMember = null })
        }

        // Información del proyecto dentro de un cuadro con bordes
        Card(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .background(Color.White), // Fondo blanco para el card
            elevation = 4.dp, // Sombra para dar un efecto elevado
            border = BorderStroke(2.dp, Color.Gray) // Bordes grises de 2dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp) // Espacio dentro del cuadro
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally // Centrar contenido horizontalmente
            ) {
                Text(
                    text = "Información de la materia",
                    style = MaterialTheme.typography.h6.copy(fontSize = 20.sp),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(
                    text = "ASIGNATURA: DESARROLLO PARA DISPOSITIVOS INTELIGENTES",
                    style = MaterialTheme.typography.body1,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(
                    text = "Profesor: Dr. Armando Méndez Morales",
                    style = MaterialTheme.typography.body1,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(
                    text = "Cuatrimestre: 9",
                    style = MaterialTheme.typography.body2,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(
                    text = "Año: 2024",
                    style = MaterialTheme.typography.body2,
                    color = Color.Black
                )
            }
        }

        // Spacer to balance the layout and remove bottom space
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun TeamMemberItem(member: TeamMember, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }, // Añade clic para abrir el modal
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
                    .size(80.dp)
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

@Composable
fun MemberDetailModal(member: TeamMember, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = null, // No title since we will include it in the content
        text = {
            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 8.dp,
                color = Color.White // Modal background color
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                        .verticalScroll(rememberScrollState()) // Make the content scrollable
                ) {
                    // Title
                    Text(
                        text = "Información del Personal",
                        style = MaterialTheme.typography.h6.copy(fontSize = 20.sp),
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Centered image
                    Image(
                        painter = painterResource(id = member.photoResId),
                        contentDescription = member.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Centered text
                    Text(
                        text = "Grado: ${member.grade}",
                        style = MaterialTheme.typography.body1,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = "Grupo: ${member.group}",
                        style = MaterialTheme.typography.body1,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Biography text
                    Text(
                        text = member.bio,
                        style = MaterialTheme.typography.body2,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0D47A1)),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text("Contactar", color = Color.White)
                        }

                        OutlinedButton(
                            onClick = { onDismiss() },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        ) {
                            Text("Cerrar", color = Color(0xFF0D47A1))
                        }
                    }
                }
            }
        },
        buttons = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAboutScreen() {
    AcercadeNosotro(navController = rememberNavController()) // Proporciona un controlador de navegación ficticio para la vista previa
}
