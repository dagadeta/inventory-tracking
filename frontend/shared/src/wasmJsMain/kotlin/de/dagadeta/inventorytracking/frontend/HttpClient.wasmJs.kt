package de.dagadeta.inventorytracking.frontend

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.engine.cio.*

actual fun httpClient() = HttpClient(CIO) {
    install(Auth) {
        basic {
            credentials {
                BasicAuthCredentials("Admin", "very secret")
            }
        }
    }
    install(ContentNegotiation)
}
