package com.guyroyse.ephemeral.graph;

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
    public void it_cypherizes_an_object_without_properties() {

        @Graphable("thing")
        class Thing {}

        Thing thing = new Thing();

        String s = subject.cypherize(thing);

        assertThat(s, is("CREATE (n:thing)"));

    }

    @Test
    public void it_cypherizes_an_object_with_a_string_property() {

        @Graphable("thing")
        class Thing {
            @GraphString("name")
            public String getName() {
                return "swamp";
            }
        }

        assertThat(subject.cypherize(new Thing()), is("CREATE (n:thing) SET n.name = 'swamp'"));
    }

    @Test
    public void it_cypherizes_an_object_with_an_integer_property() {

        @Graphable("thing")
        class Thing {
            @GraphInt("age")
            public int getAge() {
                return 2;
            }
        }

        assertThat(subject.cypherize(new Thing()), is("CREATE (n:thing) SET n.age = 2"));
    }

    @Test
    public void it_cypherizes_an_object_with_varied_properties() {

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

        assertThat(subject.cypherize(new Thing()), is("CREATE (n:user) SET n.name = 'Bob', n.age = 106"));
    }
}
