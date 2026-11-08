package de.dagadeta.inventorytracking.backend.domain.model

data class InventoryItemAmounts(
    var id: Long = 0,
    val name: String,
    val totalAmount: Int,
    val lentAmount: Int = 0,
    val remainingAmount: Int = totalAmount - lentAmount,
)
