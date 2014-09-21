package com.xebia.couchbase;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {

    @Test
    public void should_get_hello_world_document() {
        final Cluster couchbaseCluster = CouchbaseCluster.create(Configuration.COUCHBASE_ENVIRONMENT, Configuration.COUCHBASE_SERVER_ADDRESS);
        final Bucket defaultBucket = couchbaseCluster.openBucket();
        final JsonDocument testJsonDocument = defaultBucket.get("hello_couchbase");

        assertThat(testJsonDocument.content().get("hello")).isEqualTo("world");
    }
}
