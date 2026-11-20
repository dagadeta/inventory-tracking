package de.dagadeta.inventorytracking.frontend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.dagadeta.inventorytracking.common.model.ChangePasswordResponseDto
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.SUCCESS
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.WRONG_PASSWORD
import de.dagadeta.inventorytracking.common.model.UserInfoDto
import de.dagadeta.inventorytracking.frontend.UserAccountAction.*
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.ApiResult
import de.dagadeta.inventorytracking.frontend.http.ApiResult.Success
import de.dagadeta.inventorytracking.frontend.http.ApiResult.Unprivileged
import de.dagadeta.inventorytracking.frontend.http.Credentials
import de.dagadeta.inventorytracking.frontend.http.Credentials.*
import de.dagadeta.inventorytracking.frontend.http.toCredentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserAccountViewModel(
    private val api: API,
) : ViewModel() {
  val state: StateFlow<UserAccountState>
    field = MutableStateFlow(UserAccountState())

  fun onAction(action: UserAccountAction) {
    when (action) {
      is LoginAction -> login(action.credentials)
      LogoutAction -> logout()
      is ChangePasswordAction -> changePassword(action.currentPassword, action.newPassword)
      ShowPasswordChangeAction -> showChangePasswordDialog()
    }
  }

  private fun login(credentials: InputCredentials) {
    viewModelScope.launch {
      val result =
          when (val info = api.login(credentials)) {
            is ApiResult.Error -> WrongCredentials(credentials, info.message)
            is Success<UserInfoDto> -> info.toCredentials(credentials)
            Unprivileged -> WrongCredentials(credentials, "Invalid user name or password.")
          }
      // TODO do we need to keep the password here? Should be enough to keep them in HttpClient
      state.update { it.copy(credentials = result) }
    }
  }

  private fun logout() {
    api.logout()
    state.update { it.copy(credentials = NoCredentials) }
  }

  private fun showChangePasswordDialog() {
    updateTexts()
    togglePasswordChangeDialog()
  }

  private fun changePassword(currentPassword: String, newPassword: String) {
    viewModelScope.launch {
      when (val result = api.changePassword(currentPassword, newPassword)) {
        is ApiResult.Error -> {
          updateTexts(error = result.message)
        }

        is Success<ChangePasswordResponseDto> -> {
          when (result.data.result) {
            SUCCESS -> {
              updateTexts("Password changed successfully!")
              togglePasswordChangeDialog()
            }

            WRONG_PASSWORD -> updateTexts(error = "Current password is wrong!")
          }
        }

        Unprivileged -> {
          updateTexts(error = "You are not authorized to change password for this account.")
        }
      }
    }
  }

  private fun updateTexts(info: String = "", error: String = "") {
    state.update { it.copy(infoText = info, errorText = error) }
  }

  private fun togglePasswordChangeDialog() {
    state.update { it.copy(showPasswordChangeDialog = !it.showPasswordChangeDialog) }
  }
}

data class UserAccountState(
    val credentials: Credentials = NoCredentials,
    val infoText: String = "",
    val errorText: String = "",
    val showPasswordChangeDialog: Boolean = false,
) {
  val isLoggedIn: Boolean
    get() = credentials is UserInfo

  val userInfo: UserInfo
    get() = credentials as UserInfo
}

sealed interface UserAccountAction {
  data class LoginAction(val credentials: InputCredentials) : UserAccountAction

  object LogoutAction : UserAccountAction

  object ShowPasswordChangeAction : UserAccountAction

  data class ChangePasswordAction(val currentPassword: String, val newPassword: String) :
      UserAccountAction
}
