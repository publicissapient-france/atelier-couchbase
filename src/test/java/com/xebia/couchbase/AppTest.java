package com.xebia.couchbase;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    //TODO change this url to run it on your EC2 instance
    public static final String COUCHBASE_SERVER_ADDRESS = "127.0.0.1";

    @Test
    public void should_get_hello_world_document() {
        final Cluster couchbaseCluster = new CouchbaseCluster(COUCHBASE_SERVER_ADDRESS);
        final Bucket defaultBucket = couchbaseCluster.openBucket().toBlocking().single();
        final JsonDocument testJsonDocument = defaultBucket.get("hello_couchbase").toBlocking().single();

        assertThat(testJsonDocument.content().get("hello")).isEqualTo("world");
    }
}
