package com.xebia.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

public class Configuration {

    //TODO change this url to run it on your EC2 instance
    public static final String COUCHBASE_SERVER_ADDRESS = "127.0.0.1";
    public static final CouchbaseEnvironment COUCHBASE_ENVIRONMENT = getCouchbaseEnvironment();
    public static final String PUBLICOTAURUS_BUCKET_NAME = "publicotaurus";
    private static Bucket PUBLICOTAURUS_BUCKET = null;
    public static CouchbaseCluster couchbaseCluster = CouchbaseCluster.create(COUCHBASE_ENVIRONMENT, COUCHBASE_SERVER_ADDRESS);
    public static final String DEFAULT_BUCKET_NAME = "default";
    private static Bucket DEFAULT_BUCKET = null;

    private static DefaultCouchbaseEnvironment getCouchbaseEnvironment() {
        return DefaultCouchbaseEnvironment.builder().connectTimeout(10_000).disconnectTimeout(10_000).kvTimeout(10_000)
                .managementTimeout(10_000).queryTimeout(10_000).build();
    }

    public static Bucket publicotaurusBucket() {
        return getBucketOfName(PUBLICOTAURUS_BUCKET, PUBLICOTAURUS_BUCKET_NAME);
    }

    public static void reinitConnection() {
        couchbaseCluster = CouchbaseCluster.create(COUCHBASE_ENVIRONMENT, COUCHBASE_SERVER_ADDRESS);
    }

    public static Bucket defaultBucket() {
        return getBucketOfName(DEFAULT_BUCKET, DEFAULT_BUCKET_NAME);
    }

    public static Bucket getBucketOfName(Bucket bucket, String bucketName) {
        if (bucket == null) {
            bucket = couchbaseCluster.openBucket(bucketName);
        }

        return bucket;
    }

}
