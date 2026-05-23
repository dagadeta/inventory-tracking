package de.dagadeta.inventorytracking.common.model


data class UserDto(val id: Long, val name: String, val role: UserRoleDto)

enum class UserRoleDto {
    ADMIN, USER
}
