package de.dagadeta.inventorytracking.frontend.ui.inventory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.dagadeta.inventorytracking.frontend.PageableList
import de.dagadeta.inventorytracking.frontend.http.API

@Composable
fun InventoryScreen(
    api: API,
    modifier: Modifier = Modifier,
) {
  PageableList(
      header = "Inventory",
      fetchItems = { api.fetchInventoryItems() },
      describeItem = {
        "${it.name} - total: ${it.totalAmount}, lent: ${it.lentAmount}, remaining: ${it.remainingAmount}"
      },
      modifier = modifier,
  )
}
