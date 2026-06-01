package de.dagadeta.inventorytracking.backend.config

import de.dagadeta.inventorytracking.backend.persistance.UserRole.ADMIN
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    fun basicAuthSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .securityMatcher("/api/**")
            .authorizeHttpRequests { request ->
                request.requestMatchers("/api/users/**").hasRole(ADMIN.name)
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
    fun getPasswordEncoder() = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
