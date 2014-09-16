package com.xebia.couchbase;

import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

public class Configuration {

    //TODO change this url to run it on your EC2 instance
    public static final String COUCHBASE_SERVER_ADDRESS = "127.0.0.1";
    public static final CouchbaseEnvironment COUCHBASE_ENVIRONMENT =
            DefaultCouchbaseEnvironment.builder().build();

}
