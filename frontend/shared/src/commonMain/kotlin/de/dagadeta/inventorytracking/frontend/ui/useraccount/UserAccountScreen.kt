package de.dagadeta.inventorytracking.frontend.ui.useraccount

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.dagadeta.inventorytracking.frontend.listColumnTag
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserAccountAction.*

const val accountHeaderTag = "account-header"
const val userRoleTag = "user-role"
const val changePasswordComposableTag = "change-password-composable"
const val changePasswordButtonTag = "change-password-button"
const val infoTextTag = "info-text"
const val errorTextTag = "error-text"
const val logoutButtonTag = "logout-button"

@Composable
fun UserAccountScreen(
    state: UserAccountState,
    onAction: (UserAccountAction) -> Unit,
    modifier: Modifier = Modifier,
) {
  Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = modifier.padding(horizontal = 16.dp).testTag(listColumnTag),
  ) {
    Text(
        text = "User Account Information",
        fontSize = 16.sp,
        fontWeight = Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).testTag(accountHeaderTag),
    )

    Text(
        text = "Name: ${state.userInfo.name}",
        modifier = Modifier.testTag(userNameTag),
    )
    Text(
        text = "Role: ${state.userInfo.role}",
        modifier = Modifier.testTag(userRoleTag),
    )

    if (state.showPasswordChangeDialog) {
      ChangePassword(
          onCancelClick = { onAction(TogglePasswordChangeAction) },
          onSubmitClick = { currentPassword, newPassword ->
            onAction(ChangePasswordAction(currentPassword, newPassword))
          },
          modifier = Modifier.fillMaxWidth().testTag(changePasswordComposableTag),
      )
    } else {
      Spacer(modifier = Modifier.height(16.dp))
      Button(
          onClick = { onAction(TogglePasswordChangeAction) },
          modifier = Modifier.testTag(changePasswordButtonTag),
      ) {
        Text("Change password")
      }
    }
    if (state.infoText.isNotEmpty()) {
      Spacer(modifier = Modifier.height(16.dp))
      Text(
          text = state.infoText,
          color = MaterialTheme.colorScheme.secondary,
          modifier = Modifier.testTag(infoTextTag),
      )
    }
    if (state.errorText.isNotEmpty()) {
      Spacer(modifier = Modifier.height(16.dp))
      Text(
          text = state.errorText,
          color = MaterialTheme.colorScheme.error,
          modifier = Modifier.testTag(errorTextTag),
      )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = { onAction(LogoutAction) },
        modifier = Modifier.testTag(logoutButtonTag),
    ) {
      Text("Logout")
    }
  }
}
