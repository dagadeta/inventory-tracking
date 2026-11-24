package de.dagadeta.inventorytracking.backend

import de.dagadeta.inventorytracking.common.model.InventoryItemDto
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
import org.springframework.http.MediaType.TEXT_HTML
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

  @Test
  fun `users can be sorted by name`() {
    client
        .get()
        .uri("/api/users?sort=name,asc")
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
          assertThat(content).hasSizeGreaterThan(1)
          assertThat(content).isEqualTo(content.sortedBy(UserDto::name))
        }
  }

  @Test
  fun `inventory items can be sorted by totalAmount`() {
    client
        .get()
        .uri("/api/inventory/items?sort=totalAmount,asc")
        .accept(APPLICATION_JSON)
        .headers { it.setBasicAuth("Admin", "very secret") }
        .exchange()
        .expectStatus()
        .isOk
        .expectHeader()
        .contentType(APPLICATION_JSON)
        .expectBody<PageDto<InventoryItemDto>>()
        .consumeWith {
          val content = it.responseBody!!.content
          assertThat(content).hasSizeGreaterThan(1)
          assertThat(content).isEqualTo(content.sortedBy(InventoryItemDto::totalAmount))
        }
  }

  @Test
  fun `swagger SpringDoc is served without authentication`() {
    client
        .get()
        .uri("/swagger-ui/index.html")
        .accept(TEXT_HTML)
        .exchange()
        .expectStatus()
        .isOk
        .expectHeader()
        .contentType(TEXT_HTML)
        .expectBody()
        .toString()
        .contains("OpenAPI definition")

    client
        .get()
        .uri("/v3/api-docs")
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk
        .expectHeader()
        .contentType(APPLICATION_JSON)
        .expectBody()
        .toString()
        .contains("/api/inventory/items")

    client
        .get()
        .uri("/v3/api-docs.yaml")
        .exchange()
        .expectStatus()
        .isOk
        .expectHeader()
        .contentType("application/vnd.oai.openapi")
        .expectBody()
        .toString()
        .contains("/api/inventory/items")
  }
}
