package com.xebia.couchbase.user;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.CASMismatchException;
import com.couchbase.client.java.transcoder.JsonTranscoder;
import com.google.gson.Gson;
import com.xebia.couchbase.Configuration;

import java.util.List;

import static com.xebia.couchbase.Configuration.reinitConnection;

public class UserRepository {
    private static final String USER_DOCUMENT_PREFIX = "user::";
    private final Gson gson;
    private final JsonTranscoder jsonTranscoder;
    private final CounterRepository counterRepository;

    public UserRepository() {
        jsonTranscoder = new JsonTranscoder();
        gson = new Gson();
        counterRepository = new CounterRepository();
    }

    //TODO Exercise 3.2
    public void insertUser(User user) {
    }
    //TODO Exercise 3.3
    //TODO Exercise 5.2
    public JsonDocument findUser(String firstName, String lastName) {
        return null;
    }

    //TODO Exercise 4a
    public void updateUser(JsonDocument user) {
    }

    //TODO Exercise 4b
    public JsonDocument getAndLock(String firstName, String lastName) {
        return null;
    }

    //TODO Exercise 6.2
    public void insertBulkOfUsers(List<User> users) {
    }

    //TODO Exercise 6.1
    private void upsertUser(User user) {
    }

    //TODO Exercise 3.1
    private JsonDocument userToDocument(User user) {
        // Transform user to document thanks to a Gson object and a JsonTranscoder object
        // (both available in this class)
        return null;
    }
}
