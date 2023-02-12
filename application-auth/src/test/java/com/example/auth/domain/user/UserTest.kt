package com.example.auth.domain.user

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class UserTest {

    @Test
    fun `User_Entity 생성 성공 테스트`() {
        // given
        val email = "koangho93@naver.com"
        val password = "12345"
        val displayName = "sabarada"

        // when
        val user = User(
            email = email,
            password = password,
            displayName = displayName
        )

        // then
        assertEquals(user.email.value, email)
        assertEquals(user.password, password)
        assertEquals(user.displayName, displayName)
        assertNotNull(user.createdAt)
    }

    @Test
    fun `User_Entity 생성 실패 테스트`() {
        // when & then
        assertThrows<IllegalArgumentException> {
            val user = User(
                email = "koan93",
                password = "12345",
                displayName = "sabarada"
            )
        }
    }

}