# Jwt를 이용한 OAuth 인증 예제

## 함께 작성된 이론 및 설명 문서

- [[JWT] JWT(Json Web Token)에 대해서 자세히 알아봅시다 - 이론편](https://sabarada.tistory.com/246)
- [[JWT+JAVA] JWT(Json Web Token)에 대해서 자세히 알아봅시다 - 실습편](https://sabarada.tistory.com/247)
- [[OAuth2] OAuth2 개론 - 개요와 Authorization Code Flow](https://sabarada.tistory.com/248)
- Auth 서버 만들기 실습 [미완성]
- Resource 서버 만들기 실습 [미완성]

## 환경 및 의존성 정보

- MySQL : 5.7
- Spring Boot : 2.7.8
- kotlin : 1.6.21
- java : 17.0
- jjwt [https://github.com/jwtk/jjwt]

## 실행(bootRun) 방법

아래 명령어를 통해 docker container를 실행시킵니다.

container에는 mysql이 포함되어 있습니다.

```bash
docker compose up -d
```
