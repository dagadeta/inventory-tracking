package de.dagadeta.inventorytracking.backend.rest

import de.dagadeta.inventorytracking.backend.config.LoginUtilityService
import de.dagadeta.inventorytracking.backend.persistance.UsersRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/change-password")
@RestController
class PasswordChangeController(
    private val loginUtilityService: LoginUtilityService,
    private val usersRepository: UsersRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    private val logger = KotlinLogging.logger {}

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun changePassword(@RequestBody body: ChangePasswordDto): ResponseEntity<Unit> {
        val loggedInUser = loginUtilityService.loggedInUser
        val storedUser = usersRepository.findByName(loggedInUser?.name).first()

        if (passwordEncoder.matches(body.oldPassword, loggedInUser?.password)) {
            passwordEncoder.encode(body.newPassword)?.let {
                storedUser.password = it
                usersRepository.save(storedUser)
                logger.info { "password changed" }
                return ResponseEntity.ok().build()
            }
            logger.info { "encoded new password for user $loggedInUser was null" }
        }
        logger.info { "password hashes do not match for user $loggedInUser" }
        return ResponseEntity.badRequest().build()
    }
}

data class ChangePasswordDto(val oldPassword: String, val newPassword: String)
