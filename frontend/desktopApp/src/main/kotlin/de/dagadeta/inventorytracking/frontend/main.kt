package de.dagadeta.inventorytracking.frontend

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Inventory Tracking",
    ) {
        App()
    }
}
