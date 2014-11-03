package com.xebia.couchbase.user;

import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.transcoder.JsonTranscoder;
import com.google.gson.Gson;
import com.xebia.couchbase.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.functions.Action1;

public class AsyncUserRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncUserRepository.class);
    private static final AsyncBucket ASYNC_BUCKET = Configuration.publicotaurusBucket().async();
    private static final String USER_DOCUMENT_PREFIX = "user::";

    private final Gson gson;
    private final JsonTranscoder jsonTranscoder;
    private final CounterRepository counterRepository;

    public AsyncUserRepository() {
        gson = new Gson();
        jsonTranscoder = new JsonTranscoder();
        counterRepository = new CounterRepository();
    }

    public void insertUser(User user) {
        Observable
                .just(userToDocument(user))
                .flatMap(ASYNC_BUCKET::insert)
                .subscribe(new Action1<JsonDocument>() {
                    @Override
                    public void call(JsonDocument jsonDocument) {
                        LOGGER.debug("{} inserted", jsonDocument);
                    }
                });

    }

    private JsonDocument userToDocument(User user) {
        final JsonObject userJsonObject;
        try {
            userJsonObject = jsonTranscoder.stringToJsonObject(gson.toJson(user));
            final UserProfile userProfile = user.getUserProfile();
            final String userDocumentId = computeUserId(userProfile.getFirstName(), userProfile.getLastName());
            return JsonDocument.create(userDocumentId, userJsonObject);
        } catch (Exception e) {
            LOGGER.error("Unable to transform to JSON", e);
            return null;
        }
    }

    String computeUserId(String firstName, String lastName) {
        return String.format("%s%s_%s", USER_DOCUMENT_PREFIX, firstName.toLowerCase(), lastName.toLowerCase());
    }
}
