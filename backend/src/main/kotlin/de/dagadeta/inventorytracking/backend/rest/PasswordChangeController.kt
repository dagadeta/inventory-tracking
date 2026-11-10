package de.dagadeta.inventorytracking.backend.rest

import de.dagadeta.inventorytracking.backend.config.LoginUtilityService
import de.dagadeta.inventorytracking.backend.persistance.UsersRepository
import de.dagadeta.inventorytracking.common.model.ChangePasswordRequestDto
import de.dagadeta.inventorytracking.common.model.ChangePasswordResponseDto
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.SUCCESS
import de.dagadeta.inventorytracking.common.model.PasswordChangeResult.WRONG_PASSWORD
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping(ChangePasswordRequestDto.ENDPOINT)
@RestController
class PasswordChangeController(
    private val loginUtilityService: LoginUtilityService,
    private val usersRepository: UsersRepository,
    private val passwordEncoder: PasswordEncoder,
) {
  private val logger = KotlinLogging.logger {}

  @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
  fun changePassword(
      @RequestBody body: ChangePasswordRequestDto,
  ): ResponseEntity<ChangePasswordResponseDto> {
    val loggedInUsername = loginUtilityService.loggedInUser?.username
    val storedUser = usersRepository.findByName(loggedInUsername).first()
    if (passwordEncoder.matches(body.currentPassword, storedUser.password)) {
      passwordEncoder.encode(body.newPassword)?.let {
        storedUser.password = it
        usersRepository.save(storedUser)
        logger.info { "password changed" }
        return ResponseEntity.ok().body(ChangePasswordResponseDto(SUCCESS))
      }
      logger.info { "encoded new password for user '$loggedInUsername' was null" }
    }
    logger.info { "password hashes do not match for user '$loggedInUsername'" }
    return ResponseEntity.badRequest().body(ChangePasswordResponseDto(WRONG_PASSWORD))
  }
}
