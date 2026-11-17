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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import de.dagadeta.inventorytracking.frontend.UserAccountAction.LoginAction
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.Credentials.UserInfo
import de.dagadeta.inventorytracking.frontend.http.HttpAPI
import de.dagadeta.inventorytracking.frontend.http.fakeAPI
import de.dagadeta.inventorytracking.frontend.theme.AppTheme

@Composable fun App() = ConnectedApp(HttpAPI())

@Composable @Preview fun PreviewApp() = ConnectedApp(fakeAPI)

@Composable
fun ConnectedApp(
    api: API,
) {
  val userAccountViewModel = viewModel { UserAccountViewModel(api) }
  val userAccountState by userAccountViewModel.state.collectAsState()

  AppTheme {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
      if (!userAccountState.isLoggedIn) {
        Login(
            credentials = userAccountState.credentials,
            onLoginClick = { userAccountViewModel.onAction(LoginAction(it)) },
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
              api = api,
              role = (userAccountState.credentials as UserInfo).role,
              userAccountViewModel = userAccountViewModel,
          )
        }
      }
    }
  }
}
