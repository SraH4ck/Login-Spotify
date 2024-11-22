package com.lpm.ej09_login.screen.register

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lpm.ej09_login.model.User
import com.lpm.ej09_login.navigation.Screens

@Composable
fun RegisterScreen(navController: NavController? = null) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val displayName = remember { mutableStateOf("") } // Nombre del usuario
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    fun registerUser() {
        Log.d("Register", "registerUser called") // Verifica que el botón activa la función
        auth.createUserWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Register", "Registration successful")
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                    // Crear un objeto User usando el data class
                    val user = User(
                        id = null, // Puedes manejar la ID según tus necesidades
                        userId = userId,
                        displayName = displayName.value,
                        avatarUrl = "", // URL de avatar si tienes
                        quote = "", // Mensaje o cita opcional
                        profession = "" // Profesión opcional
                    )

                    // Guardar los datos en Firestore
                    firestore.collection("users").document(userId)
                        .set(user.toMap())
                        .addOnSuccessListener {
                            Log.d("Register", "User data added to Firestore")
                            navController?.navigate(Screens.LoginScreen.name) // Navega a la pantalla de inicio de sesión
                        }
                        .addOnFailureListener { e ->
                            Log.e("Register", "Failed to add user data to Firestore", e)
                        }
                } else {
                    Log.e("Register", "Registration failed: ${task.exception?.message}")
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Regístrate en Spotify", color = Color.White, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(30.dp))

        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = "Nombre de usuario", color = Color.White)
            TextField(
                value = displayName.value,
                onValueChange = { displayName.value = it },
                placeholder = { Text("Nombre de usuario", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = "Correo electrónico", color = Color.White)
            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                placeholder = { Text("Correo electrónico", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = "Contraseña", color = Color.White)
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                placeholder = { Text("Contraseña", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { registerUser() },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(43.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(59, 228, 119))
        ) {
            Text(text = "Registrarse", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(35.dp))

        Text(text = "¿Ya tienes cuenta? Inicia sesión", color = Color.Gray, modifier = Modifier.clickable {
            navController?.navigate(Screens.LoginScreen.name)
        })
    }
}