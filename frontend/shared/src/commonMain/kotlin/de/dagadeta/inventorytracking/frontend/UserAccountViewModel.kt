package de.dagadeta.inventorytracking.frontend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.dagadeta.inventorytracking.common.model.UserInfoDto
import de.dagadeta.inventorytracking.frontend.UserAccountAction.LoginAction
import de.dagadeta.inventorytracking.frontend.UserAccountAction.LogoutAction
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
      state.update { it.copy(credentials = result) }
    }
  }

  private fun logout() {
    api.logout()
    state.update { it.copy(credentials = NoCredentials) }
  }
}

data class UserAccountState(
    val credentials: Credentials = NoCredentials,
) {
  val isLoggedIn: Boolean
    get() = credentials is UserInfo
}

sealed interface UserAccountAction {
  data class LoginAction(val credentials: InputCredentials) : UserAccountAction

  object LogoutAction : UserAccountAction
}
