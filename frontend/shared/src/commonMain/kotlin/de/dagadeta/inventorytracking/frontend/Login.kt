package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType.Companion.Password
import androidx.compose.ui.text.input.KeyboardType.Companion.PersonName
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.dagadeta.inventorytracking.frontend.UserAccountAction.LoginAction
import de.dagadeta.inventorytracking.frontend.http.Credentials
import de.dagadeta.inventorytracking.frontend.http.Credentials.*

const val userNameTag = "user-name"
const val passwordTag = "password"
const val loginButtonTag = "login-button"
const val hintTextTag = "hint-text"

@Composable
fun Login(
    onAction: (LoginAction) -> Unit,
    credentials: Credentials,
) {
  var userName by remember { mutableStateOf(credentials.userName) }
  var password by remember { mutableStateOf(credentials.password) }

  Column(
      modifier = Modifier.padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.Center,
  ) {
    Text(
        text = "Login",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
    )

    OutlinedTextField(
        value = userName,
        onValueChange = { userName = it },
        singleLine = true,
        label = { Text("Name") },
        keyboardOptions =
            KeyboardOptions(
                keyboardType = PersonName,
                imeAction = Next,
            ),
        modifier = Modifier.fillMaxWidth().testTag(userNameTag),
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        singleLine = true,
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions =
            KeyboardOptions(
                keyboardType = Password,
                imeAction = Done,
            ),
        keyboardActions =
            KeyboardActions(
                onDone = { onAction(LoginAction(InputCredentials(userName, password))) },
            ),
        modifier = Modifier.fillMaxWidth().testTag(passwordTag),
    )

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        onClick = { onAction(LoginAction(InputCredentials(userName, password))) },
        modifier = Modifier.fillMaxWidth().testTag(loginButtonTag),
    ) {
      Text(text = "Log in")
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (credentials is WrongCredentials) {
      Text(
          text = credentials.message,
          color = MaterialTheme.colorScheme.error,
          modifier = Modifier.testTag(hintTextTag),
      )
    }
  }
}

private val Credentials.userName: String
  get() =
      when (this) {
        is InputCredentials -> userName
        NoCredentials -> ""
        is UserInfo -> inputCredentials.userName
        is WrongCredentials -> inputCredentials.userName
      }

private val Credentials.password: String
  get() =
      when (this) {
        is InputCredentials -> password
        NoCredentials -> ""
        is UserInfo -> inputCredentials.password
        is WrongCredentials -> inputCredentials.password
      }
