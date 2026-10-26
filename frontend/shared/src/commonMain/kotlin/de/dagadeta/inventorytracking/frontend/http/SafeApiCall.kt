package de.dagadeta.inventorytracking.frontend.http


import de.dagadeta.inventorytracking.common.model.ErrorResponse
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.http.HttpStatusCode.Companion.Forbidden
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.serialization.*
import kotlinx.io.IOException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.json.Json

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data object Unprivileged : ApiResult<Nothing>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
}

val json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalSerializationApi::class)
suspend inline fun <reified T> safeApiCall(block: suspend () -> HttpResponse): ApiResult<T> {
    return try {
        val response = block()
        if (response.status in listOf(Unauthorized, Forbidden)) {
            return ApiResult.Unprivileged
        }

        val responseText = response.bodyAsText()
        try {
            val decoded = json.decodeFromString<T>(responseText)
            ApiResult.Success(decoded)
        } catch (_: MissingFieldException) {
            val error = json.decodeFromString<ErrorResponse>(responseText)
            ApiResult.Error(error.toString())
        } catch (e: JsonConvertException) {
            ApiResult.Error("Response conversion error: ${e.message}", response.status.value)
        }
    } catch (e: RedirectResponseException) {
        ApiResult.Error("Redirect error: ${e.message}", e.response.status.value)
    } catch (e: ClientRequestException) {
        ApiResult.Error("Client error: ${e.message}", e.response.status.value)
    } catch (e: ServerResponseException) {
        ApiResult.Error("Server error: ${e.message}", e.response.status.value)
    } catch (e: HttpRequestTimeoutException) {
        ApiResult.Error("Timeout error: ${e.message}")
    } catch (e: IOException) {
        ApiResult.Error("Network error: ${e.message}")
    } catch (e: Exception) {
        ApiResult.Error("Unknown error: ${e.message} ($e)")
    }
}
