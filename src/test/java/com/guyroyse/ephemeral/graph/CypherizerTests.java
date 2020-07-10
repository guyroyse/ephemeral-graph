package com.guyroyse.ephemeral.graph;

import com.guyroyse.ephemeral.graph.annotations.GraphID;
import com.guyroyse.ephemeral.graph.annotations.GraphInt;
import com.guyroyse.ephemeral.graph.annotations.GraphString;
import com.guyroyse.ephemeral.graph.annotations.Graphable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CypherizerTests {

    private Cypherizer subject;

    @BeforeEach
    public void setup() {
        subject = new Cypherizer();
    }

    @Test
    public void it_cypherizes_an_object_with_just_an_id_and_no_properties() {

        @Graphable("thing")
        class Thing {
            @GraphID
            public String getID() {
                return "foo";
            }
        }

        String expected = "CREATE (n:thing) SET n.__id = 'foo'";
        String actual = subject.cypherize(new Thing());

        assertThat(actual, is(expected));
    }

    @Test
    public void it_cypherizes_an_object_with_an_id_and_a_string_property() {

        @Graphable("thing")
        class Thing {
            @GraphID
            public String getID() {
                return "foo";
            }

            @GraphString("name")
            public String getName() {
                return "swamp";
            }
        }

        String expected = "CREATE (n:thing) SET n.__id = 'foo', n.name = 'swamp'";
        String actual = subject.cypherize(new Thing());

        assertThat(actual, is(expected));
    }

    @Test
    public void it_cypherizes_an_object_with_an_id_and_a_string_property_that_are_the_same() {

        @Graphable("thing")
        class Thing {
            @GraphID
            @GraphString("name")
            public String getName() {
                return "swamp";
            }
        }

        String expected = "CREATE (n:thing) SET n.__id = 'swamp', n.name = 'swamp'";
        String actual = subject.cypherize(new Thing());

        assertThat(actual, is(expected));
    }

    @Test
    public void it_cypherizes_an_object_with_an_id_and_an_integer_property() {

        @Graphable("thing")
        class Thing {
            @GraphID
            public String getID() {
                return "foo";
            }

            @GraphInt("age")
            public int getAge() {
                return 2;
            }
        }

        String expected = "CREATE (n:thing) SET n.__id = 'foo', n.age = 2";
        String actual = subject.cypherize(new Thing());

        assertThat(actual, is(expected));
    }

    @Test
    public void it_cypherizes_an_object_with_all_the_things() {

        @Graphable("user")
        class Thing {
            @GraphID
            public String getID() {
                return "foo";
            }

            @GraphString("name")
            public String getName() {
                return "Bob";
            }

            @GraphInt("age")
            public int getAge() {
                return 106;
            }
        }

        String expected = "CREATE (n:user) SET n.__id = 'foo', n.age = 106, n.name = 'Bob'";
        String actual = subject.cypherize(new Thing());

        assertThat(actual, is(expected));
    }
}
