package de.dagadeta.inventorytracking.backend.rest

import de.dagadeta.inventorytracking.backend.config.LoginUtilityService
import de.dagadeta.inventorytracking.backend.config.SecurityConfiguration
import de.dagadeta.inventorytracking.backend.persistance.InventoryUser
import de.dagadeta.inventorytracking.backend.persistance.UserRole.USER
import de.dagadeta.inventorytracking.backend.persistance.UsersRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(UserInfoController::class, LoginUtilityService::class, SecurityConfiguration::class)
class UserInfoControllerTest {

    @MockitoBean
    private lateinit var usersRepository: UsersRepository

    @Autowired
    private lateinit var webMvc: MockMvc

    private val userInfoURL = "/api/user-info"

    @BeforeEach
    fun initUser() {
        usersRepository.stub {
            on { findByName("User") } doReturn listOf(
                InventoryUser(2L, "User", "{noop}password", USER)
            )
        }
    }

    @Test
    fun `without credentials no user info`() {
        webMvc.get(userInfoURL)
            .andExpect { status { isUnauthorized() } }
    }

    @Test
    fun `with wrong credentials no user info`() {
        webMvc.get(userInfoURL) {
            with(httpBasic("User", "wrong password"))
        }
            .andExpect { status { isUnauthorized() } }
    }

    @Test
    fun `with right credentials the user info is returned`() {
        webMvc.get(userInfoURL) {
            with(httpBasic("User", "password"))
        }
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(
                        """{"id":2,"name":"User","role":"USER"}"""
                    )
                }
            }
    }
}
