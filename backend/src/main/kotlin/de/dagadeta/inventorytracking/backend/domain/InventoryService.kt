package de.dagadeta.inventorytracking.backend.domain

import de.dagadeta.inventorytracking.backend.domain.model.InventoryItemAmounts
import de.dagadeta.inventorytracking.backend.persistance.InventoryLentRepository
import de.dagadeta.inventorytracking.backend.persistance.InventoryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class InventoryService(
    private val inventoryRepository: InventoryRepository,
    private val inventoryLentRepository: InventoryLentRepository,
) {
    fun allInventoryItemAmounts(pageable: Pageable): Page<InventoryItemAmounts> =
        inventoryRepository.findAll(pageable).map { item ->
            InventoryItemAmounts(
                item.id,
                item.name,
                totalAmount = item.amount,
                lentAmount = inventoryLentRepository.countByItem(item),
            )
        }
}
