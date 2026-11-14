package de.dagadeta.inventorytracking.common.model

import kotlin.collections.emptyList
import kotlinx.serialization.Serializable

@Serializable
data class PageDto<T>(val content: List<T>, val page: PageDetailsDto) {
  companion object {
    fun <T> emptyPage() = PageDto(emptyList<T>(), PageDetailsDto(0, 0, 0, 0))
  }
}

@Serializable
data class PageDetailsDto(
    val size: Int,
    val number: Int,
    val totalElements: Int,
    val totalPages: Int,
)
