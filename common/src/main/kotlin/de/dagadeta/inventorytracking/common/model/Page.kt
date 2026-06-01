package de.dagadeta.inventorytracking.common.model

data class PageDto<T>(val content: List<T>, val page: PageDetailsDto)

data class PageDetailsDto(val size: Int, val number: Int, val totalElements: Int, val totalPages: Int)
