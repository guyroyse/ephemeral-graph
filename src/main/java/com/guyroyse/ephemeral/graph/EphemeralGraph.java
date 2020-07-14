package com.guyroyse.ephemeral.graph;

import com.guyroyse.ephemeral.graph.annotations.GraphID;
import com.guyroyse.ephemeral.graph.lettuce.GraphCommands;
import com.guyroyse.ephemeral.graph.lettuce.LettuceGraph;
import com.guyroyse.ephemeral.graph.reflection.Reflector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EphemeralGraph {
    LettuceGraph lettuceGraph;
    private Cypherizer cypherizer;
    private String key;
    private Map<String, Object> objectMap;

    public EphemeralGraph(String key) {
        this.key = key;
        this.cypherizer = new Cypherizer();
        this.lettuceGraph = new LettuceGraph("redis://localhost");
        this.objectMap = new HashMap();
    }

    public void load(Object... objects) {
        GraphCommands graph = lettuceGraph.getCommands();
        Arrays.stream(objects).forEach(o -> {
            String id = extractId(o);
            if (!objectMap.containsKey(id)) {
                objectMap.put(id, o);
                String query = cypherizer.cypherize(o);
                graph.query(key, query);
            }
        });
    }

    public List<Object> query(String query) {
        return null;
    }

    private String extractId(Object o) {
        return (String) Arrays.stream(o.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(GraphID.class))
                .findFirst()
                .flatMap(m -> Reflector.invoke(m, o))
                .orElseThrow(() -> new RuntimeException("GraphID annotation is required"));
    }

}