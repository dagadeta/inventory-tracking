package de.dagadeta.inventorytracking.backend.rest

import de.dagadeta.inventorytracking.backend.persistance.InventoryItem
import de.dagadeta.inventorytracking.backend.persistance.InventoryRepository
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

@WebMvcTest(InventoryController::class)
@AutoConfigureMockMvc(addFilters = false)
class InventoryControllerTest {

    @MockitoBean
    private lateinit var inventoryRepository: InventoryRepository

    @Autowired
    private lateinit var webMvc: MockMvc

    private val inventoryURL = "/api/inventory/items"

    @Test
    fun `an empty inventory returns an empty page`() {
        inventoryRepository.stub { on { findAll(any<Pageable>()) } doReturn empty() }

        webMvc.get(inventoryURL)
            .andExpect { status { isOk() } }
            .andExpect { content { json("""{"content":[],"page":{"size":0,"number":0,"totalElements":0,"totalPages":1}}""") } }
    }

    @Test
    fun `available inventory items are listed`() {
        inventoryRepository.stub {
            on { findAll(any<Pageable>()) } doReturn PageImpl(
                listOf(
                    InventoryItem(1L, "Hammer", 1),
                    InventoryItem(2L, "Nail", 10),
                )
            )
        }

        webMvc.get(inventoryURL)
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(
                        """{
                        |   "content":[
                        |       {"id":1,"name":"Hammer","amount":1},
                        |       {"id":2,"name":"Nail","amount":10}
                        |   ],
                        |   "page":{"size":2,"number":0,"totalElements":2,"totalPages":1}
                        |}""".trimMargin()
                    )
                }
            }
    }
}
