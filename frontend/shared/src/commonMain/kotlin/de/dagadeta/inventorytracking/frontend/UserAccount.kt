package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.dagadeta.inventorytracking.common.model.ChangePasswordResponseDto
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.SUCCESS
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.WRONG_PASSWORD
import de.dagadeta.inventorytracking.common.model.UserInfoDto
import de.dagadeta.inventorytracking.common.model.UserInfoDto.Companion.NO_USER
import de.dagadeta.inventorytracking.frontend.http.ApiResult
import de.dagadeta.inventorytracking.frontend.http.ApiResult.*
import kotlinx.coroutines.launch

private data class Texts(val info: String = "", val error: String = "")

const val accountHeaderTag = "account-header"
const val userRoleTag = "user-role"
const val changePasswordComposableTag = "change-password-composable"
const val changePasswordButtonTag = "change-password-button"
const val infoTextTag = "info-text"
const val errorTextTag = "error-text"

@Composable
fun UserAccount(
    loadUserInfo: suspend () -> ApiResult<UserInfoDto>,
    changePassword:
        suspend (oldPassword: String, newPassword: String) -> ApiResult<ChangePasswordResponseDto>,
    modifier: Modifier = Modifier,
) {
  val coroutineScope = rememberCoroutineScope()
  var userInfo by remember { mutableStateOf(NO_USER) }
  var texts by remember { mutableStateOf(Texts()) }
  var passwordChangeDialogOpen by remember { mutableStateOf(false) }

  LaunchedEffect(true) {
    when (val result = loadUserInfo()) {
      is Success -> {
        userInfo = result.data
        texts = Texts()
      }

      is Error -> {
        userInfo = NO_USER
        texts = Texts(error = result.message)
      }

      Unprivileged -> {
        userInfo = NO_USER
        texts = Texts(error = "You are not authorized to load user info for this account.")
      }
    }
  }

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
        text = "Name: ${userInfo.name}",
        modifier = Modifier.testTag(userNameTag),
    )
    Text(
        text = "Role: ${userInfo.role}",
        modifier = Modifier.testTag(userRoleTag),
    )

    if (passwordChangeDialogOpen) {
      ChangePassword(
          onSubmitClick = { newPassword, newPasswordRepeated ->
            coroutineScope.launch {
              when (val result = changePassword(newPassword, newPasswordRepeated)) {
                is Error -> {
                  texts = Texts(error = result.message)
                }

                is Success<ChangePasswordResponseDto> -> {
                  when (result.data.result) {
                    SUCCESS -> {
                      texts = Texts("Password changed successfully!")
                      passwordChangeDialogOpen = false
                    }

                    WRONG_PASSWORD -> texts = Texts(error = "Current password is wrong!")
                  }
                }

                Unprivileged -> {
                  texts =
                      Texts(error = "You are not authorized to change password for this account.")
                }
              }
            }
          },
          modifier = Modifier.fillMaxWidth().testTag(changePasswordComposableTag),
      )
    } else {
      Button(
          onClick = {
            texts = Texts()
            passwordChangeDialogOpen = true
          },
          modifier = Modifier.testTag(changePasswordButtonTag),
      ) {
        Text("Change password")
      }
    }
    if (texts.info.isNotEmpty()) {
      Spacer(modifier = Modifier.height(16.dp))
      Text(
          text = texts.info,
          color = Color.DarkGray,
          modifier = Modifier.testTag(infoTextTag),
      )
    }
    if (texts.error.isNotEmpty()) {
      Spacer(modifier = Modifier.height(16.dp))
      Text(
          text = texts.error,
          color = Color.Red,
          modifier = Modifier.testTag(errorTextTag),
      )
    }
  }
}
