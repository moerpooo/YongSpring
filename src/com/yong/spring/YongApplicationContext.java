package com.yong.spring;

import com.yong.service.AppConfig;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Banging
 * @Classname YongApplicationContext
 * @Description TODO
 * @Date 2022/6/7 13:45
 * @Created by Banging
 */
public class YongApplicationContext {

    private Class configClass;
    private ConcurrentHashMap<String,BeanDefinition> beanDefinitionMap=new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Object> singletonBeanMap=new ConcurrentHashMap<>();

    public YongApplicationContext(Class configClass) {
        this.configClass = configClass;
        if(configClass.isAnnotationPresent(ComponentScan.class)){
            ComponentScan componentScanAnnotation=(ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path=componentScanAnnotation.value();
            path=path.replace(".","/");
            ClassLoader classLoader=YongApplicationContext.class.getClassLoader();
            URL resource=classLoader.getResource(path);
            File file=new File(resource.getFile());
            System.out.println(file);
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (File f : files) {
                    String absolutePath = f.getAbsolutePath();
                    System.out.println(absolutePath);
                    if (absolutePath.endsWith(".class")){
                        String className=componentScanAnnotation.value();
                        String className1=absolutePath.substring(absolutePath.indexOf("com"),absolutePath.indexOf(".class"));
                        className1=className1.replace("\\",".");
                        System.out.println(className);
                        System.out.println(className1);
                        try {
                            Class<?> aClass = classLoader.loadClass(className1);
                            if (aClass.isAnnotationPresent(Component.class)){
                                Component component = aClass.getAnnotation(Component.class);
                                String beanName = component.value();

                                //BeanDefinition
                                BeanDefinition beanDefinition=new BeanDefinition();
                                beanDefinition.setType(aClass);
                                if (aClass.isAnnotationPresent(Scope.class)){
                                    Scope scopeAnnotation = aClass.getAnnotation(Scope.class);
                                    beanDefinition.setScope(scopeAnnotation.value());
                                }else {
                                    beanDefinition.setScope("singleton");
                                    singletonBeanMap.put(beanName,beanDefinition);
                                }
                                beanDefinitionMap.put(beanName,beanDefinition);
                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        }
        //
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition=beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")){
                Object bean=creatBean(beanName,beanDefinition);
            }
        }
    }

    private Object creatBean(String beanName,BeanDefinition beanDefinition){
        Class clz=beanDefinition.getType();
        try {
            Object o = clz.getConstructor().newInstance();
            return o;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public Object getBean(String beanName){
        BeanDefinition beanDefinition=beanDefinitionMap.get(beanName);
        if (beanDefinition==null){
            throw new NullPointerException();
        }else {
            String scope=beanDefinition.getScope();
            if ("singleton".equals(scope)){
                Object bean = singletonBeanMap.get(beanName);
                if (bean==null){
                    Object o = creatBean(beanName, beanDefinition);
                    singletonBeanMap.put(beanName,o);
                }
                return bean;
            }else {
                return creatBean(beanName,beanDefinition);
            }

        }
    }
}
