package com.jose.provider;

import com.jose.domain.People;
import com.jose.httpinterface.SpeakingAble;

public class SpeakInterfaceImpl implements SpeakingAble {

    public String speak(People people) {
        return "Hello world!";
    }
}
