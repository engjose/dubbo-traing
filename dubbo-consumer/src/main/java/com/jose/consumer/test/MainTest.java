package com.jose.consumer.test;

import com.jose.domain.People;
import com.jose.httpinterface.SpeakingAble;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
//        SpeakingAble speakInterface = (SpeakingAble) context.getBean("speakInterface");
//        String say = speakInterface.speak(new People());
//        System.out.print(say);

        while (true) {
            Thread.yield();
        }

    }
}
