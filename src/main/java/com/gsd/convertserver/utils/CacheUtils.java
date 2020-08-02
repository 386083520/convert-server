package com.gsd.convertserver.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CacheUtils {
    private CacheUtils() {
        throw new IllegalStateException("Utility class");
    }
    private static ConcurrentMap<String, String> map = new ConcurrentHashMap<>(10);

    public static void setValues(String key, String value) {
        map.put(key, value);
    }
    public static void moveValues(String key, String value) {
        if(map.containsKey(key)) {
            map.remove(key, value);
        }
    }

    public static ConcurrentMap<String,String> getMap() {
        return map;
    }
}
