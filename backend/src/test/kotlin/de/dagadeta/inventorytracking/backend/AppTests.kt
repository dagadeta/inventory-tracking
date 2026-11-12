package de.dagadeta.inventorytracking.backend

import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.common.model.UserDto
import de.dagadeta.inventorytracking.common.model.UserRoleDto.ADMIN
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureEmbeddedDatabase
class AppTests {

  companion object {
    lateinit var client: RestTestClient

    @JvmStatic
    @BeforeAll
    fun initClient(@Value($$"${local.server.port}") port: Int) {
      client = RestTestClient.bindToServer().baseUrl("http://localhost:$port").build()
    }
  }

  @Test fun contextLoads() {}

  @Test
  fun `on first startup, a default admin user is created`() {
    client
        .get()
        .uri("/api/users")
        .accept(APPLICATION_JSON)
        .headers { it.setBasicAuth("Admin", "very secret") }
        .exchange()
        .expectStatus()
        .isOk
        .expectHeader()
        .contentType(APPLICATION_JSON)
        .expectBody<PageDto<UserDto>>()
        .consumeWith {
          val content = it.responseBody!!.content
          assertThat(content).hasSizeGreaterThan(0)
          assertThat(content).contains(UserDto(1L, "Admin", ADMIN))
        }
  }
}
