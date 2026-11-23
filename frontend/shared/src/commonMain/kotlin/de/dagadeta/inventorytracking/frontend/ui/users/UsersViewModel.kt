package de.dagadeta.inventorytracking.frontend.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.common.model.UserDto
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.ApiResult.*
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserRole
import de.dagadeta.inventorytracking.frontend.ui.useraccount.toRole
import de.dagadeta.inventorytracking.frontend.ui.users.UsersAction.FetchUsers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsersViewModel(
    private val api: API,
) : ViewModel() {
  val state: StateFlow<UsersState>
    field = MutableStateFlow(UsersState())

  fun onAction(action: UsersAction) {
    when (action) {
      FetchUsers -> fetchUsers()
    }
  }

  private fun fetchUsers() {
    viewModelScope.launch {
      when (val result = api.fetchUsers()) {
        is Error ->
            state.update {
              it.copy(users = Page(), errorText = "${result.message} (${result.code})")
            }
        is Success<PageDto<UserDto>> ->
            state.update { it.copy(users = result.toUsers(), errorText = "") }
        Unprivileged ->
            state.update {
              it.copy(users = Page(), errorText = "You are not allowed to browse users.")
            }
      }
    }
  }
}

data class UsersState(val users: Page<User> = Page(), val errorText: String = "")

data class Page<T>(
    val content: List<T> = emptyList(),
    val size: Int = 0,
    val number: Int = 0,
    val totalElements: Int = 0,
    val totalPages: Int = 0,
)

data class User(val id: Long, val name: String, val role: UserRole)

private fun Success<PageDto<UserDto>>.toUsers() =
    Page(
        content = data.content.map { it.toUser() },
        size = data.page.size,
        number = data.page.number,
        totalElements = data.page.totalElements,
        totalPages = data.page.totalPages,
    )

private fun UserDto.toUser() = User(id, name, role.toRole())

sealed interface UsersAction {
  object FetchUsers : UsersAction
}
