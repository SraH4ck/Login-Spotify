package com.lpm.ej09_login.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginScreenViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun signInWithEmailOrUsername(
        input: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
        onUserNotFound: () -> Unit
    ) {
        // Buscar el usuario por email o nombre de usuario en Firestore
        firestore.collection("users")
            .whereEqualTo("email", input)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Si se encuentra el usuario por email, autenticar
                    auth.signInWithEmailAndPassword(input, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Login", "Login successful")
                                onSuccess()
                            } else {
                                task.exception?.let { onFailure(it) }
                            }
                        }
                } else {
                    // Si no encuentra por email, buscar por nombre de usuario
                    firestore.collection("users")
                        .whereEqualTo("display_name", input)
                        .get()
                        .addOnSuccessListener { usernameDocuments ->
                            if (!usernameDocuments.isEmpty) {
                                // Usuario encontrado por nombre de usuario, obtener su email para autenticar
                                val userEmail = usernameDocuments.documents[0].getString("email")
                                if (userEmail != null) {
                                    auth.signInWithEmailAndPassword(userEmail, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Log.d("Login", "Login successful")
                                                onSuccess()
                                            } else {
                                                task.exception?.let { onFailure(it) }
                                            }
                                        }
                                } else {
                                    onUserNotFound()
                                }
                            } else {
                                onUserNotFound()
                            }
                        }
                        .addOnFailureListener { exception ->
                            onFailure(exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun signInWithGoogle(idToken: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Login", "Google login successful")
                    onSuccess()
                } else {
                    task.exception?.let { onFailure(it) }
                }
            }
    }
}
