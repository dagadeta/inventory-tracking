package de.dagadeta.inventorytracking.common.model

import kotlinx.serialization.Serializable


@Serializable
data class UserDto(val id: Long, val name: String, val role: UserRoleDto) {
    companion object {
        const val ENDPOINT = "/api/users"
    }
}

@Serializable
data class UserInfoDto(val id: Long, val name: String, val role: UserRoleDto) {
    companion object {
        const val ENDPOINT = "/api/user-info"
    }
}

@Serializable
enum class UserRoleDto {
    ADMIN, USER
}
