package com.xebia.couchbase;

import com.couchbase.client.CouchbaseClient;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Configuration {

    //TODO change this url to run it on your EC2 instance
    public static final String ip = "localhost";
    public static final CouchbaseClient COUCHBASE_CLIENT = initClient();
    public static final String PUBLICOTAURUS_BUCKET_NAME = "publicotaurus";

    public static CouchbaseClient initClient() {
        final List<URI> uris;
        try {
            uris = Lists.newArrayList(new URI("http://" + ip + "/pools"));
            return new CouchbaseClient(uris, PUBLICOTAURUS_BUCKET_NAME, "");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
