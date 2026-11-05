package com.xbbg.springlab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class JavaConfigApplication {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            // get beans from context and use them
        }
    }
}
