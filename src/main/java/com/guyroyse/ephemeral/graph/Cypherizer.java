package com.guyroyse.ephemeral.graph;

import com.guyroyse.ephemeral.graph.annotations.GraphID;
import com.guyroyse.ephemeral.graph.annotations.GraphInt;
import com.guyroyse.ephemeral.graph.annotations.GraphString;
import com.guyroyse.ephemeral.graph.annotations.Graphable;
import com.guyroyse.ephemeral.graph.reflection.Reflector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cypherizer {
    public String cypherize(Object object) {
        String label = buildQueryLabel(object);
        String sets = buildQuerySets(object);
        String query = String.format("CREATE (n:%s) SET %s", label, sets);
        return query;
    }

    private String buildQueryLabel(Object object) {
        Graphable graphable = object.getClass().getAnnotation(Graphable.class);
        return graphable.value();
    }

    private String buildQuerySets(Object object) {
        return Arrays.stream(object.getClass().getMethods())
                .filter(m -> hasDesirableAnnotation(m))
                .map(m -> buildQuerySet(object, m))
                .sorted()
                .collect(Collectors.joining(", "));
    }

    private boolean hasDesirableAnnotation(Method m) {
        return m.isAnnotationPresent(GraphID.class)
                || m.isAnnotationPresent(GraphString.class)
                || m.isAnnotationPresent(GraphInt.class);
    }

    private String buildQuerySet(Object o, Method m) {
        return Stream.of(buildQueryIdSet(o, m), buildQueryStringSet(o, m), buildQueryIntegerSet(o, m))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(", "));
    }

    private String buildQueryIdSet(Object object, Method method) {
        String set = "";

        Optional<GraphID> annotation = Reflector.getAnnotation(method, GraphID.class);
        if (annotation.isEmpty()) return set;

        Optional<Object> value = Reflector.invoke(method, object);
        if (value.isEmpty()) return set;

        String name = "__id";
        set = String.format("n.%s = '%s'", name, value.get());

        return set;
    }

    private String buildQueryStringSet(Object object, Method method) {
        String set = "";

        Optional<GraphString> annotation = Reflector.getAnnotation(method, GraphString.class);
        if (annotation.isEmpty()) return set;

        Optional<Object> value = Reflector.invoke(method, object);
        if (value.isEmpty()) return set;

        String name = annotation.get().value();
        set = String.format("n.%s = '%s'", name, value.get());

        return set;
    }

    private String buildQueryIntegerSet(Object object, Method method) {
        String set = "";

        Optional<GraphInt> annotation = Reflector.getAnnotation(method, GraphInt.class);
        if (annotation.isEmpty()) return set;

        Optional<Object> value = Reflector.invoke(method, object);
        if (value.isEmpty()) return set;

        String name = annotation.get().value();
        set = String.format("n.%s = %s", name, (int) value.get());

        return set;
    }
}
