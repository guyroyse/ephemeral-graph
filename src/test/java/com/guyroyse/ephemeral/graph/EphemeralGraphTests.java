package com.guyroyse.ephemeral.graph;

import com.guyroyse.ephemeral.graph.annotations.GraphID;
import com.guyroyse.ephemeral.graph.annotations.GraphInt;
import com.guyroyse.ephemeral.graph.annotations.GraphString;
import com.guyroyse.ephemeral.graph.annotations.Graphable;
import com.guyroyse.ephemeral.graph.lettuce.GraphCommands;
import com.guyroyse.ephemeral.graph.lettuce.LettuceGraph;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.mockito.Mockito.*;

@DisplayName("EphemeralGraph")
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

    @Nested
    @DisplayName("#load")
    public class Load {

        @Test
        public void it_loads_an_object_into_redis_graph() {
            subject.load(new Thing("1", "Bob", 106));

            String key = "some_graph";
            String query = "CREATE (n:thing) SET n.__id = '1', n.age = 106, n.name = 'Bob'";

            verify(mockGraph).query(key, query);
        }

        @Test
        public void it_loads_multiple_objects_into_redis_graph() {
            subject.load(
                    new Thing("1", "Thing 1", 1),
                    new Thing("2", "Things 2", 2),
                    new Cat("3", "The Cat in the Hat", "About That"));

            String key = "some_graph";
            String query1 = "CREATE (n:thing) SET n.__id = '1', n.age = 1, n.name = 'Thing 1'";
            String query2 = "CREATE (n:thing) SET n.__id = '2', n.age = 2, n.name = 'Thing 2'";
            String query3 = "CREATE (n:cat) SET n.__id = '3', n.knows = 'About That', n.name = 'The Cat in the Hat'";

            verify(mockGraph, times(3)).query(eq(key), anyString());
            verify(mockGraph, atMostOnce()).query(eq(key), eq(query1));
            verify(mockGraph, atMostOnce()).query(eq(key), eq(query2));
            verify(mockGraph, atMostOnce()).query(eq(key), eq(query3));
        }

        @Test
        public void it_loads_multiple_objects_into_redis_graph_without_duplicates() {

            Thing thing1 = new Thing("1", "Thing 1", 1);
            Thing thing2 = new Thing("2", "Things 2", 2);
            Thing otherThing2 = new Thing("2", "Thing 2", 2);
            Cat catInTheCat = new Cat("3", "The Cat in the Hat", "About That");

            subject.load(thing1, thing2, otherThing2, catInTheCat);

            String key = "some_graph";
            String query1 = "CREATE (n:thing) SET n.__id = '1', n.age = 1, n.name = 'Thing 1'";
            String query2 = "CREATE (n:thing) SET n.__id = '2', n.age = 2, n.name = 'Thing 2'";
            String query3 = "CREATE (n:cat) SET n.__id = '3', n.knows = 'About That', n.name = 'The Cat in the Hat'";

            verify(mockGraph, times(3)).query(eq(key), anyString());
            verify(mockGraph, atMostOnce()).query(eq(key), eq(query1));
            verify(mockGraph, atMostOnce()).query(eq(key), eq(query2));
            verify(mockGraph, atMostOnce()).query(eq(key), eq(query3));
        }
    }

    @Nested
    @Disabled
    @DisplayName("#query")
    public class Query {

        @Test
        public void it_queries_multiple_objects() {

            Thing thing1 = new Thing("1", "Thing 1", 1);
            Thing thing2 = new Thing("2", "Things 2", 2);
            Thing otherThing2 = new Thing("2", "Thing 2", 2);
            Cat catInTheCat = new Cat("3", "The Cat in the Hat", "About That");

            subject.load(thing1, thing2, otherThing2, catInTheCat);

            List<Object> results = subject.query("MATCH (n:thing) RETURN n");

            assertThat(results.get(0), is(thing1));
            assertThat(results.get(1), is(thing2));
        }
    }

    @Graphable("thing")
    public class Thing {
        private String id;
        private String name;
        private int age;

        public Thing(String id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        @GraphID
        public String getID() {
            return id;
        }

        @GraphString("name")
        public String getName() {
            return name;
        }

        @GraphInt("age")
        public int getAge() {
            return age;
        }
    }

    @Graphable("cat")
    public class Cat {
        private String id;
        private String name;
        private String knows;

        public Cat(String id, String name, String knows) {
            this.id = id;
            this.name = name;
            this.knows = knows;
        }

        @GraphID
        public String getID() {
            return id;
        }

        @GraphString("name")
        public String getName() {
            return name;
        }

        @GraphString("knows")
        public String getKnows() {
            return knows;
        }
    }
}
