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
import de.dagadeta.inventorytracking.frontend.http.ApiResult.*
import de.dagadeta.inventorytracking.frontend.http.Credentials
import de.dagadeta.inventorytracking.frontend.http.Credentials.*
import de.dagadeta.inventorytracking.frontend.http.HttpAPI
import de.dagadeta.inventorytracking.frontend.http.toCredentials
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    MaterialTheme {
        var credentials by remember { mutableStateOf<Credentials>(NoCredentials) }

        if (credentials !is UserInfo) {
            Login(
                credentials = credentials,
                onLoginClick = {
                    MainScope().launch {
                        val api = HttpAPI(it)
                        credentials = when (val info = api.fetchUserInfo()) {
                            is Error -> WrongCredentials(info.message)
                            is Success<UserInfoDto> -> info.toCredentials(it)
                            Unprivileged -> WrongCredentials("Invalid user name or password.")
                        }
                    }
                }
            )
        } else {
            // TODO don't create a new instance on each recompose
            val api = HttpAPI((credentials as UserInfo).inputCredentials)
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
