package de.dagadeta.inventorytracking.backend.rest

import de.dagadeta.inventorytracking.backend.domain.InventoryService
import de.dagadeta.inventorytracking.backend.domain.model.InventoryItemAmounts
import de.dagadeta.inventorytracking.common.model.InventoryItemDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping(InventoryItemDto.ENDPOINT)
@RestController
class InventoryController(
    private val inventoryService: InventoryService,
) {
    @GetMapping
    fun getInventoryItems(@PageableDefault(page = 0, size = 50) pageable: Pageable): Page<InventoryItemDto> {
        return inventoryService.allInventoryItemAmounts(pageable).map { it.toDto() }
    }
}

fun InventoryItemAmounts.toDto() = InventoryItemDto(id, name, totalAmount, lentAmount, remainingAmount)
