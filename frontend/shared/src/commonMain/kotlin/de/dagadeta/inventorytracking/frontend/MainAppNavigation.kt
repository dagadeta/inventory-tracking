package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.WideNavigationRailValue.Expanded
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import de.dagadeta.inventorytracking.frontend.http.API
import inventory_tracking.frontend.shared.generated.resources.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

const val expandButtonTag = "expand-button"
const val inventoryListTag = "inventory-list"
const val usersListTag = "users-list"

private data class NavItem(
    val name: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val content: @Composable (ColumnScope.() -> Unit)
)

@Composable
fun MainAppNavigation(
    api: API,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        NavItem(
            name = "Inventory",
            selectedIcon = painterResource(Res.drawable.view_list_filled),
            unselectedIcon = painterResource(Res.drawable.view_list),
            content = {
                PageableList(
                    header = "Inventory",
                    fetchItems = { api.fetchInventoryItems() },
                    describeItem = { "${it.name}: ${it.amount}" },
                    modifier = Modifier.testTag(inventoryListTag),
                )
            }
        ),
        NavItem(
            name = "Users",
            selectedIcon = painterResource(Res.drawable.group_filled),
            unselectedIcon = painterResource(Res.drawable.group),
            content = {
                PageableList(
                    header = "Users",
                    fetchItems = { api.fetchUsers() },
                    describeItem = { "${it.name}: ${it.role}" },
                    modifier = Modifier.testTag(usersListTag),
                )
            }
        ),
    )

    var selectedItem by rememberSaveable { mutableStateOf(items.first()) }

    val state = rememberWideNavigationRailState()
    val scope = rememberCoroutineScope()
    val headerDescription = if (state.targetValue == Expanded) "Collapse rail" else "Expand rail"

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        WideNavigationRail(
            state = state,
            header = {
                IconButton(
                    modifier =
                        Modifier.testTag(expandButtonTag).padding(start = 24.dp).semantics {
                            stateDescription = if (state.currentValue == Expanded) "Expanded" else "Collapsed"
                        },
                    onClick = {
                        scope.launch {
                            if (state.targetValue == Expanded) state.collapse() else state.expand()
                        }
                    },
                ) {
                    if (state.targetValue == Expanded) {
                        Icon(
                            painter = painterResource(Res.drawable.menu_open),
                            contentDescription = headerDescription
                        )
                    } else {
                        Icon(
                            painter = painterResource(Res.drawable.menu),
                            contentDescription = headerDescription
                        )
                    }
                }
            },
        ) {
            items.forEach { item ->
                WideNavigationRailItem(
                    railExpanded = state.targetValue == Expanded,
                    icon = {
                        val painter = if (selectedItem == item) item.selectedIcon else item.unselectedIcon
                        Icon(painter = painter, contentDescription = null)
                    },
                    label = { Text(item.name) },
                    selected = selectedItem == item,
                    onClick = { selectedItem = item },
                    modifier = Modifier.testTag("navigate-to-${item.name}"),
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            content = selectedItem.content
        )
    }
}
