package com.xebia.couchbase;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import org.junit.Test;
import rx.Observable;

import static org.assertj.core.api.Assertions.*;

public class AppTest {

    @Test
    public void testApp() {
        final Cluster couchbaseCluster = new CouchbaseCluster("127.0.0.1");
        final Bucket defaultBucket = couchbaseCluster.openBucket().toBlocking().single();
        final JsonDocument testJsonDocument = defaultBucket.get("test").toBlocking().single();

        assertThat(testJsonDocument.content().get("cle")).isEqualTo("valeur");
    }
}
