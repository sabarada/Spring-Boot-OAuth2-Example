package com.example.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
class DefaultSecurityConfig {
    // @formatter:off
    /**
     * 기본적인 접근에 대해서 인증하기 위한 FilterChain
     *
     * 인증없이 접근하면 {@link com.example.auth.config.AuthorizationServerConfig}의
     * #authorizationServerSecurityFilterChain 메서드 exceptinoHaling에 의해서
     * /login 페이지로 이동한다.
     */
    @Bean
    @Throws(Exception::class)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorize ->
                authorize.anyRequest().authenticated()
            }
            .formLogin(withDefaults())
        return http.build()
    }

    // @formatter:on
}