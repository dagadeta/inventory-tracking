package de.dagadeta.inventorytracking.backend.persistance

import de.dagadeta.inventorytracking.backend.persistance.UserRole.ADMIN
import de.dagadeta.inventorytracking.backend.persistance.UserRole.USER
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.InvalidDataAccessApiUsageException
import java.time.LocalDateTime.now

@DataJpaTest
@AutoConfigureEmbeddedDatabase
class DatabaseIntegrationTest {

    @Autowired
    lateinit var usersRepository: UsersRepository

    @Autowired
    lateinit var inventoryRepository: InventoryRepository

    @Autowired
    lateinit var inventoryLentRepository: InventoryLentRepository

    @Test
    fun `on empty database nothing is found`() {
        assertThat(usersRepository.findAll()).isEmpty()
        assertThat(inventoryRepository.findAll()).isEmpty()
        assertThat(inventoryLentRepository.findAll()).isEmpty()
    }

    @Test
    fun `a saved user can be read`() {
        usersRepository.save(InventoryUser(name = "Someone", password = "", role = ADMIN))

        usersRepository.findAll().single().let {
            assertThat(it.id).isNotEqualTo(0)
            assertThat(it.name).isEqualTo("Someone")
            assertThat(it.role).isEqualTo(ADMIN)
        }
    }

    @Test
    fun `user names must be unique`() {
        usersRepository.save(InventoryUser(name = "Someone", password = "", role = USER))

        assertThatThrownBy {
            usersRepository.save(InventoryUser(name = "Someone", password = "", role = USER))
        }.isInstanceOf(DataIntegrityViolationException::class.java)
            .hasMessageContaining("duplicate key value violates unique constraint")
            .hasMessageContaining("Detail: Key (name)=(Someone) already exists.")
    }

    @Test
    fun `a saved inventory item can be read`() {
        inventoryRepository.save(InventoryItem(name = "Cooling pack", amount = 10))

        inventoryRepository.findAll().single().let {
            assertThat(it.id).isNotEqualTo(0)
            assertThat(it.name).isEqualTo("Cooling pack")
            assertThat(it.amount).isEqualTo(10)
        }
    }

    @Test
    fun `a saved inventory item lent needs referenced objects to be saved before`() {
        val startTime = now()
        val user = InventoryUser(name = "Someone else", password = "", role = USER)
        val item = InventoryItem(name = "Cooling pack", amount = 1)

        val savedUser = usersRepository.save(user)
        val savedItem = inventoryRepository.save(item)
        val lent = InventoryItemLent(item = savedItem, lentBy = savedUser, lentTo = "Freddie Mercury")
        inventoryLentRepository.save(lent)

        inventoryLentRepository.findAll().single().let {
            assertThat(it.id).isNotEqualTo(0)
            assertThat(it.lentTo).isEqualTo("Freddie Mercury")
            assertThat(it.lentBy).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(user)
            assertThat(it.item).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(item)
            assertThat(it.timestamp).isBetween(startTime, now())
        }
    }

    @Test
    fun `an InventoryUser cannot be deleted as long as an InventoryItemLent references it`() {
        val user = InventoryUser(name = "Someone else", password = "", role = USER)
        val item = InventoryItem(name = "Cooling pack", amount = 1)

        val savedUser = usersRepository.save(user)
        val savedItem = inventoryRepository.save(item)
        val lent = InventoryItemLent(item = savedItem, lentBy = savedUser, lentTo = "Freddie Mercury")
        inventoryLentRepository.save(lent)

        val exception = assertThrows<InvalidDataAccessApiUsageException> {
            usersRepository.deleteById(savedUser.id)
            usersRepository.flush()
        }
        assertThat(exception.message).contains("Persistent instance of 'de.dagadeta.inventorytracking.backend.persistance.InventoryItemLent' references an unsaved transient instance of 'de.dagadeta.inventorytracking.backend.persistance.InventoryUser'")
    }

    @Test
    fun `an InventoryItem cannot be deleted as long as an InventoryItemLent references it`() {
        val user = InventoryUser(name = "Someone else", password = "", role = USER)
        val item = InventoryItem(name = "Cooling pack", amount = 1)

        val savedUser = usersRepository.save(user)
        val savedItem = inventoryRepository.save(item)
        val lent = InventoryItemLent(item = savedItem, lentBy = savedUser, lentTo = "Freddie Mercury")
        inventoryLentRepository.save(lent)

        val exception = assertThrows<InvalidDataAccessApiUsageException> {
            inventoryRepository.deleteById(savedItem.id)
            inventoryRepository.flush()
        }
        assertThat(exception.message).contains("Persistent instance of 'de.dagadeta.inventorytracking.backend.persistance.InventoryItemLent' references an unsaved transient instance of 'de.dagadeta.inventorytracking.backend.persistance.InventoryItem'")
    }
}
