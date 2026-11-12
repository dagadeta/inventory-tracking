package de.dagadeta.inventorytracking.backend.rest

import de.dagadeta.inventorytracking.backend.persistance.InventoryUser
import de.dagadeta.inventorytracking.backend.persistance.UserRole.ADMIN
import de.dagadeta.inventorytracking.backend.persistance.UserRole.USER
import de.dagadeta.inventorytracking.backend.persistance.UsersRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.data.domain.Page.empty
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(UsersController::class)
@AutoConfigureMockMvc(addFilters = false)
class UsersControllerTest {

  @MockitoBean private lateinit var usersRepository: UsersRepository

  @Autowired private lateinit var webMvc: MockMvc

  private val usersURL = "/api/users"

  @Test
  fun `without a user the response is empty`() {
    usersRepository.stub { on { findAll(any<Pageable>()) } doReturn empty() }

    webMvc
        .get(usersURL)
        .andExpect { status { isOk() } }
        .andExpect {
          content {
            json("""{"content":[],"page":{"size":0,"number":0,"totalElements":0,"totalPages":1}}""")
          }
        }
  }

  @Test
  fun `available users are listed`() {
    usersRepository.stub {
      on { findAll(any<Pageable>()) } doReturn
          PageImpl(
              listOf(
                  InventoryUser(1L, "Admin", "{noop}password", ADMIN),
                  InventoryUser(2L, "User", "{noop}password", USER),
              ),
          )
    }

    webMvc
        .get(usersURL)
        .andExpect { status { isOk() } }
        .andExpect {
          content {
            json(
                """
                |{
                |   "content":[
                |       {"id":1,"name":"Admin","role":"ADMIN"},
                |       {"id":2,"name":"User","role":"USER"}
                |   ],
                |   "page":{"size":2,"number":0,"totalElements":2,"totalPages":1}
                |}
                """
                    .trimMargin(),
            )
          }
        }
  }
}
