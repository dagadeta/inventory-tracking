package de.dagadeta.inventorytracking.backend.config

import de.dagadeta.inventorytracking.backend.domain.InventoryService
import de.dagadeta.inventorytracking.backend.persistance.InventoryUser
import de.dagadeta.inventorytracking.backend.persistance.UserRole.ADMIN
import de.dagadeta.inventorytracking.backend.persistance.UserRole.USER
import de.dagadeta.inventorytracking.backend.persistance.UsersRepository
import de.dagadeta.inventorytracking.backend.rest.InventoryController
import de.dagadeta.inventorytracking.backend.rest.UsersController
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(
    SecurityConfiguration::class,
    InventoryController::class,
    UsersController::class,
    LoginUtilityService::class
)
class SecurityConfigurationTest {

    @MockitoBean
    private lateinit var usersRepository: UsersRepository

    @MockitoBean
    private lateinit var inventoryService: InventoryService

    @Autowired
    private lateinit var webMvc: MockMvc

    private val inventoryURL = "/api/inventory/items"
    private val usersURL = "/api/users"

    @Test
    fun `without authorization should return 401 Unauthorized`() {
        webMvc.get(inventoryURL)
            .andExpect { status { isUnauthorized() } }
    }

    @Test
    fun `with invalid password should return 401 Unauthorized`() {
        webMvc.get(inventoryURL) { with(httpBasic("User", "wrongPassword")) }
            .andExpect { status { isUnauthorized() } }
    }

    @Test
    fun `with valid password should return 200 Authorized`() {
        usersRepository.stub {
            on { findByName(anyString()) } doReturn listOf(
                InventoryUser(1L, "User", "{noop}password", USER)
            )
        }
        inventoryService.stub {
            on { allInventoryItemAmounts(Pageable.ofSize(50)) } doReturn Page.empty()
        }
        webMvc.get(inventoryURL) { with(httpBasic("User", "password")) }
            .andExpect { status { isOk() } }
    }

    @Test
    fun `only admin users are allowed to query all users`() {
        usersRepository.stub {
            on { findByName("Admin") } doReturn listOf(
                InventoryUser(1L, "Admin", "{noop}adminPassword", ADMIN)
            )
            on { findByName("User") } doReturn listOf(
                InventoryUser(2L, "User", "{noop}userPassword", USER)
            )
            on { findAll(any(Pageable::class.java)) } doReturn Page.empty()
        }

        webMvc.get(usersURL) { with(httpBasic("User", "userPassword")) }
            .andExpect { status { isForbidden() } }
        webMvc.get(usersURL) { with(httpBasic("Admin", "adminPassword")) }
            .andExpect { status { isOk() } }
    }
}
