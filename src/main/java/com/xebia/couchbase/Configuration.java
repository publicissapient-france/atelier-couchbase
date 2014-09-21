package com.xebia.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

public class Configuration {

    //TODO change this url to run it on your EC2 instance
    public static final String COUCHBASE_SERVER_ADDRESS = "127.0.0.1";
    public static final CouchbaseEnvironment COUCHBASE_ENVIRONMENT =
            DefaultCouchbaseEnvironment.builder().build();

    public static final String PUBLICOTAURUS_BUCKET = "publicotaurus";
    public static final CouchbaseCluster couchbaseCluster = CouchbaseCluster.create(COUCHBASE_ENVIRONMENT, COUCHBASE_SERVER_ADDRESS);
    public static final Bucket userBucket = Configuration.couchbaseCluster.openBucket(Configuration.PUBLICOTAURUS_BUCKET);
}
