package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import de.dagadeta.inventorytracking.frontend.ui.users.Page

const val listColumnTag = "pageable-list-column"

@Composable
fun <T> PageableList(
    header: String,
    loadItems: () -> Unit,
    items: Page<T>,
    error: String,
    itemContent: @Composable ((T) -> Unit),
    modifier: Modifier = Modifier,
) {
  LaunchedEffect(true) { loadItems() }

  Column(
      modifier = modifier.testTag(listColumnTag),
  ) {
    Text(header, fontWeight = Bold)

    if (error.isNotEmpty()) {
      Text(error)
    }
    items.content.forEach { itemContent(it) }
  }
}
