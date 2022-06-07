package com.yong.service;

import com.yong.spring.YongApplicationContext;

/**
 * @Classname Test
 * @Description TODO
 * @Date 2022/6/7 13:43
 * @Created by Banging
 */
public class Test {
    public static void main(String[] args) {
        YongApplicationContext applicationContext = new YongApplicationContext(AppConfig.class);

        UserService userService= (UserService) applicationContext.getBean("userService");
    }
}
