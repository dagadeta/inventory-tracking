package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import de.dagadeta.inventorytracking.frontend.http.API
import inventory_tracking.frontend.shared.generated.resources.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainAppNavigation(
    api: API,
    modifier: Modifier = Modifier,
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    val items = listOf("Inventory", "Users")
    val selectedIcons = listOf(painterResource(Res.drawable.view_list_filled), painterResource(Res.drawable.group_filled))
    val unselectedIcons = listOf(painterResource(Res.drawable.view_list), painterResource(Res.drawable.group))

    val state = rememberWideNavigationRailState()
    val scope = rememberCoroutineScope()
    val headerDescription =
        if (state.targetValue == WideNavigationRailValue.Expanded) {
            "Collapse rail"
        } else {
            "Expand rail"
        }

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        WideNavigationRail(
            state = state,
            header = {
                IconButton(
                    modifier =
                        Modifier.padding(start = 24.dp).semantics {
                            stateDescription =
                                if (state.currentValue == WideNavigationRailValue.Expanded) {
                                    "Expanded"
                                } else {
                                    "Collapsed"
                                }
                        },
                    onClick = {
                        scope.launch {
                            if (state.targetValue == WideNavigationRailValue.Expanded)
                                state.collapse()
                            else state.expand()
                        }
                    },
                ) {
                    if (state.targetValue == WideNavigationRailValue.Expanded) {
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
            items.forEachIndexed { index, item ->
                WideNavigationRailItem(
                    railExpanded = state.targetValue == WideNavigationRailValue.Expanded,
                    icon = {
                        val painter =
                            if (selectedItem == index) {
                                selectedIcons[index]
                            } else {
                                unselectedIcons[index]
                            }
                        Icon(painter = painter, contentDescription = null)
                    },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index },
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            when(selectedItem) {
                0 -> {
                    PageableList(
                        header = "Inventory",
                        fetchItems = { api.fetchInventoryItems() },
                        describeItem = { "${it.name}: ${it.amount}" }
                    )
                }
                1 -> {
                    PageableList(
                        header = "Users",
                        fetchItems = { api.fetchUsers() },
                        describeItem = { "${it.name}: ${it.role}" }
                    )
                }
            }
        }
    }
}
