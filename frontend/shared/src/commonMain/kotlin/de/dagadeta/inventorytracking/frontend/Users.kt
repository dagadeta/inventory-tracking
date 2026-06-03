package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.common.model.PageDto.Companion.emptyPage
import de.dagadeta.inventorytracking.common.model.UserDto
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.ApiResult

@Composable
fun Users(
    api: API,
    modifier: Modifier = Modifier,
) {
    var users by remember { mutableStateOf<PageDto<UserDto>>(emptyPage()) }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        when (val result = api.fetchUsers()) {
            is ApiResult.Success -> {
                users = result.data
                error = ""
            }

            is ApiResult.Error -> {
                users = emptyPage()
                error = "${result.message} (${result.code})"
            }

            is ApiResult.Unprivileged -> {
                users = emptyPage()
                error = "You are not allowed to browse all users."
            }
        }
    }
    Column(
        modifier = modifier
    ) {
        Text("Users", fontWeight = Bold)

        if (error.isNotEmpty()) {
            Text(error)
        }
        users.content.forEach {
            Text("${it.name}: ${it.role}")
        }
    }
}
