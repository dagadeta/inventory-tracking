package de.dagadeta.inventorytracking.common.model

import de.dagadeta.inventorytracking.common.model.UserRoleDto.NONE
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
    val NO_USER = UserInfoDto(0, "", NONE)
    const val ENDPOINT = "/api/user-info"
  }
}

@Serializable
enum class UserRoleDto {
  ADMIN,
  USER,
  NONE,
}
