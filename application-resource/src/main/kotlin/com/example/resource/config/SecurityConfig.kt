package com.example.resource.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http.mvcMatcher("/articles/**")
            .authorizeRequests()
            .mvcMatchers("/articles/**")
            .access("hasAuthority('SCOPE_articles.read')")
            .and()
            .oauth2ResourceServer()
            .jwt()
        return http.build()
    }

}