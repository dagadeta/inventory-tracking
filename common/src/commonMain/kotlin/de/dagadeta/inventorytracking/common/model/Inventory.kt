package de.dagadeta.inventorytracking.common.model

import kotlinx.serialization.Serializable

@Serializable
data class InventoryItemDto(val id: Long, val name: String, val amount: Int) {
    companion object {
        const val ENDPOINT = "/api/inventory/items"
    }
}
