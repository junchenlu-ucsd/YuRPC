package com.daniel.yurpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegistry {

    // the reference to the ConcurrentHashMap object is constant
    private static final Map<String, Class<?>> map = new ConcurrentHashMap<>();

    public static void register(String svcName, Class<?> implClass) {
        map.put(svcName, implClass);
    }

    public static Class<?> getSvc(String svcName) {
        return map.get(svcName);
    }

    public static void rmSvc(String svcName) {
        map.remove(svcName);
    }
}
