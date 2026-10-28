package de.dagadeta.inventorytracking.frontend

import androidx.compose.ui.semantics.SemanticsProperties.StateDescription
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher.Companion.expectValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import de.dagadeta.inventorytracking.common.model.InventoryItemDto
import de.dagadeta.inventorytracking.common.model.PageDetailsDto
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.common.model.UserDto
import de.dagadeta.inventorytracking.common.model.UserRoleDto.ADMIN
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.ApiResult
import de.dagadeta.inventorytracking.frontend.http.ApiResult.Success
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

private val fakeAPI: API = object : API {
    override suspend fun fetchInventoryItems(): ApiResult<PageDto<InventoryItemDto>> =
        Success(pageOf(InventoryItemDto(1, "item-1", 1)))

    override suspend fun fetchUsers(): ApiResult<PageDto<UserDto>> =
        Success(pageOf(UserDto(1, "admin", ADMIN)))
}

private fun <T> pageOf(vararg items: T) =
    PageDto(items.toList(), PageDetailsDto(items.size, 1, items.size, 1))
