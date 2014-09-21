package com.xebia.couchbase.user;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.CASMismatchException;
import com.couchbase.client.java.transcoder.JsonTranscoder;
import com.google.gson.Gson;
import com.xebia.couchbase.Configuration;

public class UserRepository {
    public static final String USER_DOCUMENT_PREFIX = "user::v1::";
    private final Gson gson;
    private final JsonTranscoder jsonTranscoder;

    public UserRepository() {
        jsonTranscoder = new JsonTranscoder();
        gson = new Gson();
    }

    public void insertUser(User user) throws Exception {
        final JsonObject userJsonObject = jsonTranscoder.stringToJsonObject(gson.toJson(user));
        final UserProfile userProfile = user.getUserProfile();
        final String userDocumentId = String.format("%s%s_%s", USER_DOCUMENT_PREFIX, userProfile.getFirstName().toLowerCase(), userProfile.getLastName().toLowerCase());
        final JsonDocument userJsonDocument = JsonDocument.create(userDocumentId, userJsonObject);

        try {
            Configuration.userBucket.insert(userJsonDocument);
        } catch (CASMismatchException e) {
            //Test should be ok if document has already been inserted
        }
    }

    public void updateUser(JsonDocument user) {
        Configuration.userBucket.replace(user);
    }

    public JsonDocument getAndLock(String userId) {
        return Configuration.userBucket.getAndLock(USER_DOCUMENT_PREFIX + userId, 5);
    }

    public JsonDocument findUser(String userId) {
        return Configuration.userBucket.get(userId);
    }
}
