package de.dagadeta.inventorytracking.frontend.ui.inventory

import de.dagadeta.inventorytracking.common.model.InventoryItemDto
import de.dagadeta.inventorytracking.common.model.PageDetailsDto
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.frontend.MainDispatcherTest
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.ApiResult
import de.dagadeta.inventorytracking.frontend.http.ApiResult.*
import de.dagadeta.inventorytracking.frontend.ui.inventory.InventoryAction.FetchInventoryItems
import de.dagadeta.inventorytracking.frontend.ui.users.Page
import dev.mokkery.*
import dev.mokkery.answering.returns
import io.kotest.matchers.shouldBe
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class InventoryViewModelTest : MainDispatcherTest(), MokkerySuiteScope {

  private lateinit var api: API
  private lateinit var viewModel: InventoryViewModel

  @BeforeTest
  fun init() {
    api = mock(MockMode.strict)
    viewModel = InventoryViewModel(api)
  }

  @AfterTest fun after() = verifyNoMoreCalls()

  @Test
  fun `the first emitted state contains an empty page`() = runTest {
    viewModel.state.value shouldBe
        InventoryState(
            items = Page(emptyList(), 0, 0, 0, 0),
            errorText = "",
        )
  }

  @Test
  fun `FetchInventoryItems successfully`() =
      fetchInventoryItemsTest(
          Success(
              PageDto(
                  listOf(InventoryItemDto(1, "Flux Capacitor", 1, 1, 0)),
                  PageDetailsDto(1, 2, 3, 4),
              ),
          ),
          InventoryState(
              Page(listOf(InventoryItem(1, "Flux Capacitor", 1, 1, 0)), 1, 2, 3, 4),
          ),
      )

  @Test
  fun `FetchInventoryItems with wrong credentials emits an errorText`() =
      fetchInventoryItemsTest(
          Unprivileged,
          InventoryState(errorText = "You are not allowed to browse items."),
      )

  @Test
  fun `FetchInventoryItems with a backend error emits an ErrorMessage`() =
      fetchInventoryItemsTest(
          Error("message", 42),
          InventoryState(errorText = "message (42)"),
      )

  private fun fetchInventoryItemsTest(
      loadResult: ApiResult<PageDto<InventoryItemDto>>,
      expectedState: InventoryState,
  ) = runTest {
    everySuspend { api.fetchInventoryItems() } returns loadResult

    viewModel.onAction(FetchInventoryItems)

    viewModel.state.value shouldBe expectedState
    verifySuspend { api.fetchInventoryItems() }
  }
}
