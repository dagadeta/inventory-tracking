package de.dagadeta.inventorytracking.backend.persistance

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(schema = "inventory", name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long,
    val username: String,
    val role: UserRole,
)

enum class UserRole {
    ADMIN, USER
}
