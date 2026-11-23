package de.dagadeta.inventorytracking.frontend

import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import de.dagadeta.inventorytracking.frontend.ui.inventory.InventoryItem
import de.dagadeta.inventorytracking.frontend.ui.users.Page
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class PageableListTest {

  @Test
  fun `renders the items using itemContent`() = runComposeUiTest {
    setContent {
      PageableList(
          header = "Header",
          loadItems = {},
          items = Page(listOf(InventoryItem(1, "item-1", 3, 2, 1))),
          error = "",
          itemContent = { Text("${it.name}: ${it.totalAmount}") },
      )
    }

    val rows = onNodeWithTag(listColumnTag).onChildren()
    rows[0].assertTextEquals("Header")
    rows[1].assertTextEquals("item-1: 3")
  }

  @Test
  fun `renders an error`() = runComposeUiTest {
    setContent {
      PageableList(
          header = "Header",
          loadItems = {},
          items = Page<InventoryItem>(),
          error = "error message (42)",
          itemContent = { Text("${it.name}: ${it.totalAmount}") },
      )
    }

    val rows = onNodeWithTag(listColumnTag).onChildren()
    rows[0].assertTextEquals("Header")
    rows[1].assertTextEquals("error message (42)")
  }
}
