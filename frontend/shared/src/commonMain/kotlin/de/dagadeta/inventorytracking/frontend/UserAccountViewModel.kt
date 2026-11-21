package de.dagadeta.inventorytracking.frontend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.dagadeta.inventorytracking.common.model.ChangePasswordResponseDto
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.SUCCESS
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.WRONG_PASSWORD
import de.dagadeta.inventorytracking.common.model.UserInfoDto
import de.dagadeta.inventorytracking.common.model.UserRoleDto
import de.dagadeta.inventorytracking.frontend.Credentials.*
import de.dagadeta.inventorytracking.frontend.UserAccountAction.*
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.ApiResult
import de.dagadeta.inventorytracking.frontend.http.ApiResult.Success
import de.dagadeta.inventorytracking.frontend.http.ApiResult.Unprivileged
import de.dagadeta.inventorytracking.frontend.http.HttpCredentials
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
      is LoginAction -> login(action.userName, action.password)
      LogoutAction -> logout()
      is ChangePasswordAction -> changePassword(action.currentPassword, action.newPassword)
      ShowPasswordChangeAction -> showChangePasswordDialog()
    }
  }

  private fun login(userName: String, password: String) {
    viewModelScope.launch {
      val result =
          when (val info = api.login(HttpCredentials(userName, password))) {
            is ApiResult.Error -> WrongCredentials(info.message)
            is Success<UserInfoDto> -> info.toUserInfo(userName)
            Unprivileged -> WrongCredentials("Invalid user name or password.")
          }
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

sealed interface Credentials {
  object NoCredentials : Credentials

  data class WrongCredentials(val message: String) : Credentials

  data class UserInfo(val name: String, val role: UserRole) : Credentials
}

enum class UserRole {
  NONE,
  USER,
  ADMIN,
}

sealed interface UserAccountAction {
  data class LoginAction(val userName: String, val password: String) : UserAccountAction

  object LogoutAction : UserAccountAction

  object ShowPasswordChangeAction : UserAccountAction

  data class ChangePasswordAction(val currentPassword: String, val newPassword: String) :
      UserAccountAction
}

private fun Success<UserInfoDto>.toUserInfo(userName: String) =
    UserInfo(userName, data.role.toRole())

private fun UserRoleDto.toRole() =
    when (this) {
      UserRoleDto.ADMIN -> UserRole.ADMIN
      UserRoleDto.USER -> UserRole.USER
      UserRoleDto.NONE -> UserRole.NONE
    }
