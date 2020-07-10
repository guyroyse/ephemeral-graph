package com.guyroyse.ephemeral.graph.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.dynamic.RedisCommandFactory;

public class LettuceGraph {
    private RedisClient client;

    public LettuceGraph(String uri) {
        client = RedisClient.create("redis://localhost");
    }

    public GraphCommands getCommands() {
        RedisCommandFactory factory = new RedisCommandFactory(client.connect());
        factory.setVerifyCommandMethods(false);
        return factory.getCommands(GraphCommands.class);
    }
}
