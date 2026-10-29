package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType.Companion.Password
import androidx.compose.ui.text.input.KeyboardType.Companion.PersonName
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.dagadeta.inventorytracking.frontend.http.Credentials
import de.dagadeta.inventorytracking.frontend.http.Credentials.InputCredentials
import de.dagadeta.inventorytracking.frontend.http.Credentials.WrongCredentials

@Composable
fun Login(
    onLoginClick: (InputCredentials) -> Unit,
    credentials: Credentials,
) {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
        )

        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            singleLine = true,
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions(
                keyboardType = PersonName,
                imeAction = Next,
            ),
            keyboardActions = KeyboardActions(
                onDone = { onLoginClick(InputCredentials(userName, password)) },
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = Password,
                imeAction = Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { onLoginClick(InputCredentials(userName, password)) },
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onLoginClick(InputCredentials(userName, password)) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Log in")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (credentials is WrongCredentials) {
            Text(
                text = credentials.message,
                color = Color.Red,
            )
        }
    }
}
