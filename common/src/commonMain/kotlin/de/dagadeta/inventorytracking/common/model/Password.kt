package de.dagadeta.inventorytracking.common.model

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequestDto(val currentPassword: String, val newPassword: String) {
  companion object {
    const val ENDPOINT = "/api/change-password"
  }
}

@Serializable data class ChangePasswordResponseDto(val result: PasswordChangeResult)

enum class PasswordChangeResult {
  SUCCESS,
  WRONG_PASSWORD,
}
