package de.dagadeta.inventorytracking.common.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val timestamp: String,
    val status: Int,
    val error: String,
    val path: String,
)
