package de.dagadeta.inventorytracking.frontend.ui.users

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.dagadeta.inventorytracking.frontend.PageableList
import de.dagadeta.inventorytracking.frontend.http.API

@Composable
fun UsersScreen(
    api: API,
    modifier: Modifier,
) {
  PageableList(
      header = "Users",
      fetchItems = { api.fetchUsers() },
      describeItem = { "${it.name}: ${it.role}" },
      modifier = modifier,
  )
}
