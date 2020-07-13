package com.guyroyse.ephemeral.graph;

import com.guyroyse.ephemeral.graph.annotations.GraphID;
import com.guyroyse.ephemeral.graph.annotations.GraphInt;
import com.guyroyse.ephemeral.graph.annotations.GraphString;
import com.guyroyse.ephemeral.graph.annotations.Graphable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("Cypherizer")
public class CypherizerTests {

    private Cypherizer subject;

    @BeforeEach
    public void setup() {
        subject = new Cypherizer();
    }

    @Test
    public void it_cypherizes_an_object_with_just_an_id_and_no_properties() {
        String expected = "CREATE (n:thing) SET n.__id = 'foo'";
        String actual = subject.cypherize(new ThingWithID());
        assertThat(actual, is(expected));
    }

    @Graphable("thing")
    public class ThingWithID {
        @GraphID
        public String getID() {
            return "foo";
        }
    }

    @Test
    public void it_cypherizes_an_object_with_an_id_and_a_string_property() {
        String expected = "CREATE (n:thing) SET n.__id = 'foo', n.name = 'swamp'";
        String actual = subject.cypherize(new ThingWithIdAndString());
        assertThat(actual, is(expected));
    }

    @Graphable("thing")
    public class ThingWithIdAndString {
        @GraphID
        public String getID() {
            return "foo";
        }

        @GraphString("name")
        public String getName() {
            return "swamp";
        }
    }

    @Test
    public void it_cypherizes_an_object_with_an_id_and_a_string_property_that_are_the_same() {
        String expected = "CREATE (n:thing) SET n.__id = 'swamp', n.name = 'swamp'";
        String actual = subject.cypherize(new ThingWithSameIdAndString());
        assertThat(actual, is(expected));
    }

    @Graphable("thing")
    public class ThingWithSameIdAndString {
        @GraphID
        @GraphString("name")
        public String getName() {
            return "swamp";
        }
    }

    @Test
    public void it_cypherizes_an_object_with_an_id_and_an_integer_property() {
        String expected = "CREATE (n:thing) SET n.__id = 'foo', n.age = 2";
        String actual = subject.cypherize(new ThingWithIdAndInt());
        assertThat(actual, is(expected));
    }

    @Graphable("thing")
    public class ThingWithIdAndInt {
        @GraphID
        public String getID() {
            return "foo";
        }

        @GraphInt("age")
        public int getAge() {
            return 2;
        }
    }

    @Test
    public void it_cypherizes_an_object_with_all_the_things() {
        String expected = "CREATE (n:user) SET n.__id = 'foo', n.age = 106, n.name = 'Bob'";
        String actual = subject.cypherize(new ThingWithItAll());
        assertThat(actual, is(expected));
    }

    @Graphable("user")
    public class ThingWithItAll {
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
}
