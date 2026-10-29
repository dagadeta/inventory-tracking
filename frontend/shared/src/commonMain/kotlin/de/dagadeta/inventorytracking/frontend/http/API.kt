package de.dagadeta.inventorytracking.frontend.http

import de.dagadeta.inventorytracking.common.model.InventoryItemDto
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.common.model.UserDto
import de.dagadeta.inventorytracking.common.model.UserInfoDto
import de.dagadeta.inventorytracking.frontend.http.Credentials.InputCredentials
import io.ktor.client.request.*

interface API {
    suspend fun fetchUserInfo(): ApiResult<UserInfoDto>
    suspend fun fetchInventoryItems(): ApiResult<PageDto<InventoryItemDto>>
    suspend fun fetchUsers(): ApiResult<PageDto<UserDto>>
}

class HttpAPI(credentials: InputCredentials) : API {
    private val client = httpClient(credentials)

    override suspend fun fetchUserInfo(): ApiResult<UserInfoDto> = safeApiCall {
        client.get("http://localhost:8080${UserInfoDto.ENDPOINT}")
    }

    override suspend fun fetchInventoryItems(): ApiResult<PageDto<InventoryItemDto>> = safeApiCall {
        client.get("http://localhost:8080${InventoryItemDto.ENDPOINT}")
    }

    override suspend fun fetchUsers(): ApiResult<PageDto<UserDto>> = safeApiCall {
        client.get("http://localhost:8080${UserDto.ENDPOINT}")
    }
}
