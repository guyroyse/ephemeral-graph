package com.guyroyse.ephemeral.graph.reflection;

import com.guyroyse.ephemeral.graph.annotations.GraphID;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class Reflector {

    public static Optional<Object> invoke(Method m, Object o) {
        try {
            return Optional.ofNullable(m.invoke(o));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Annotation> Optional<T> getAnnotation(Method m, Class<T> clazz) {
        return Optional.ofNullable(m.getAnnotation(clazz));
    }
}
