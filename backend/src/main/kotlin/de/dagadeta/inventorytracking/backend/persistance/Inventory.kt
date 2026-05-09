package de.dagadeta.inventorytracking.backend.persistance

import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY
import java.time.LocalDateTime

@Entity
@Table(schema = "inventory", name = "item_lent")
data class InventoryItemLent(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long,

    @ManyToOne
    @JoinColumn
    val item: InventoryItem,

    @ManyToOne
    @JoinColumn
    val lentBy: User,
    val lentTo: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)

@Entity
@Table(schema = "inventory", name = "items")
data class InventoryItem(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long,
    val name: String,
    val amount: Int,
)
