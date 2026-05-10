package de.dagadeta.inventorytracking.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val loginUtilityService: LoginUtilityService,
) {

    // 1. Define SecurityFilterChain for API (Basic Authentication)
    @Bean
    @Order(1)
    fun basicAuthSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        // Define BasicAuth
        return http
            .securityMatcher("/api/**")
            .authorizeHttpRequests { request ->
                request.anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .httpBasic(Customizer.withDefaults())
            .csrf { it.disable() }
            .build()
    }

    // 2. Define SecurityFilterChain for Web (Form Login)
    @Bean
    @Order(2)
    fun formSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests { request ->
            request.requestMatchers("/").permitAll()
            request.requestMatchers("/open").permitAll()
            request.anyRequest().authenticated()
        }
            .formLogin(Customizer.withDefaults())
            .passwordManagement(Customizer.withDefaults())
            .build()
    }

    // 3. Ignore static directories / URLs (images, javascript files etc.)
    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web -> web.ignoring().requestMatchers("/images/**", "/js/**") }
    }

    // 4. Custom User Details Service to manage User instance
    @Bean
    fun userDetailsService() = UserDetailsService { userName: String? -> loginUtilityService.findMatch(userName) }

    // 5. Password encoder
    @Bean
    fun getPasswordEncoder() = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
