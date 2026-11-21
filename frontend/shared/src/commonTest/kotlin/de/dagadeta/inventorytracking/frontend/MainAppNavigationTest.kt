package de.dagadeta.inventorytracking.frontend

import androidx.compose.ui.semantics.SemanticsProperties.StateDescription
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher.Companion.expectValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import de.dagadeta.inventorytracking.common.model.PageDto.Companion.emptyPage
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.ApiResult.Success
import de.dagadeta.inventorytracking.frontend.http.fakeAPI
import dev.mokkery.answering.calls
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlin.test.Test
import kotlinx.coroutines.test.TestResult

@OptIn(ExperimentalTestApi::class)
class MainAppNavigationTest {

  @Test
  fun `navigation rail can be expanded and collapsed`(): TestResult {
    return runComposeUiTest {
      setContent {
        MainAppNavigation(
            fakeAPI,
            UserAccountState(userHans),
            {},
        )
      }

      onNodeWithTag(expandButtonTag).assert(expectValue(StateDescription, "Collapsed"))
      onNodeWithTag(expandButtonTag).performClick()
      onNodeWithTag(expandButtonTag).assert(expectValue(StateDescription, "Expanded"))
      onNodeWithTag(expandButtonTag).performClick()
      onNodeWithTag(expandButtonTag).assert(expectValue(StateDescription, "Collapsed"))
    }
  }

  @Test
  fun `can switch between list items`() = runComposeUiTest {
    setContent {
      MainAppNavigation(
          fakeAPI,
          UserAccountState(admin),
          {},
      )
    }

    onNodeWithTag(inventoryListTag).assertExists()
    onNodeWithTag(usersListTag).assertDoesNotExist()
    onNodeWithTag(userAccountTag).assertDoesNotExist()

    onNodeWithTag("navigate-to-Users").performClick()

    onNodeWithTag(inventoryListTag).assertDoesNotExist()
    onNodeWithTag(usersListTag).assertExists()
    onNodeWithTag(userAccountTag).assertDoesNotExist()

    onNodeWithTag("navigate-to-Account").performClick()

    onNodeWithTag(inventoryListTag).assertDoesNotExist()
    onNodeWithTag(usersListTag).assertDoesNotExist()
    onNodeWithTag(userAccountTag).assertExists()

    onNodeWithTag("navigate-to-Inventory").performClick()

    onNodeWithTag(inventoryListTag).assertExists()
    onNodeWithTag(usersListTag).assertDoesNotExist()
    onNodeWithTag(userAccountTag).assertDoesNotExist()
  }

  @Test
  fun `admin users see all navigation items`() = runComposeUiTest {
    setContent {
      MainAppNavigation(
          fakeAPI,
          UserAccountState(admin),
          {},
      )
    }

    onNodeWithTag("navigate-to-Inventory").assertExists()
    onNodeWithTag("navigate-to-Users").assertExists()
    onNodeWithTag("navigate-to-Account").assertExists()
  }

  @Test
  fun `non-admin users don't see the users item`() = runComposeUiTest {
    val api = mock<API> { everySuspend { fetchInventoryItems() } calls { Success(emptyPage()) } }
    setContent {
      MainAppNavigation(
          api,
          UserAccountState(userHans),
          {},
      )
    }

    onNodeWithTag("navigate-to-Inventory").assertExists()
    onNodeWithTag("navigate-to-Users").assertDoesNotExist()
    onNodeWithTag("navigate-to-Account").assertExists()
  }
}
