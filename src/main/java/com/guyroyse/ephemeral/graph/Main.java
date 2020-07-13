package com.guyroyse.ephemeral.graph;

import com.guyroyse.ephemeral.graph.annotations.GraphID;
import com.guyroyse.ephemeral.graph.annotations.GraphInt;
import com.guyroyse.ephemeral.graph.annotations.GraphString;
import com.guyroyse.ephemeral.graph.annotations.Graphable;

import java.util.List;

public class Main {
    public static void main(String args[]) {

        @Graphable("thing")
        class Thing {
            @GraphID
            public String getID() {
                return "1";
            }

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
        myGraph.load(thing);

        List results = myGraph.query("MATCH (n) RETURN n");
        System.out.println(results);
    }
}
