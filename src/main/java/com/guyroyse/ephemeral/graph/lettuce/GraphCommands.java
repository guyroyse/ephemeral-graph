package com.guyroyse.ephemeral.graph.lettuce;

import io.lettuce.core.dynamic.Commands;
import io.lettuce.core.dynamic.annotation.Command;
import io.lettuce.core.dynamic.annotation.Key;
import io.lettuce.core.dynamic.annotation.Value;

import java.util.List;

public interface GraphCommands extends Commands {
    @Command("GRAPH.QUERY")
    List<Object> query(@Key String key, @Value String query);
}
