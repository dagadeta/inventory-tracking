package de.dagadeta.inventorytracking.backend.domain

import de.dagadeta.inventorytracking.backend.domain.model.InventoryItemAmounts
import de.dagadeta.inventorytracking.backend.persistance.InventoryItem
import de.dagadeta.inventorytracking.backend.persistance.InventoryLentRepository
import de.dagadeta.inventorytracking.backend.persistance.InventoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class InventoryServiceTest {

    private val inventoryRepository = mock<InventoryRepository>()
    private val inventoryLentRepository = mock<InventoryLentRepository>()
    private val inventoryService = InventoryService(inventoryRepository, inventoryLentRepository)

    private val pageable = Pageable.ofSize(50)

    @ParameterizedTest
    @MethodSource("lentItems")
    fun `allInventoryItemAmounts with amounts of lent items`(totalItems: Int, lentItems: Int, remainingItems: Int) {
        val item1 = InventoryItem(1, "item-1", totalItems)
        inventoryRepository.stub {
            on { findAll(pageable) } doReturn PageImpl(listOf(item1))
        }
        inventoryLentRepository.stub {
            on { countByItem(item1) } doReturn lentItems
        }

        val page = inventoryService.allInventoryItemAmounts(pageable)

        assertThat(page.totalPages).isEqualTo(1)
        assertThat(page.totalElements).isEqualTo(1)
        val amounts = page.single()
        assertThat(amounts).isEqualTo(InventoryItemAmounts(1, "item-1", totalItems, lentItems, remainingItems))
    }

    companion object {
        @JvmStatic
        fun lentItems() = listOf(
            Arguments.of(3, 0, 3),
            Arguments.of(3, 1, 2),
            Arguments.of(3, 2, 1),
            Arguments.of(3, 3, 0),
        )
    }
}
