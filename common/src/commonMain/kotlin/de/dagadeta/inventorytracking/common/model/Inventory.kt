package de.dagadeta.inventorytracking.common.model

import kotlinx.serialization.Serializable

@Serializable
data class InventoryItemDto(
    val id: Long,
    val name: String,
    val totalAmount: Int,
    val lentAmount: Int,
    val remainingAmount: Int,
) {
    companion object {
        const val ENDPOINT = "/api/inventory/items"
    }
}
