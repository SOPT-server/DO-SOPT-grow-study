package io.demo.danggn.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id @GeneratedValue
    private long id;

    private String name;

    public Member() {
    }

    public Member(String name) {
        this.name = name;
    }

    // 사용자 이름 제한
}
