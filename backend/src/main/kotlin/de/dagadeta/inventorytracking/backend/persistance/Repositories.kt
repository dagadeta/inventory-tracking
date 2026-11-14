package de.dagadeta.inventorytracking.backend.persistance

import org.springframework.data.jpa.repository.JpaRepository

interface InventoryRepository : JpaRepository<InventoryItem, Long>

interface InventoryLentRepository : JpaRepository<InventoryItemLent, Long> {
  fun countByItem(item: InventoryItem): Int
}

interface UsersRepository : JpaRepository<InventoryUser, Long> {
  fun findByName(name: String?): List<InventoryUser>
}
