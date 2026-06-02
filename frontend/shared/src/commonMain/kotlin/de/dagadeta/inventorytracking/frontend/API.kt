package de.dagadeta.inventorytracking.frontend

import de.dagadeta.inventorytracking.common.model.InventoryItemDto
import de.dagadeta.inventorytracking.common.model.PageDto
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class API {
    private val client = httpClient()

    suspend fun fetchInventoryItems(): PageDto<InventoryItemDto> {
        val response = client.get("http://localhost:8080/api/inventory/items") {
            accept(ContentType.Application.Json)
            basicAuth("Default User", "secret")
        }
        return response.body<PageDto<InventoryItemDto>>()
    }
}
