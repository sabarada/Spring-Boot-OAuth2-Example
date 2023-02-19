package com.example.auth.service.jwt;

public class JwtUserDummy {
    public String nickname;
    public Integer age;

    //기본 생성자 추가
    public JwtUserDummy() {

    }

    public JwtUserDummy(String nickname, Integer age) {
        this.nickname = nickname;
        this.age = age;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "nickname='" + nickname + '\'' +
                ", age=" + age +
                '}';
    }
}
