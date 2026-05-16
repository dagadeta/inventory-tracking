package de.dagadeta.inventorytracking.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val loginUtilityService: LoginUtilityService,
) {

    @Bean
    fun basicAuthSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .securityMatcher("/api/**")
            .authorizeHttpRequests { request ->
                request.anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(STATELESS)
            }
            .httpBasic(Customizer.withDefaults())
            .csrf { it.disable() }
            .build()
    }

    @Bean
    fun userDetailsService() = UserDetailsService { userName: String? -> loginUtilityService.findMatch(userName) }
    @Bean
    fun getPasswordEncoder() = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
