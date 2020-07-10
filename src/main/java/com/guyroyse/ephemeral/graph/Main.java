package com.guyroyse.ephemeral.graph;

import com.guyroyse.ephemeral.graph.annotations.GraphInt;
import com.guyroyse.ephemeral.graph.annotations.GraphString;
import com.guyroyse.ephemeral.graph.annotations.Graphable;

import java.util.List;

public class Main {
    public static void main(String args[]) {

        @Graphable("thing")
        class Thing {
            @GraphString("name")
            public String getName() {
                return "Swamp Thing";
            }

            @GraphInt("quantity")
            public int getAge() {
                return 42;
            }
        }

        EphemeralGraph myGraph = new EphemeralGraph("my_graph");

        Thing thing = new Thing();
        List result = myGraph.load(thing);

        System.out.println(result);
    }
}
