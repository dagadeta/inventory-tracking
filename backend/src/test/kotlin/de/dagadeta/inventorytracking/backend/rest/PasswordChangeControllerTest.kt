package de.dagadeta.inventorytracking.backend.rest

import de.dagadeta.inventorytracking.backend.config.LoginUtilityService
import de.dagadeta.inventorytracking.backend.config.SecurityConfiguration
import de.dagadeta.inventorytracking.backend.persistance.InventoryUser
import de.dagadeta.inventorytracking.backend.persistance.UserRole.USER
import de.dagadeta.inventorytracking.backend.persistance.UsersRepository
import kotlin.test.assertEquals
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(
    PasswordChangeController::class,
    LoginUtilityService::class,
    SecurityConfiguration::class,
)
class PasswordChangeControllerTest {

  @MockitoBean private lateinit var usersRepository: UsersRepository

  @Autowired private lateinit var webMvc: MockMvc

  private val changePasswordURL = "/api/change-password"

  @Test
  fun `without authorization should return 401 Unauthorized`() {
    webMvc.post(changePasswordURL).andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `with valid password should change password`() {
    usersRepository.stub {
      on { findByName(anyString()) } doReturn
          listOf(
              InventoryUser(1L, "User", "{noop}password", USER),
          )
    }
    webMvc
        .post(changePasswordURL) {
          with(httpBasic("User", "password"))
          contentType = MediaType.APPLICATION_JSON
          content = """{ "currentPassword": "password", "newPassword": "secret" }"""
        }
        .andExpect { status { isOk() } }

    val captor = argumentCaptor<InventoryUser>()
    verify(usersRepository).save(captor.capture())

    val userSaved = captor.firstValue
    assertEquals(1L, userSaved.id)
    assertEquals("User", userSaved.name)
    assertEquals(USER, userSaved.role)
    assertThat(userSaved.password).startsWith("{bcrypt}")
  }
}
