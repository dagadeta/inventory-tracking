package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.dagadeta.inventorytracking.common.model.UserInfoDto
import de.dagadeta.inventorytracking.frontend.http.*
import de.dagadeta.inventorytracking.frontend.http.ApiResult.*
import de.dagadeta.inventorytracking.frontend.http.Credentials.*
import kotlinx.coroutines.launch

@Composable
fun App() = ConnectedApp(HttpAPI())

@Composable
@Preview
fun PreviewApp() = ConnectedApp(fakeAPI)

@Composable
fun ConnectedApp(
    api: API,
) {
    val coroutineScope = rememberCoroutineScope()

    MaterialTheme {
        var credentials by remember { mutableStateOf<Credentials>(NoCredentials) }

        if (credentials !is UserInfo) {
            Login(
                credentials = credentials,
                onLoginClick = {
                    coroutineScope.launch {
                        credentials = when (val info = api.login(it)) {
                            is Error -> WrongCredentials(it, info.message)
                            is Success<UserInfoDto> -> info.toCredentials(it)
                            Unprivileged -> WrongCredentials(it, "Invalid user name or password.")
                        }
                    }
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainAppNavigation(api)
            }
        }
    }
}
