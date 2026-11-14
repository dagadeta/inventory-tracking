package de.dagadeta.inventorytracking.common.model

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordDto(val currentPassword: String, val newPassword: String) {
  companion object {
    const val ENDPOINT = "/api/change-password"
  }
}
