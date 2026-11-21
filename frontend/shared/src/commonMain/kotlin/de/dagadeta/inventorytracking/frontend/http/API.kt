package de.dagadeta.inventorytracking.frontend.http

import de.dagadeta.inventorytracking.common.model.*
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.SUCCESS
import de.dagadeta.inventorytracking.frontend.http.ApiResult.Success
import de.dagadeta.inventorytracking.frontend.http.HttpCredentials.Companion.EMPTY_CREDENTIALS
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

interface API {
  suspend fun login(credentials: HttpCredentials): ApiResult<UserInfoDto>

  fun logout()

  suspend fun changePassword(
      currentPassword: String,
      newPassword: String,
  ): ApiResult<ChangePasswordResponseDto>

  suspend fun fetchInventoryItems(): ApiResult<PageDto<InventoryItemDto>>

  suspend fun fetchUsers(): ApiResult<PageDto<UserDto>>
}

class HttpAPI : API {
  private val backendUrl = "http://localhost:8080"
  private lateinit var client: HttpClient
  private lateinit var userName: String

  override suspend fun login(credentials: HttpCredentials): ApiResult<UserInfoDto> {
    client = httpClient(credentials)
    userName = credentials.userName
    return safeApiCall { client.get("$backendUrl${UserInfoDto.ENDPOINT}") }
  }

  override fun logout() {
    client = httpClient(EMPTY_CREDENTIALS)
    userName = ""
  }

  override suspend fun changePassword(
      currentPassword: String,
      newPassword: String,
  ): ApiResult<ChangePasswordResponseDto> {
    val response =
        safeApiCall<ChangePasswordResponseDto> {
          client.post("$backendUrl${ChangePasswordRequestDto.ENDPOINT}") {
            contentType(ContentType.Application.Json)
            setBody(ChangePasswordRequestDto(currentPassword, newPassword))
          }
        }
    if (response is Success && response.data.result == SUCCESS) {
      client.close()
      client = httpClient(HttpCredentials(userName, newPassword))
    }
    return response
  }

  override suspend fun fetchInventoryItems(): ApiResult<PageDto<InventoryItemDto>> = safeApiCall {
    client.get("$backendUrl${InventoryItemDto.ENDPOINT}")
  }

  override suspend fun fetchUsers(): ApiResult<PageDto<UserDto>> = safeApiCall {
    client.get("$backendUrl${UserDto.ENDPOINT}")
  }
}
