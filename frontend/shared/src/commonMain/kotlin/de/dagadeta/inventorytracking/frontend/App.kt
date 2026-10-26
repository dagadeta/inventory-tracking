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
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.Credentials

@Composable
@Preview
fun App() {
    MaterialTheme {
        var credentials by remember { mutableStateOf(Credentials.EMPTY) }

        if (credentials.isIncomplete()) {
            Login(
                onLoginClick = {
                    credentials = it
                }
            )
        } else {
            // TODO don't create a new instance on each recompose
            val api = API(credentials)
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
