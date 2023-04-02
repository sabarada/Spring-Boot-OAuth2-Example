package com.example.resource.domain.user.apis

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    // 일반적으로 Service를 통해서 Repoisitory로 접근하지만 해당 샘플의 경우는 간결성을 위해서 Controller에서 직접 접근합니다.
) {

    @GetMapping("/me")
    fun getUser(
        // todo 파싱된 정보를 가져올 수 있도록 합니다.
        //      context 가져오기
    ): String {
        return "call me"
    }

}