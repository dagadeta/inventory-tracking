package de.dagadeta.inventorytracking.backend.init

import de.dagadeta.inventorytracking.backend.persistance.InventoryUser
import de.dagadeta.inventorytracking.backend.persistance.UserRole.ADMIN
import de.dagadeta.inventorytracking.backend.persistance.UsersRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class InitializeUsers(
    private val usersRepository: UsersRepository,
    private val passwordEncoder: PasswordEncoder,
) {
  private val log = KotlinLogging.logger {}

  @EventListener(ApplicationReadyEvent::class)
  fun initializeUsers() {
    if (usersRepository.count() > 0L) return

    log.info { "Initializing admin user" }
    val password = passwordEncoder.encode("very secret")!!
    usersRepository.save(InventoryUser(name = "Admin", password = password, role = ADMIN))
  }
}
