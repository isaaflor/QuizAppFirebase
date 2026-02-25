package com.example.quizapp.ui.screens.auth.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.ui.UiEvent
import com.example.quizapp.ui.navigation.HomeRoute
import com.example.quizapp.ui.navigation.RegisterRoute
import com.example.quizapp.ui.screens.auth.AuthScreensEvent
import com.example.quizapp.ui.screens.auth.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    navigateToHomeScreen: () -> Unit,
    navigateToRegisterScreen: () -> Unit
) {
    val email = viewModel.email
    val password = viewModel.password
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when(uiEvent){
                is UiEvent.Navigate<*> -> {
                    when(uiEvent.route){
                        HomeRoute -> {navigateToHomeScreen()}
                        RegisterRoute -> {navigateToRegisterScreen()}
                    }
                }
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(uiEvent.message)
                }
                else -> {}
            }
        }
    }

    LoginContent(
        email = email,
        password = password,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginContent(
    email: String,
    password: String,
    onEvent: (AuthScreensEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        // O SnackBarHost deve ser declarado aqui para o Scaffold gerenciar as mensagens
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título principal estilizado
                Text(
                    text = "FourQuiz",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Faça login para começar",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Campo de E-mail com ícone e bordas arredondadas
                OutlinedTextField(
                    value = email,
                    onValueChange = { onEvent(AuthScreensEvent.onEmailChange(it)) },
                    label = { Text("E-mail") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Senha com ícone e transformação visual
                OutlinedTextField(
                    value = password,
                    onValueChange = { onEvent(AuthScreensEvent.onPasswordChange(it)) },
                    label = { Text("Senha") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botão de Entrar estilizado
                Button(
                    onClick = { onEvent(AuthScreensEvent.onSignIn) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                ) {
                    Text("Entrar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Link para cadastro
                TextButton(
                    onClick = { onEvent(AuthScreensEvent.onSignUpClick) },
                ) {
                    Text(
                        "Ainda não tem conta? Criar Conta",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}