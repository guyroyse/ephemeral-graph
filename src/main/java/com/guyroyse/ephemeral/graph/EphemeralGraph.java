package com.guyroyse.ephemeral.graph;

import java.util.List;

public class EphemeralGraph {
    LettuceGraph lettuceGraph;
    private Cypherizer cypherizer;
    private String key;

    public EphemeralGraph(String key) {
        this.key = key;
        this.cypherizer = new Cypherizer();
        this.lettuceGraph = new LettuceGraph("redis://localhost");
    }

    public void load(Object object) {
        GraphCommands graph = lettuceGraph.getCommands();
        String query = cypherizer.cypherize(object);
        List result = graph.query(key, query);
        System.out.println(result);
    }
}
