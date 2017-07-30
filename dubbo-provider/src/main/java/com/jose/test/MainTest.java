package com.jose.test;

import com.jose.domain.People;
import com.jose.httpinterface.GroupUpInterface;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        System.out.println("start dubbo");

        System.out.println("*********作为消费者****************");
        GroupUpInterface groupUpInterface = (GroupUpInterface) applicationContext.getBean("groupUpInterface");
        People people = groupUpInterface.addAge(new People());
        System.out.print(people.getAge());

        while (true) {
            Thread.yield();
        }
    }
}
