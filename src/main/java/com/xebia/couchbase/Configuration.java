package com.xebia.couchbase;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.CouchbaseConnectionFactory;
import com.couchbase.client.CouchbaseConnectionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class Configuration {

    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

    //TODO Exercice 1 configure your client to use your default bucket
    public static final String ip = "adrastea-couchbase-workshop.aws.xebiatechevent.info";

    public static final String DEFAULT_BUCKET_NAME = "default";
    public static final CouchbaseClient DEFAULT_CLIENT = initClient(DEFAULT_BUCKET_NAME);

    public static final String PUBLICOTAURUS_BUCKET_NAME = "publicotaurus";
    public static final CouchbaseClient PUBLICOTAURUS_CLIENT = initClient(PUBLICOTAURUS_BUCKET_NAME);

    //TODO Exercice 1 configure your client to use your default bucket
    private static CouchbaseClient initClient(String bucketName) {
        try {
            final CouchbaseConnectionFactoryBuilder connectionFactoryBuilder = new CouchbaseConnectionFactoryBuilder();
            connectionFactoryBuilder.setOpTimeout(10_000);
            connectionFactoryBuilder.setOpQueueMaxBlockTime(5000);
            final List<URI> uris = newArrayList(new URI("http://" + ip + "/pools"));

            return new CouchbaseClient(connectionFactoryBuilder.buildCouchbaseConnection(uris, bucketName, ""));
        } catch (URISyntaxException | IOException e) {
            logger.error("Erreur lors de l'initialisation de la connexion au serveur Couchbase", e);
        }

        return null;
    }

    private static CouchbaseConnectionFactory initCouchbaseFactory() {

        return null;
    }

}
