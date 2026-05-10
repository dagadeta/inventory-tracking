package de.dagadeta.inventorytracking.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    // 1. Define SecurityFilterChain for API (Basic Authetication)
    @Bean
    @Order(1)
    fun basicAuthSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        // Define BasicAuth
        return http
            .securityMatcher("/api/**")
            .authorizeHttpRequests { request ->
                request.requestMatchers(
                    "/api/inventory/**",
                    "/api/users/**",
                ).permitAll()
                request.anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .httpBasic(Customizer.withDefaults())
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
            .build()
    }

    // 3. Ignore static directors / urls (images, javascript files etc.)
    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web -> web.ignoring().requestMatchers("/images/**", "/js/**") }
    }
}
