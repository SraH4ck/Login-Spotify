package com.lpm.ej09_login.screen.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.lpm.ej09_login.navigation.Screens

@Composable
fun LoginScreen(
    navController: NavController? = null,
    viewModel: LoginScreenViewModel = viewModel()
) {
    // Configuración de Google Sign-In
    val context = LocalContext.current
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("YOUR_CLIENT_ID") // Reemplaza con tu ID de cliente de Firebase
        .requestEmail()
        .build()
    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    // Lanzador para manejar el resultado de la actividad de Google Sign-In
    val signInLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignInResult(task, navController, viewModel)
    }

    // Función para iniciar el flujo de Google Sign-In
    fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    // UI del LoginScreen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo de Spotify
        Image(
            painter = rememberAsyncImagePainter("https://imgur.com/ufiErvn.jpg"),
            contentDescription = "Logo de Spotify",
            modifier = Modifier
                .size(60.dp)
                .padding(bottom = 16.dp)
        )

        Text(text = "Inicia sesión en Spotify", color = Color.White, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(30.dp))

        // Botón para iniciar sesión con Google
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(Color.Transparent)
                .border(1.dp, Color.White, RoundedCornerShape(30.dp))
                .padding(10.dp)
                .clickable { signInWithGoogle() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Logo de Google
                Image(
                    painter = rememberAsyncImagePainter("https://imgur.com/CkZHDpA.jpg"),
                    contentDescription = "Logo de Google",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Texto para iniciar sesión con Google
                Text(text = "Iniciar sesión con Google", color = Color.White, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(43.dp))

        // Línea divisoria
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Gray)
                .padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Campo para Email o nombre de usuario
        Column(
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                text = "Email o nombre de usuario",
                color = Color.White,
                modifier = Modifier.padding(start = 1.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                Text(text = "Email o nombre de usuario", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Campo para Contraseña
        Column(
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                text = "Contraseña",
                color = Color.White,
                modifier = Modifier.padding(start = 1.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                Text(text = "Contraseña", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Botón para acceder
        Button(
            onClick = {
                navController?.navigate(Screens.HomeScreen.name)
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(43.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(59, 228, 119))
        ) {
            Text(text = "Acceder", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(35.dp))

        // Opciones adicionales
        Text(text = "¿Has olvidado tu contraseña?", color = Color.Gray)
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "¿No tienes cuenta? Regístrate en Spotify",
            color = Color.Gray,
            modifier = Modifier.clickable {
                navController?.navigate(Screens.RegisterScreen.name) // Navega a la pantalla de registro
            }
        )
    }
}

// Función para manejar el resultado del inicio de sesión de Google
private fun handleSignInResult(
    task: Task<GoogleSignInAccount>,
    navController: NavController?,
    viewModel: LoginScreenViewModel
) {
    try {
        val account = task.getResult(ApiException::class.java)
        val idToken = account?.idToken
        if (idToken != null) {
            viewModel.signInWithGoogle(idToken,
                onSuccess = {
                    // Redirigir al HomeScreen
                    navController?.navigate(Screens.HomeScreen.name) {
                        popUpTo(Screens.LoginScreen.name) { inclusive = true }
                    }
                },
                onFailure = { e -> Log.e("Login", "Google sign-in failed", e) }
            )
        }
    } catch (e: ApiException) {
        Log.e("Login", "Google Sign-In failed", e)
    }
}