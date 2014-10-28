package com.xebia.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

public class Configuration {

    //TODO Exercise 1.1
    // Change this url to run it on your EC2 instance
    public static final String COUCHBASE_SERVER_ADDRESS = "127.0.0.1";
    public static final CouchbaseEnvironment COUCHBASE_ENVIRONMENT = getCouchbaseEnvironment();

    private static final String DEFAULT_BUCKET_NAME = "default";
    private static Bucket DEFAULT_BUCKET = null;

    public static final String PUBLICOTAURUS_BUCKET_NAME = "publicotaurus";
    private static Bucket PUBLICOTAURUS_BUCKET = null;
    private static CouchbaseCluster couchbaseCluster = CouchbaseCluster.create(COUCHBASE_ENVIRONMENT, COUCHBASE_SERVER_ADDRESS);

    private static DefaultCouchbaseEnvironment getCouchbaseEnvironment() {
        return DefaultCouchbaseEnvironment.builder().connectTimeout(10_000).disconnectTimeout(10_000).kvTimeout(10_000)
                .managementTimeout(10_000).queryTimeout(10_000).build();
    }

    public static Bucket publicotaurusBucket() {
        return getBucketOfName(PUBLICOTAURUS_BUCKET, PUBLICOTAURUS_BUCKET_NAME);
    }

    //TODO Exercise 1.2
    public static void reinitConnection() {
        // Create the connection to the cluster using CouchbaseCluster.create() method.
        couchbaseCluster = CouchbaseCluster.create(COUCHBASE_ENVIRONMENT, COUCHBASE_SERVER_ADDRESS);
    }

    public static Bucket defaultBucket() {
        return getBucketOfName(DEFAULT_BUCKET, DEFAULT_BUCKET_NAME);
    }

    /**
     * Get or init the connection with a bucket
     * @param bucket the bucket var to check if it is already initiated
     * @param bucketName the bucket name, to be able to initiate the connection
     * @return bucket
     */
    //TODO Exercise 1.3
    private static Bucket getBucketOfName(Bucket bucket, String bucketName) {
        // Should return the PUBLICOTAURUS_BUCKET constant and init it if needed
        if (bucket == null) {
            bucket = couchbaseCluster.openBucket(bucketName);
        }

        return bucket;
    }

}
