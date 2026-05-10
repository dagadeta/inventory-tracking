package de.dagadeta.inventorytracking.backend.temp

import de.dagadeta.inventorytracking.backend.persistance.InventoryRepository
import de.dagadeta.inventorytracking.backend.rest.toDto
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@RequestMapping("/")
@Controller
class UiController(
    private val inventoryRepository: InventoryRepository,
) {

    private val logger = KotlinLogging.logger {}
    private val screenLabel: String = "Temporary Security Demo - UI [%s]"

    @GetMapping("")
    fun home(model: Model): String {
        val label = String.format(screenLabel, "HOME-PAGE")
        logger.info { "Request - $label" }
        model.addAttribute("label", label)
        return "home"
    }

    @GetMapping("/open")
    fun openPage(model: Model): String {
        val label = String.format(screenLabel, "OPEN-PAGE")
        logger.info { "Request - $label" }
        model.addAttribute("label", label)
        return "home"
    }

    @GetMapping("/protected")
    fun protectedPage(model: Model): String {
        val label = String.format(screenLabel, "PROTECTED-PAGE")
        logger.info { "Request - $label" }
        model.addAttribute("label", label)
        return "home"
    }

    @GetMapping("/inventory")
    fun inventoryPage(model: Model): String {
        val label = String.format(screenLabel, "INVENTORY-PAGE")
        logger.info { "Request - $label" }
        model.addAttribute("label", label)

        val items = inventoryRepository.findAll().map { it.toDto() }
        model.addAttribute("items", items)

        return "inventory"
    }
}
