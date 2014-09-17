package com.xebia.couchbase.user;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.transcoder.JsonTranscoder;
import com.google.gson.Gson;
import com.xebia.couchbase.Configuration;

public class UserRepository {
    private CouchbaseCluster couchbaseCluster;
    private Bucket userBucket;
    private Gson gson;
    private final JsonTranscoder jsonTranscoder;

    public UserRepository() {
        couchbaseCluster = CouchbaseCluster.create(Configuration.COUCHBASE_ENVIRONMENT, Configuration.COUCHBASE_SERVER_ADDRESS);
        userBucket = couchbaseCluster.openBucket(Configuration.PUBLICOTAURUS_BUCKET).toBlocking().single();
        jsonTranscoder = new JsonTranscoder();
        gson = new Gson();
    }

    public void saveUser(User user) throws Exception {
        final JsonObject userJsonObject = jsonTranscoder.stringToJsonObject(gson.toJson(user));
        final JsonDocument userJsonDocument = JsonDocument.create(user.getIdentifier().toString(), userJsonObject);

        userBucket.upsert(userJsonDocument);
    }
}
