package de.dagadeta.inventorytracking.frontend.http

import de.dagadeta.inventorytracking.common.model.UserInfoDto
import de.dagadeta.inventorytracking.common.model.UserRoleDto
import de.dagadeta.inventorytracking.frontend.http.ApiResult.Success
import de.dagadeta.inventorytracking.frontend.http.Credentials.InputCredentials
import de.dagadeta.inventorytracking.frontend.http.Credentials.UserInfo


sealed interface Credentials {
    object NoCredentials : Credentials
    data class InputCredentials(val userName: String, val password: String) : Credentials
    data class WrongCredentials(val inputCredentials: InputCredentials, val message: String) : Credentials
    data class UserInfo(val inputCredentials: InputCredentials, val role: UserRole) : Credentials
}

enum class UserRole {
    ADMIN, USER
}

fun Success<UserInfoDto>.toCredentials(input: InputCredentials) = UserInfo(input, data.role.toRole())
fun UserRoleDto.toRole() = when (this) {
    UserRoleDto.ADMIN -> UserRole.ADMIN
    UserRoleDto.USER -> UserRole.USER
}
