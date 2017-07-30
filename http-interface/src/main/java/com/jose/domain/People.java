package com.jose.domain;

import java.io.Serializable;

/**
 * 2017-07-27 09:22:16
 */
public class People implements Serializable{

    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
