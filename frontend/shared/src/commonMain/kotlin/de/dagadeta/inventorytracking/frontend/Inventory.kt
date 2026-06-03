package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import de.dagadeta.inventorytracking.common.model.InventoryItemDto
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.common.model.PageDto.Companion.emptyPage

@Composable
fun Inventory(api: API, modifier: Modifier = Modifier) {
    val items = remember { mutableStateOf<PageDto<InventoryItemDto>>(emptyPage()) }
    LaunchedEffect(true) {
        items.value = api.fetchInventoryItems()
    }
    Column(
        modifier = modifier,
    ) {
        items.value.content.forEach {
            Text("${it.name}: ${it.amount}")
        }
    }
}
