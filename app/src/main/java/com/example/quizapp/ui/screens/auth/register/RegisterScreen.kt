package com.example.quizapp.ui.screens.auth.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.ui.UiEvent
import com.example.quizapp.ui.navigation.HomeRoute
import com.example.quizapp.ui.screens.auth.AuthScreensEvent
import com.example.quizapp.ui.screens.auth.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    navigateToHomeScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    val email = viewModel.email
    val password = viewModel.password
    val authState = viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when(uiEvent){
                is UiEvent.Navigate<*> -> {
                    when(uiEvent.route){
                        is HomeRoute -> {
                            navigateToHomeScreen()
                        }
                    }
                }

                is UiEvent.NavigateBack -> {
                    navigateBack()
                }

                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(uiEvent.message)
                }
            }
        }
    }

    RegisterContent(
        email = email,
        password = password,
        authState = authState.value,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun RegisterContent(
    email: String,
    password: String,
    authState: Boolean,
    onEvent: (AuthScreensEvent) -> Unit
){
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Criar Conta", style = MaterialTheme.typography.displayMedium)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { onEvent(AuthScreensEvent.onEmailChange(it)) },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { onEvent(AuthScreensEvent.onPasswordChange(it)) },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))


            Button(
                onClick = { onEvent(AuthScreensEvent.onSignUp) },
                modifier = Modifier.fillMaxWidth(0.8f),
                enabled = !authState
            ) {
                if (authState) CircularProgressIndicator(modifier = Modifier.size(24.dp)) else Text("Cadastrar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { onEvent(AuthScreensEvent.onSignInClick) }, enabled = !authState) {
                Text("Já tem uma conta? Entrar")
            }
        }
    }
}