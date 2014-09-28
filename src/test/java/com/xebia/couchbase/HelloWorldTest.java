package com.xebia.couchbase;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloWorldTest {

    @Test
    public void should_get_hello_world_document() {
        final Object helloCouchbaseDocument = Configuration.DEFAULT_CLIENT.get("hello_couchbase");

        assertThat(helloCouchbaseDocument).isEqualTo("{\"hello\":\"world\"}");
    }
}
