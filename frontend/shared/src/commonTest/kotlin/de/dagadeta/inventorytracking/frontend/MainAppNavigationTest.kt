package de.dagadeta.inventorytracking.frontend

import androidx.compose.ui.semantics.SemanticsProperties.StateDescription
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher.Companion.expectValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import de.dagadeta.inventorytracking.frontend.fixture.fakeAPI
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MainAppNavigationTest {

    @Test
    fun `navigation rail can be expanded and collapsed`() = runComposeUiTest {
        setContent {
            MainAppNavigation(fakeAPI)
        }

        onNodeWithTag(expandButtonTag).assert(expectValue(StateDescription, "Collapsed"))
        onNodeWithTag(expandButtonTag).performClick()
        onNodeWithTag(expandButtonTag).assert(expectValue(StateDescription, "Expanded"))
        onNodeWithTag(expandButtonTag).performClick()
        onNodeWithTag(expandButtonTag).assert(expectValue(StateDescription, "Collapsed"))
    }

    @Test
    fun `can switch between inventory and users list`() = runComposeUiTest {
        setContent {
            MainAppNavigation(fakeAPI)
        }

        onNodeWithTag(inventoryListTag).assertExists()
        onNodeWithTag(usersListTag).assertDoesNotExist()

        onNodeWithTag("navigate-to-Users").performClick()

        onNodeWithTag(inventoryListTag).assertDoesNotExist()
        onNodeWithTag(usersListTag).assertExists()

        onNodeWithTag("navigate-to-Inventory").performClick()

        onNodeWithTag(inventoryListTag).assertExists()
        onNodeWithTag(usersListTag).assertDoesNotExist()
    }
}
