package com.guyroyse.ephemeral.graph;

import com.guyroyse.ephemeral.graph.annotations.GraphInt;
import com.guyroyse.ephemeral.graph.annotations.GraphString;
import com.guyroyse.ephemeral.graph.annotations.Graphable;
import com.guyroyse.ephemeral.graph.lettuce.GraphCommands;
import com.guyroyse.ephemeral.graph.lettuce.LettuceGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class EphemeralGraphTests {

    private EphemeralGraph subject;
    private GraphCommands mockGraph;

    @BeforeEach
    public void setup() {
        LettuceGraph radishGraph = mock(LettuceGraph.class);
        mockGraph = mock(GraphCommands.class);
        when(radishGraph.getCommands()).thenReturn(mockGraph);

        subject = new EphemeralGraph("some_graph");
        subject.lettuceGraph = radishGraph;
    }

    @Test
    public void it_loads_an_object_into_redis_graph() {

        @Graphable("user")
        class Thing {
            @GraphString("name")
            public String getName() {
                return "Bob";
            }

            @GraphInt("age")
            public int getAge() {
                return 106;
            }
        }

        subject.load(new Thing());

        verify(mockGraph).query("some_graph", "CREATE (n:user) SET n.name = 'Bob', n.age = 106");
    }
}
