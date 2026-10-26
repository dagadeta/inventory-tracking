package de.dagadeta.inventorytracking.frontend.http

import de.dagadeta.inventorytracking.common.model.InventoryItemDto
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.common.model.UserDto
import io.ktor.client.request.*

class API(credentials: Credentials) {
    private val client = httpClient(credentials)

    suspend fun fetchInventoryItems(): ApiResult<PageDto<InventoryItemDto>> = safeApiCall {
        client.get("http://localhost:8080${InventoryItemDto.ENDPOINT}")
    }

    suspend fun fetchUsers(): ApiResult<PageDto<UserDto>> = safeApiCall {
        client.get("http://localhost:8080${UserDto.ENDPOINT}")
    }
}
