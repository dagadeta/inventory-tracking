package de.dagadeta.inventorytracking.frontend

import androidx.compose.ui.semantics.SemanticsProperties.StateDescription
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher.Companion.expectValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import de.dagadeta.inventorytracking.frontend.ui.inventory.InventoryState
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserAccountState
import de.dagadeta.inventorytracking.frontend.ui.users.UsersState
import kotlin.test.Test
import kotlinx.coroutines.test.TestResult

@OptIn(ExperimentalTestApi::class)
class MainAppNavigationTest {

  @Test
  fun `navigation rail can be expanded and collapsed`(): TestResult {
    return runComposeUiTest {
      setContent {
        MainAppNavigation(
            UserAccountState(userHans),
            {},
            InventoryState(),
            {},
            UsersState(),
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
          UserAccountState(admin),
          {},
          InventoryState(),
          {},
          UsersState(),
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
          UserAccountState(admin),
          {},
          InventoryState(),
          {},
          UsersState(),
          {},
      )
    }

    onNodeWithTag("navigate-to-Inventory").assertExists()
    onNodeWithTag("navigate-to-Users").assertExists()
    onNodeWithTag("navigate-to-Account").assertExists()
  }

  @Test
  fun `non-admin users don't see the users item`() = runComposeUiTest {
    setContent {
      MainAppNavigation(
          UserAccountState(userHans),
          {},
          InventoryState(),
          {},
          UsersState(),
          {},
      )
    }

    onNodeWithTag("navigate-to-Inventory").assertExists()
    onNodeWithTag("navigate-to-Users").assertDoesNotExist()
    onNodeWithTag("navigate-to-Account").assertExists()
  }
}
