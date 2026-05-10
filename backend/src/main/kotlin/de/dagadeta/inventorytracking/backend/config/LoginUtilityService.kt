package de.dagadeta.inventorytracking.backend.config

import de.dagadeta.inventorytracking.backend.persistance.InventoryUser
import de.dagadeta.inventorytracking.backend.persistance.UsersRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class LoginUtilityService(
    val usersRepository: UsersRepository,
) {
    private val logger = KotlinLogging.logger {}

    fun findMatch(username: String?): AuthenticatedUser {
        val users = usersRepository.findByName(username)
        if (users.isEmpty()) {
            logger.info { "User not found: $username" }
            throw UsernameNotFoundException("UserName $username doesn't exist")
        }
        return AuthenticatedUser(users.single(), setOf(GrantedAuthority { users.single().role.name }))
    }

    val loggedInUser: InventoryUser?
        get() {
            val authentication: Authentication? = SecurityContextHolder.getContext().authentication
            if (authentication == null || authentication.principal == null) {
                return null
            }
            return (authentication.principal as AuthenticatedUser).user
        }
}

class AuthenticatedUser(val user: InventoryUser, authorities: Collection<GrantedAuthority>) :
    User(user.name, user.password, authorities)
