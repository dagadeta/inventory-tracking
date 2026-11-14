package de.dagadeta.inventorytracking.backend.config

import de.dagadeta.inventorytracking.backend.persistance.UsersRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.transaction.Transactional
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service("userDetailsService")
@Transactional
class LoginUtilityService(
    val usersRepository: UsersRepository,
) : UserDetailsService {
  private val logger = KotlinLogging.logger {}

  override fun loadUserByUsername(username: String): UserDetails {
    val users = usersRepository.findByName(username)
    if (users.isEmpty()) {
      logger.info { "User not found: $username" }
      throw UsernameNotFoundException("UserName $username doesn't exist")
    }
    val user = users.single()
    return User(user.name, user.password, listOf(SimpleGrantedAuthority("ROLE_${user.role.name}")))
  }

  val loggedInUser: UserDetails?
    get() = SecurityContextHolder.getContext().authentication?.principal as UserDetails?
}
