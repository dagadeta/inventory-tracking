package de.dagadeta.inventorytracking.frontend

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import de.dagadeta.inventorytracking.common.model.InventoryItemDto
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.frontend.http.ApiResult
import de.dagadeta.inventorytracking.frontend.http.fakeAPI
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class PageableListTest {

    @Test
    fun `renders the fetchItems result using describeItem`() = runComposeUiTest {
        setContent {
            PageableList(
                header = "Header",
                fetchItems = {
                    fakeAPI.fetchInventoryItems()
                },
                describeItem = { "${it.name}: ${it.totalAmount}" },
            )
        }

        val rows = onNodeWithTag(listColumnTag).onChildren()
        rows[0].assertTextEquals("Header")
        rows[1].assertTextEquals("item-1: 1")
    }

    @Test
    fun `renders an error if fetchItems fails`() = runComposeUiTest {
        setContent {
            PageableList(
                header = "Header",
                fetchItems = {
                    ApiResult.Error("error message", 42) as ApiResult<PageDto<InventoryItemDto>>
                },
                describeItem = { "${it.name}: ${it.totalAmount}" },
            )
        }

        val rows = onNodeWithTag(listColumnTag).onChildren()
        rows[0].assertTextEquals("Header")
        rows[1].assertTextEquals("error message (42)")
    }
}
