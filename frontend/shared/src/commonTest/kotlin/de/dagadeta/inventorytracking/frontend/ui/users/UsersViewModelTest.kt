package de.dagadeta.inventorytracking.frontend.ui.users

import de.dagadeta.inventorytracking.common.model.PageDetailsDto
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.common.model.UserDto
import de.dagadeta.inventorytracking.common.model.UserRoleDto
import de.dagadeta.inventorytracking.frontend.MainDispatcherTest
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.ApiResult
import de.dagadeta.inventorytracking.frontend.http.ApiResult.*
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserRole
import de.dagadeta.inventorytracking.frontend.ui.users.UsersAction.FetchUsers
import dev.mokkery.*
import dev.mokkery.answering.returns
import io.kotest.matchers.shouldBe
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class UsersViewModelTest : MainDispatcherTest(), MokkerySuiteScope {

  private lateinit var api: API
  private lateinit var viewModel: UsersViewModel

  @BeforeTest
  fun init() {
    api = mock(MockMode.strict)
    viewModel = UsersViewModel(api)
  }

  @AfterTest fun after() = verifyNoMoreCalls()

  @Test
  fun `the first emitted state contains an empty page`() = runTest {
    viewModel.state.value shouldBe
        UsersState(
            users = Page(emptyList(), 0, 0, 0, 0),
            errorText = "",
        )
  }

  @Test
  fun `FetchUsers successfully`() =
      loadUsersTest(
          Success(
              PageDto(
                  listOf(UserDto(1, "La Forge", UserRoleDto.ADMIN)),
                  PageDetailsDto(1, 2, 3, 4),
              ),
          ),
          UsersState(Page(listOf(User(1, "La Forge", UserRole.ADMIN)), 1, 2, 3, 4)),
      )

  @Test
  fun `FetchUsers with wrong credentials emits an errorText`() =
      loadUsersTest(
          Unprivileged,
          UsersState(errorText = "You are not allowed to browse users."),
      )

  @Test
  fun `FetchUsers with a backend error emits an ErrorMessage`() =
      loadUsersTest(
          Error("message", 42),
          UsersState(errorText = "message (42)"),
      )

  private fun loadUsersTest(loadResult: ApiResult<PageDto<UserDto>>, expectedState: UsersState) =
      runTest {
        everySuspend { api.fetchUsers() } returns loadResult

        viewModel.onAction(FetchUsers)

        viewModel.state.value shouldBe expectedState
        verifySuspend { api.fetchUsers() }
      }
}
