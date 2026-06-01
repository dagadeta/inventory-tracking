package de.dagadeta.inventorytracking.backend.persistance

import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY
import java.time.LocalDateTime

@Entity
@Table(schema = "inventory", name = "item_lent")
data class InventoryItemLent(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long = 0,

    @ManyToOne
    @JoinColumn
    val item: InventoryItem,

    @ManyToOne
    @JoinColumn
    val lentBy: InventoryUser,
    val lentTo: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)

@Entity
@Table(schema = "inventory", name = "items")
data class InventoryItem(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long = 0,
    val name: String,
    val amount: Int,
)
