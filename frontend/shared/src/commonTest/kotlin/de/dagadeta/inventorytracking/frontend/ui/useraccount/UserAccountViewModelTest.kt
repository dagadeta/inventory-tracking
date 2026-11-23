package de.dagadeta.inventorytracking.frontend.ui.useraccount

import de.dagadeta.inventorytracking.common.model.ChangePasswordResponseDto
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.SUCCESS
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.WRONG_PASSWORD
import de.dagadeta.inventorytracking.common.model.UserInfoDto
import de.dagadeta.inventorytracking.common.model.UserRoleDto.USER
import de.dagadeta.inventorytracking.frontend.MainDispatcherTest
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.ApiResult
import de.dagadeta.inventorytracking.frontend.http.ApiResult.*
import de.dagadeta.inventorytracking.frontend.http.HttpCredentials
import de.dagadeta.inventorytracking.frontend.ui.useraccount.Credentials.*
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserAccountAction.*
import dev.mokkery.*
import dev.mokkery.answering.returns
import dev.mokkery.matcher.any
import io.kotest.matchers.shouldBe
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class UserAccountViewModelTest : MainDispatcherTest(), MokkerySuiteScope {

  private lateinit var api: API
  private lateinit var viewModel: UserAccountViewModel

  @BeforeTest
  fun init() {
    api = mock(MockMode.strict)
    viewModel = UserAccountViewModel(api)
  }

  @AfterTest fun after() = verifyNoMoreCalls()

  @Test
  fun `the first emitted state has no credentials or texts`() = runTest {
    viewModel.state.value shouldBe
        UserAccountState(
            credentials = NoCredentials,
            infoText = "",
            errorText = "",
            showPasswordChangeDialog = false,
        )
  }

  @Test
  fun `the LoginAction with successful login emits UserInfo`() =
      loginTest(
          Success(UserInfoDto(1, "Name", USER)),
          UserInfo("Name", UserRole.USER),
      )

  @Test
  fun `the LoginAction with wrong credentials emits an ErrorMessage`() =
      loginTest(
          Unprivileged,
          ErrorMessage("Invalid user name or password."),
      )

  @Test
  fun `the LoginAction with a backend error emits an ErrorMessage`() =
      loginTest(
          Error("message", 42),
          ErrorMessage("message"),
      )

  private fun loginTest(loginResult: ApiResult<UserInfoDto>, expectedCredentials: Credentials) =
      runTest {
        everySuspend { api.login(any()) } returns loginResult

        viewModel.onAction(LoginAction("Name", "Password"))

        viewModel.state.value shouldBe UserAccountState(expectedCredentials)
        verifySuspend { api.login(HttpCredentials("Name", "Password")) }
      }

  @Test
  fun `the LogoutAction emits NoCredentials`() = runTest {
    everySuspend { api.login(any()) } returns Success(UserInfoDto(1, "Name", USER))
    everySuspend { api.logout() } returns Unit
    viewModel.onAction(LoginAction("Name", "Password"))

    viewModel.onAction(LogoutAction)

    viewModel.state.value shouldBe UserAccountState(NoCredentials)
    verifySuspend {
      api.login(HttpCredentials("Name", "Password"))
      api.logout()
    }
  }

  @Test
  fun `the ChangePasswordAction with a successful change emits an infoText and closes the dialog`() =
      changePasswordTest(
          changePasswordResult = Success(ChangePasswordResponseDto(SUCCESS)),
          expectedStage =
              UserAccountState(
                  infoText = "Password changed successfully!",
                  showPasswordChangeDialog = false,
              ),
      )

  @Test
  fun `the ChangePasswordAction with a wrong password emits an errorText`() =
      changePasswordTest(
          changePasswordResult = Success(ChangePasswordResponseDto(WRONG_PASSWORD)),
          expectedStage =
              UserAccountState(
                  errorText = "Current password is wrong!",
                  showPasswordChangeDialog = true,
              ),
      )

  @Test
  fun `the ChangePasswordAction without access emits an errorText`() =
      changePasswordTest(
          changePasswordResult = Unprivileged,
          expectedStage =
              UserAccountState(
                  errorText = "You are not authorized to change password for this account.",
                  showPasswordChangeDialog = true,
              ),
      )

  @Test
  fun `the ChangePasswordAction with backend error emits an errorText`() =
      changePasswordTest(
          changePasswordResult = Error("sth. went wrong!", 7),
          expectedStage =
              UserAccountState(
                  errorText = "sth. went wrong!",
                  showPasswordChangeDialog = true,
              ),
      )

  private fun changePasswordTest(
      changePasswordResult: ApiResult<ChangePasswordResponseDto>,
      expectedStage: UserAccountState,
  ) = runTest {
    everySuspend { api.changePassword(any(), any()) } returns changePasswordResult
    viewModel.onAction(ShowPasswordChangeAction)
    viewModel.state.value shouldBe UserAccountState(showPasswordChangeDialog = true)

    viewModel.onAction(ChangePasswordAction("current", "new"))

    viewModel.state.value shouldBe expectedStage
    verifySuspend { api.changePassword("current", "new") }
  }
}
