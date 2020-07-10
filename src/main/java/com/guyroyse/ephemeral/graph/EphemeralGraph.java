package com.guyroyse.ephemeral.graph;

import com.guyroyse.ephemeral.graph.lettuce.GraphCommands;
import com.guyroyse.ephemeral.graph.lettuce.LettuceGraph;

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

    public List load(Object object) {
        GraphCommands graph = lettuceGraph.getCommands();
        String query = cypherizer.cypherize(object);
        return graph.query(key, query);
    }
}
