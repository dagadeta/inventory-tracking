package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.HttpAPI
import de.dagadeta.inventorytracking.frontend.theme.AppTheme
import de.dagadeta.inventorytracking.frontend.ui.inventory.InventoryViewModel
import de.dagadeta.inventorytracking.frontend.ui.useraccount.Login
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserAccountViewModel
import de.dagadeta.inventorytracking.frontend.ui.users.UsersViewModel

@Composable fun App() = ConnectedApp(HttpAPI())

@Composable
fun ConnectedApp(
    api: API,
    userAccountViewModel: UserAccountViewModel = viewModel { UserAccountViewModel(api) },
    inventoryViewModel: InventoryViewModel = viewModel { InventoryViewModel(api) },
    usersViewModel: UsersViewModel = viewModel { UsersViewModel(api) },
) {
  val userAccountState by userAccountViewModel.state.collectAsState()
  val inventoryState by inventoryViewModel.state.collectAsState()
  val usersState by usersViewModel.state.collectAsState()

  AppTheme {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
      if (!userAccountState.isLoggedIn) {
        Login(
            credentials = userAccountState.credentials,
            onAction = userAccountViewModel::onAction,
        )
      } else {
        Column(
            modifier =
                Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          MainAppNavigation(
              userAccountState = userAccountState,
              onUserAccountAction = userAccountViewModel::onAction,
              inventoryState = inventoryState,
              onInventoryAction = inventoryViewModel::onAction,
              usersState = usersState,
              onUsersAction = usersViewModel::onAction,
          )
        }
      }
    }
  }
}
