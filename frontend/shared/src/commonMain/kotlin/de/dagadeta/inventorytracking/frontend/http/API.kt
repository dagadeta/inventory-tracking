package de.dagadeta.inventorytracking.frontend.http

import de.dagadeta.inventorytracking.common.model.*
import de.dagadeta.inventorytracking.common.model.UserRoleDto.ADMIN
import de.dagadeta.inventorytracking.frontend.http.ApiResult.Success
import de.dagadeta.inventorytracking.frontend.http.Credentials.InputCredentials
import io.ktor.client.*
import io.ktor.client.request.*

interface API {
    suspend fun login(credentials: InputCredentials): ApiResult<UserInfoDto>
    suspend fun fetchInventoryItems(): ApiResult<PageDto<InventoryItemDto>>
    suspend fun fetchUsers(): ApiResult<PageDto<UserDto>>
}

class HttpAPI : API {
    private lateinit var client: HttpClient

    override suspend fun login(credentials: InputCredentials): ApiResult<UserInfoDto> {
        client = httpClient(credentials)
        return safeApiCall {
            client.get("http://localhost:8080${UserInfoDto.ENDPOINT}")
        }
    }

    override suspend fun fetchInventoryItems(): ApiResult<PageDto<InventoryItemDto>> = safeApiCall {
        client.get("http://localhost:8080${InventoryItemDto.ENDPOINT}")
    }

    override suspend fun fetchUsers(): ApiResult<PageDto<UserDto>> = safeApiCall {
        client.get("http://localhost:8080${UserDto.ENDPOINT}")
    }
}

// used for previews and tests
val fakeAPI: API = object : API {
    override suspend fun login(credentials: InputCredentials): ApiResult<UserInfoDto> =
        Success(UserInfoDto(1, "admin", ADMIN))

    override suspend fun fetchInventoryItems(): ApiResult<PageDto<InventoryItemDto>> =
        Success(pageOf(InventoryItemDto(1, "item-1", 1, 0, 1)))

    override suspend fun fetchUsers(): ApiResult<PageDto<UserDto>> =
        Success(pageOf(UserDto(1, "admin", ADMIN)))
}

private fun <T> pageOf(vararg items: T) =
    PageDto(items.toList(), PageDetailsDto(items.size, 1, items.size, 1))
