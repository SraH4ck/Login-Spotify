import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.lpm.ej09_login.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val text = "Spotify"
    val characters = text.map { it.toString() }
    var startAnimation by remember { mutableStateOf(false) }
    var showImage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(text.length * 100L + 500) // Espera a que termine la animación de las letras
        showImage = true // Muestra la imagen después de la animación de letras
        delay(1000) // Espera un segundo antes de navegar
        navController.navigate(Screens.LoginScreen.name) {
            popUpTo(Screens.SplashScreen.name) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Imagen de Spotify
            val imageScale by animateFloatAsState(targetValue = if (showImage) 1f else 0.5f)
            val imageAlpha by animateFloatAsState(targetValue = if (showImage) 1f else 0f)

            Image(
                painter = rememberAsyncImagePainter("https://imgur.com/nbHKKd9.jpg"),
                contentDescription = "Logo de Spotify",
                modifier = Modifier
                    .scale(imageScale)
                    .alpha(imageAlpha)
                    .size(80.dp) // Tamaño de la imagen
            )

            Spacer(modifier = Modifier.height(20.dp)) // Espacio entre la imagen y el texto

            // Animación de escritura de texto
            Row(horizontalArrangement = Arrangement.Center) {
                characters.forEachIndexed { index, char ->
                    val delayTime = index * 100L

                    LaunchedEffect(startAnimation) {
                        if (startAnimation) delay(delayTime)
                    }

                    val scale by animateFloatAsState(
                        targetValue = if (startAnimation) 1f else 0.5f,
                        animationSpec = androidx.compose.animation.core.tween(
                            durationMillis = 500,
                            delayMillis = delayTime.toInt()
                        )
                    )

                    val alpha by animateFloatAsState(
                        targetValue = if (startAnimation) 1f else 0f,
                        animationSpec = androidx.compose.animation.core.tween(
                            durationMillis = 600,
                            delayMillis = delayTime.toInt()
                        )
                    )

                    Text(
                        text = char,
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(59, 228, 119).copy(alpha = alpha),
                        modifier = Modifier
                            .scale(scale)
                            .padding(horizontal = 1.dp)
                    )
                }
            }
        }
    }
}