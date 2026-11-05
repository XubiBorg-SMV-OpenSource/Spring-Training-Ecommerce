package com.xbbg.springlab;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XmlApplication {

    public static void main(String[] args) {
        try (var context = new ClassPathXmlApplicationContext("applicationContext.xml")) {
            // get beans from context and use them
        }
    }
}