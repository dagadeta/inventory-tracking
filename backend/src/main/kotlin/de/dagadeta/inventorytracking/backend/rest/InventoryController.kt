package de.dagadeta.inventorytracking.backend.rest

import de.dagadeta.inventorytracking.backend.persistance.InventoryItem
import de.dagadeta.inventorytracking.backend.persistance.InventoryRepository
import de.dagadeta.inventorytracking.common.model.InventoryItemDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/inventory")
@RestController
class InventoryController(
    private val inventoryRepository: InventoryRepository,
) {
    @GetMapping("/items")
    fun getInventoryItems(@PageableDefault(page = 0, size = 50) pageable: Pageable): Page<InventoryItemDto> {
        return inventoryRepository.findAll(pageable).map { it.toDto() }
    }
}

fun InventoryItem.toDto() = InventoryItemDto(id, name, amount)
