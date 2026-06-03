package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import de.dagadeta.inventorytracking.common.model.InventoryItemDto
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.common.model.PageDto.Companion.emptyPage
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.ApiResult

@Composable
fun Inventory(
    api: API,
    modifier: Modifier = Modifier,
) {
    var items by remember { mutableStateOf<PageDto<InventoryItemDto>>(emptyPage()) }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        when (val result = api.fetchInventoryItems()) {
            is ApiResult.Success -> {
                items = result.data
                error = ""
            }

            is ApiResult.Error -> {
                items = emptyPage()
                error = "${result.message} (${result.code})"
            }

            is ApiResult.Unprivileged -> {
                items = emptyPage()
                error = "You are not allowed to browse the inventory."
            }
        }
    }
    Column(
        modifier = modifier,
    ) {
        Text("Inventory", fontWeight = Bold)

        if (error.isNotEmpty()) {
            Text(error)
        }
        items.content.forEach {
            Text("${it.name}: ${it.amount}")
        }
    }
}
