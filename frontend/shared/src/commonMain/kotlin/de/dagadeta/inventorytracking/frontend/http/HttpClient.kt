package de.dagadeta.inventorytracking.frontend.http

import de.dagadeta.inventorytracking.frontend.http.Credentials.InputCredentials
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun httpClient(credentials: InputCredentials) = HttpClient {
    expectSuccess = false
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            }
        )
    }
    install(Logging) {
        level = LogLevel.ALL
        logger = Logger.SIMPLE
    }
    defaultRequest {
        basicAuth(credentials.userName, credentials.password)
    }
}
