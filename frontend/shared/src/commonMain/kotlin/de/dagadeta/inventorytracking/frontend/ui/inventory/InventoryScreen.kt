package de.dagadeta.inventorytracking.frontend.ui.inventory

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.dagadeta.inventorytracking.frontend.PageableList
import de.dagadeta.inventorytracking.frontend.ui.inventory.InventoryAction.FetchInventoryItems

@Composable
fun InventoryScreen(
    state: InventoryState,
    onAction: (InventoryAction) -> Unit,
    modifier: Modifier = Modifier,
) {
  PageableList(
      header = "Inventory",
      loadItems = { onAction(FetchInventoryItems) },
      items = state.items,
      error = state.errorText,
      itemContent = {
        Text(
            "${it.name} - total: ${it.totalAmount}, lent: ${it.lentAmount}, remaining: ${it.remainingAmount}",
        )
      },
      modifier = modifier,
  )
}
