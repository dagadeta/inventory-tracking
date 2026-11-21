package de.dagadeta.inventorytracking.frontend.ui.users

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.dagadeta.inventorytracking.frontend.PageableList
import de.dagadeta.inventorytracking.frontend.ui.users.UsersAction.FetchUsers

@Composable
fun UsersScreen(
    state: UsersState,
    onAction: (UsersAction) -> Unit,
    modifier: Modifier,
) {
  PageableList(
      header = "Users",
      loadItems = { onAction(FetchUsers) },
      items = state.users,
      error = state.errorText,
      itemContent = { Text("${it.name}: ${it.role}") },
      modifier = modifier,
  )
}
