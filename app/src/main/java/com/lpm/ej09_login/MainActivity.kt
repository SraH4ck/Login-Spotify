package com.lpm.ej09_login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.FirebaseApp
import com.lpm.ej09_login.navigation.Navigation
import com.lpm.ej09_login.screen.login.LoginScreen
import com.lpm.ej09_login.screen.register.RegisterScreen
import com.lpm.ej09_login.ui.theme.Ej09LoginTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
            Ej09LoginTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(modifier = Modifier.padding(innerPadding))
                    Navigation()                }
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Preview(){
    Ej09LoginTheme {
        LoginScreen()
        RegisterScreen()
    }
}

