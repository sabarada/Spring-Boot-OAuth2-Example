package com.example.auth.domain.user

import java.time.Instant
import java.util.regex.Pattern
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(
    val email: Email,
    val password: String,
) {

    @Id
    @GeneratedValue
    var id: Long? = null

    lateinit var displayName: String

    var createdAt: Instant = Instant.now()

    @JvmInline
    value class Email(val value: String) {
        init {
            if (!Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", value)) {
                throw IllegalArgumentException("not match regex and email")
            }
        }

        fun getId(): String {
            return value.split("@")[0]
        }

        fun getHost(): String {
            return value.split("@")[1]
        }
    }

    companion object {

        operator fun invoke(
            email: String,
            password: String,
            displayName: String
        ) = User(
            email = Email(email),
            password = password
        ).apply {
            this.displayName = displayName
        }
    }
}