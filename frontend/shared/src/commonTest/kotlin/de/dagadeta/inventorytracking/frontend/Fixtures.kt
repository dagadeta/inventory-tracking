package de.dagadeta.inventorytracking.frontend

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
