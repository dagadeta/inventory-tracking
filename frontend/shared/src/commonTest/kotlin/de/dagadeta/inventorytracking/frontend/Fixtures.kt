package de.dagadeta.inventorytracking.frontend

import de.dagadeta.inventorytracking.common.model.*
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.SUCCESS
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.ApiResult
import de.dagadeta.inventorytracking.frontend.http.ApiResult.Success
import de.dagadeta.inventorytracking.frontend.http.HttpCredentials
import de.dagadeta.inventorytracking.frontend.ui.useraccount.Credentials.UserInfo
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserRole.ADMIN
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserRole.USER
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

val userHans = UserInfo("Hans", USER)
val admin = UserInfo("Admin", ADMIN)

/**
 * For more fine-grained control, pass a `StandardTestDispatcher()` as [testDispatcher] and call
 * `advanceUntilIdle()` to finish a scheduled coroutine.
 */
@OptIn(ExperimentalCoroutinesApi::class)
open class MainDispatcherTest(
    protected val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) {
  @BeforeTest
  fun setUpMainDispatcher() {
    Dispatchers.setMain(testDispatcher)
  }

  @AfterTest
  fun tearDownMainDispatcher() {
    Dispatchers.resetMain()
  }
}

val fakeAPI: API =
    object : API {
      override suspend fun login(credentials: HttpCredentials): ApiResult<UserInfoDto> =
          Success(UserInfoDto(1, credentials.userName, UserRoleDto.ADMIN))

      override fun logout() {}

      override suspend fun changePassword(
          currentPassword: String,
          newPassword: String,
      ): ApiResult<ChangePasswordResponseDto> = Success(ChangePasswordResponseDto(SUCCESS))

      override suspend fun fetchInventoryItems(): ApiResult<PageDto<InventoryItemDto>> =
          Success(pageOf(InventoryItemDto(1, "item-1", 1, 0, 1)))

      override suspend fun fetchUsers(): ApiResult<PageDto<UserDto>> =
          Success(pageOf(UserDto(1, "admin", UserRoleDto.ADMIN)))
    }

private fun <T> pageOf(vararg items: T) =
    PageDto(items.toList(), PageDetailsDto(items.size, 1, items.size, 1))
