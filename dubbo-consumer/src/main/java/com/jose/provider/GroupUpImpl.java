package com.jose.provider;

import com.jose.domain.People;
import com.jose.httpinterface.GroupUpInterface;

public class GroupUpImpl implements GroupUpInterface {

    /**
     * 这是年龄并返回
     *
     * @param people
     * @return
     */
    public People addAge(People people) {
        people.setAge(33);
        return people;
    }
}
