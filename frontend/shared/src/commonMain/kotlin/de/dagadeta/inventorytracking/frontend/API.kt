package de.dagadeta.inventorytracking.frontend

import de.dagadeta.inventorytracking.common.model.InventoryItemDto
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.common.model.UserDto
import io.ktor.client.call.*
import io.ktor.client.request.*

class API {
    private val client = httpClient()

    suspend fun fetchInventoryItems(): PageDto<InventoryItemDto> {
        return client.get("http://localhost:8080${InventoryItemDto.ENDPOINT}").body()
    }

    suspend fun fetchUsers(): PageDto<UserDto> {
        return client.get("http://localhost:8080${UserDto.ENDPOINT}").body()
    }
}
