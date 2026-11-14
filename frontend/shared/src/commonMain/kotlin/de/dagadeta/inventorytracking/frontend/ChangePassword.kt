package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.ImeAction.Companion.Next
import androidx.compose.ui.text.input.KeyboardType.Companion.Password
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val changePasswordHeaderTag = "change-password"
const val currentPasswordTag = "current-password"
const val newPasswordTag = "new-password"
const val newPasswordRepeatedTag = "new-password-repeated"
const val submitButtonTag = "submit"

@Composable
fun ChangePassword(
    onSubmitClick: (currentPassword: String, newPassword: String) -> Unit,
    modifier: Modifier = Modifier,
) {
  var currentPassword by remember { mutableStateOf("") }
  var newPassword by remember { mutableStateOf("") }
  var newPasswordRepeated by remember { mutableStateOf("") }
  val submitEnabled = newPassword.appliesToRules(newPasswordRepeated)

  Column(modifier = modifier) {
    Text(
        text = "Change Password",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.testTag(changePasswordHeaderTag),
    )

    OutlinedTextField(
        value = currentPassword,
        onValueChange = { currentPassword = it },
        singleLine = true,
        label = { Text("Current Password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions =
            KeyboardOptions(
                keyboardType = Password,
                imeAction = Next,
            ),
        modifier = Modifier.fillMaxWidth().testTag(currentPasswordTag),
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = newPassword,
        onValueChange = { newPassword = it },
        singleLine = true,
        label = { Text("New Password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions =
            KeyboardOptions(
                keyboardType = Password,
                imeAction = Next,
            ),
        modifier = Modifier.fillMaxWidth().testTag(newPasswordTag),
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = newPasswordRepeated,
        onValueChange = { newPasswordRepeated = it },
        singleLine = true,
        label = { Text("Repeat New Password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions =
            KeyboardOptions(
                keyboardType = Password,
                imeAction = Done,
            ),
        keyboardActions =
            KeyboardActions(
                onDone = { if (submitEnabled) onSubmitClick(currentPassword, newPassword) },
            ),
        modifier = Modifier.fillMaxWidth().testTag(newPasswordRepeatedTag),
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = { onSubmitClick(currentPassword, newPassword) },
        modifier = Modifier.fillMaxWidth().testTag(submitButtonTag),
        enabled = submitEnabled,
    ) {
      Text("Submit")
    }
  }
}

private fun String.appliesToRules(repeated: String) = isNotBlank() && this == repeated
