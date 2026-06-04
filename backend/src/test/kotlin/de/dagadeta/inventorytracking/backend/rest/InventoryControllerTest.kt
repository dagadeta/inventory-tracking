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
import org.springframework.data.domain.PageRequest
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

    @Test
    fun `inventory items get paged according to the request`() {
        inventoryRepository.stub {
            on { findAll(PageRequest.of(0, 2)) } doReturn PageImpl(
                listOf(
                    InventoryItem(1L, "1", 1),
                    InventoryItem(2L, "2", 2),
                ),
                PageRequest.of(0, 2),
                4,
            )
            on { findAll(PageRequest.of(1, 2)) } doReturn PageImpl(
                listOf(
                    InventoryItem(3L, "3", 3),
                    InventoryItem(4L, "4", 4),
                ),
                PageRequest.of(1, 2),
                4,
            )
        }

        webMvc.get("$inventoryURL?page=0&size=2")
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(
                        """{
                        |   "content":[
                        |       {"id":1,"name":"1","amount":1},
                        |       {"id":2,"name":"2","amount":2}
                        |   ],
                        |   "page":{"size":2,"number":0,"totalElements":4,"totalPages":2}
                        |}""".trimMargin()
                    )
                }
            }
        webMvc.get("$inventoryURL?page=1&size=2")
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(
                        """{
                        |   "content":[
                        |       {"id":3,"name":"3","amount":3},
                        |       {"id":4,"name":"4","amount":4}
                        |   ],
                        |   "page":{"size":2,"number":1,"totalElements":4,"totalPages":2}
                        |}""".trimMargin()
                    )
                }
            }
    }

    @Test
    fun `huge amount of data gets paged automatically using a default page size of 50`() {
        inventoryRepository.stub {
            on { findAll(PageRequest.of(0, 50)) } doReturn PageImpl(
                List(50) {
                    InventoryItem(it.toLong(), "$it", it)
                },
                PageRequest.of(0, 50),
                150,
            )
        }

        webMvc.get(inventoryURL)
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(
                        """{
                        |   "content":[
                        |       ${fiftyInventoryItemsJson()}
                        |   ],
                        |   "page":{"size":50,"number":0,"totalElements":150,"totalPages":3}
                        |}""".trimMargin()
                    )
                }
            }
    }

    private fun fiftyInventoryItemsJson() = List(50) {
        """{"id":$it,"name":"$it","amount":$it}"""
    }.joinToString(",")
}
