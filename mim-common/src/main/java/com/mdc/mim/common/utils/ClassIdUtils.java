package com.mdc.mim.common.utils;


import java.util.HashMap;
import java.util.Map;

public class ClassIdUtils {
    private static Map<Class<?>, Integer> classToIdMap = new HashMap<>();
    private static Map<Integer, Class<?>> idToClassMap = new HashMap<>();

    public static Class<?> getClassById(int classId) {
        return idToClassMap.get(classId);
    }

    public static int generateClassId(Class<?> clazz, int version) {
        if (!classToIdMap.containsKey(clazz)) {
            int classId = version << (3 * 8) | (clazz.getName().hashCode() >>> 8);
            classToIdMap.put(clazz, classId);
            idToClassMap.put(classId, clazz);
        }
        return classToIdMap.get(clazz);
    }
}
