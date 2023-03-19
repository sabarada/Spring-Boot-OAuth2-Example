package com.example.auth.config.properites

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("spring.authorization.server")
data class AuthorizationServerProperties(
    val clientId: String,
    val clientSecret: String,
    val authenticationRedirectUri: String,
    val successRedirectUri: String,
    val scope: String
)