package com.daniel.yurpc.proxy;

import lombok.extern.java.Log;

import java.lang.reflect.Proxy;

@Log
public class ServiceProxyFactory {

    // service class -> proxy object
    public static <T> T getProxy(Class<T> svcClass) {
        Object object = Proxy.newProxyInstance(svcClass.getClassLoader(),
                new Class[]{svcClass}, new ServiceProxy());
        if (svcClass.isInstance(object)) {
            return svcClass.cast(object);
        } else {
            log.severe("Fail to get proxy ...");
            return null;
        }
    }
}
