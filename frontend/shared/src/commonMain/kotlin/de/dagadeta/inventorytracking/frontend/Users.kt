package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.common.model.PageDto.Companion.emptyPage
import de.dagadeta.inventorytracking.common.model.UserDto

@Composable
fun Users(api: API, modifier: Modifier = Modifier) {
    val users = remember { mutableStateOf<PageDto<UserDto>>(emptyPage()) }
    LaunchedEffect(true) {
        users.value = api.fetchUsers()
    }
    Column(
        modifier = modifier
    ) {
        users.value.content.forEach {
            Text("${it.name}: ${it.role}")
        }
    }
}
