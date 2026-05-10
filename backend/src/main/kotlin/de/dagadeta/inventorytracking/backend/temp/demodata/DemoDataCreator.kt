package de.dagadeta.inventorytracking.backend.temp.demodata

import de.dagadeta.inventorytracking.backend.persistance.*
import de.dagadeta.inventorytracking.backend.persistance.UserRole.ADMIN
import de.dagadeta.inventorytracking.backend.persistance.UserRole.USER
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class DemoDataCreator(
    private val usersRepository: UsersRepository,
    private val inventoryRepository: InventoryRepository,
    private val inventoryLentRepository: InventoryLentRepository,
) {
    private val log = KotlinLogging.logger {}

    @EventListener(ApplicationReadyEvent::class)
    fun createDemoData() {
        if (usersRepository.count() > 0L) return

        log.info { "Creating demo data" }
        usersRepository.save(InventoryUser(name = "Admin", password = "very secret", role = ADMIN))
        val user = usersRepository.save(InventoryUser(name = "Default User", password = "secret", role = USER))

        val coolingPack = inventoryRepository.save(InventoryItem(name = "Cooling Pack", amount = 10))
        val walkingAid = inventoryRepository.save(InventoryItem(name = "Walking Aid", amount = 4))

        inventoryLentRepository.save(InventoryItemLent(item = coolingPack, lentBy = user, lentTo = "Some Person"))
        inventoryLentRepository.save(InventoryItemLent(item = walkingAid, lentBy = user, lentTo = "Someone Else"))
    }
}
