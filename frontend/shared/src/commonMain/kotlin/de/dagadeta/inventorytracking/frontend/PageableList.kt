package de.dagadeta.inventorytracking.frontend

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import de.dagadeta.inventorytracking.common.model.PageDto
import de.dagadeta.inventorytracking.common.model.PageDto.Companion.emptyPage
import de.dagadeta.inventorytracking.frontend.http.ApiResult

@Composable
fun <T> PageableList(
    header: String,
    fetchItems: suspend () -> ApiResult<PageDto<T>>,
    describeItem: (T) -> String,
    modifier: Modifier = Modifier,
) {
    var items by remember { mutableStateOf<PageDto<T>>(emptyPage()) }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        when (val result = fetchItems()) {
            is ApiResult.Success -> {
                items = result.data
                error = ""
            }

            is ApiResult.Error -> {
                items = emptyPage()
                error = "${result.message} (${result.code})"
            }

            is ApiResult.Unprivileged -> {
                items = emptyPage()
                error = "You are not allowed to browse $header."
            }
        }
    }
    Column(
        modifier = modifier,
    ) {
        Text(header, fontWeight = Bold)

        if (error.isNotEmpty()) {
            Text(error)
        }
        items.content.forEach {
            Text(describeItem(it))
        }
    }
}
