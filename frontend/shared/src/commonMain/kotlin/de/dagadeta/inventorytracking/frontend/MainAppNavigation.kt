package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.background
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
import de.dagadeta.inventorytracking.frontend.ui.inventory.InventoryAction
import de.dagadeta.inventorytracking.frontend.ui.inventory.InventoryScreen
import de.dagadeta.inventorytracking.frontend.ui.inventory.InventoryState
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserAccountAction
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserAccountScreen
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserAccountState
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserRole
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserRole.ADMIN
import de.dagadeta.inventorytracking.frontend.ui.useraccount.UserRole.USER
import de.dagadeta.inventorytracking.frontend.ui.users.UsersAction
import de.dagadeta.inventorytracking.frontend.ui.users.UsersScreen
import de.dagadeta.inventorytracking.frontend.ui.users.UsersState
import inventory_tracking.frontend.shared.generated.resources.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

const val expandButtonTag = "expand-button"
const val inventoryListTag = "inventory-list"
const val usersListTag = "users-list"
const val userAccountTag = "user-account"

private data class NavItem(
    val name: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val minRole: UserRole,
    val content: @Composable (ColumnScope.() -> Unit),
)

@Composable
fun MainAppNavigation(
    userAccountState: UserAccountState,
    onUserAccountAction: (UserAccountAction) -> Unit,
    inventoryState: InventoryState,
    onInventoryAction: (InventoryAction) -> Unit,
    usersState: UsersState,
    onUsersAction: (UsersAction) -> Unit,
    modifier: Modifier = Modifier,
) {
  val allNavItems =
      listOf(
          NavItem(
              name = "Inventory",
              selectedIcon = painterResource(Res.drawable.view_list_filled),
              unselectedIcon = painterResource(Res.drawable.view_list),
              minRole = USER,
              content = {
                InventoryScreen(
                    inventoryState,
                    onInventoryAction,
                    modifier = Modifier.testTag(inventoryListTag),
                )
              },
          ),
          NavItem(
              name = "Users",
              selectedIcon = painterResource(Res.drawable.group_filled),
              unselectedIcon = painterResource(Res.drawable.group),
              minRole = ADMIN,
              content = {
                UsersScreen(
                    usersState,
                    onUsersAction,
                    modifier = Modifier.testTag(usersListTag),
                )
              },
          ),
          NavItem(
              name = "Account",
              selectedIcon = painterResource(Res.drawable.account_filled),
              unselectedIcon = painterResource(Res.drawable.account),
              minRole = USER,
              content = {
                UserAccountScreen(
                    userAccountState,
                    onUserAccountAction,
                    modifier = Modifier.testTag(userAccountTag),
                )
              },
          ),
      )
  val shownNavItems = allNavItems.filter { userAccountState.userInfo.role >= it.minRole }

  var selectedItem by rememberSaveable { mutableStateOf(shownNavItems.first()) }
  val state = rememberWideNavigationRailState()
  val scope = rememberCoroutineScope()
  val headerDescription = if (state.targetValue == Expanded) "Collapse rail" else "Expand rail"

  Row(modifier = modifier.fillMaxWidth()) {
    WideNavigationRail(
        state = state,
        header = {
          IconButton(
              modifier =
                  Modifier.testTag(expandButtonTag).padding(start = 24.dp).semantics {
                    stateDescription =
                        if (state.currentValue == Expanded) "Expanded" else "Collapsed"
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
                  contentDescription = headerDescription,
              )
            } else {
              Icon(
                  painter = painterResource(Res.drawable.menu),
                  contentDescription = headerDescription,
              )
            }
          }
        },
    ) {
      shownNavItems.forEach { item ->
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
        modifier =
            Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    color = MaterialTheme.colorScheme.surface,
                ),
        content = selectedItem.content,
    )
  }
}
