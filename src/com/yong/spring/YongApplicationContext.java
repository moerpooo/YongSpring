package com.yong.spring;

import com.yong.service.AppConfig;

/**
 * @author Banging
 * @Classname YongApplicationContext
 * @Description TODO
 * @Date 2022/6/7 13:45
 * @Created by Banging
 */
public class YongApplicationContext {

    private Class configClass;

    public YongApplicationContext(Class configClass) {
        this.configClass = configClass;
    }

    public Object getBean(String beanName){
        return null;
    }
}
