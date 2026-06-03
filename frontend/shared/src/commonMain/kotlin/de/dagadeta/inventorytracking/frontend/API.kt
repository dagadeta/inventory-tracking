package de.dagadeta.inventorytracking.frontend

import de.dagadeta.inventorytracking.common.model.InventoryItemDto
import de.dagadeta.inventorytracking.common.model.PageDto
import io.ktor.client.call.*
import io.ktor.client.request.*

class API {
    private val client = httpClient()

    suspend fun fetchInventoryItems(): PageDto<InventoryItemDto> {
        return client.get("http://localhost:8080/api/inventory/items").body()
    }
}
