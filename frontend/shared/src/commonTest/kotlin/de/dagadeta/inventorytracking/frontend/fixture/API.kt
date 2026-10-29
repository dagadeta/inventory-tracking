package de.dagadeta.inventorytracking.frontend.fixture

import de.dagadeta.inventorytracking.common.model.*
import de.dagadeta.inventorytracking.common.model.UserRoleDto.ADMIN
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.ApiResult
import de.dagadeta.inventorytracking.frontend.http.ApiResult.Success

val fakeAPI: API = object : API {
    override suspend fun fetchUserInfo(): ApiResult<UserInfoDto> =
        Success(UserInfoDto(1, "admin", ADMIN))

    override suspend fun fetchInventoryItems(): ApiResult<PageDto<InventoryItemDto>> =
        Success(pageOf(InventoryItemDto(1, "item-1", 1)))

    override suspend fun fetchUsers(): ApiResult<PageDto<UserDto>> =
        Success(pageOf(UserDto(1, "admin", ADMIN)))
}

private fun <T> pageOf(vararg items: T) =
    PageDto(items.toList(), PageDetailsDto(items.size, 1, items.size, 1))
