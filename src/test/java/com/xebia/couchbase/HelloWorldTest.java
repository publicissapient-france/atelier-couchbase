package com.xebia.couchbase;


import com.couchbase.client.java.document.JsonDocument;
import org.junit.Test;

import static com.xebia.couchbase.Configuration.defaultBucket;
import static org.assertj.core.api.Assertions.assertThat;

public class HelloWorldTest {

    @Test
    //Exercise 1
    public void should_get_hello_world_document() {
        final JsonDocument testJsonDocument = defaultBucket().get("hello_couchbase");

        assertThat(testJsonDocument.content().get("hello")).isEqualTo("world");
    }
}
