package com.xebia.couchbase;

import com.couchbase.client.CouchbaseClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class Configuration {

    //TODO Exercice 1 configure your client to use your default bucket
    public static final String ip = "adrastea-couchbase-workshop.aws.xebiatechevent.info";

    public static final String DEFAULT_BUCKET_NAME = "default";
    public static final CouchbaseClient DEFAULT_CLIENT = initClient(DEFAULT_BUCKET_NAME);

    public static final String PUBLICOTAURUS_BUCKET_NAME = "publicotaurus";
    public static final CouchbaseClient PUBLICOTAURUS_CLIENT = initClient(PUBLICOTAURUS_BUCKET_NAME);

    private static CouchbaseClient initClient(String bucketName) {
        final List<URI> uris;
        try {
            uris = newArrayList(new URI("http://" + ip + "/pools"));
            return new CouchbaseClient(uris, bucketName, "");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
