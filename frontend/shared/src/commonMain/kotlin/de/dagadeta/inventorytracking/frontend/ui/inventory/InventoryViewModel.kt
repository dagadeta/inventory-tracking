package de.dagadeta.inventorytracking.frontend.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.dagadeta.inventorytracking.common.model.InventoryItemDto
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.frontend.http.API
import de.dagadeta.inventorytracking.frontend.http.ApiResult.*
import de.dagadeta.inventorytracking.frontend.ui.inventory.InventoryAction.FetchInventoryItems
import de.dagadeta.inventorytracking.frontend.ui.users.Page
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val api: API,
) : ViewModel() {
  val state: StateFlow<InventoryState>
    field = MutableStateFlow(InventoryState())

  fun onAction(action: InventoryAction) {
    when (action) {
      FetchInventoryItems -> fetchInventoryItems()
    }
  }

  private fun fetchInventoryItems() {
    viewModelScope.launch {
      when (val result = api.fetchInventoryItems()) {
        is Error ->
            state.update {
              it.copy(items = Page(), errorText = "${result.message} (${result.code})")
            }
        is Success<PageDto<InventoryItemDto>> ->
            state.update { it.copy(items = result.toItems(), errorText = "") }
        Unprivileged ->
            state.update {
              it.copy(items = Page(), errorText = "You are not allowed to browse items.")
            }
      }
    }
  }
}

data class InventoryState(val items: Page<InventoryItem> = Page(), val errorText: String = "")

data class InventoryItem(
    val id: Long,
    val name: String,
    val totalAmount: Int,
    val lentAmount: Int,
    val remainingAmount: Int,
)

private fun Success<PageDto<InventoryItemDto>>.toItems() =
    Page(
        content = data.content.map { it.toInventoryItem() },
        size = data.page.size,
        number = data.page.number,
        totalElements = data.page.totalElements,
        totalPages = data.page.totalPages,
    )

private fun InventoryItemDto.toInventoryItem() =
    InventoryItem(
        id = id,
        name = name,
        totalAmount = totalAmount,
        lentAmount = lentAmount,
        remainingAmount = remainingAmount,
    )

sealed interface InventoryAction {
  object FetchInventoryItems : InventoryAction
}
