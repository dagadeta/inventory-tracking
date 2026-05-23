package de.dagadeta.inventorytracking.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO

@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@SpringBootApplication
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
