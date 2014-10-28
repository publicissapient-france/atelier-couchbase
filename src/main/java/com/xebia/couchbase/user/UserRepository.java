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
    public static final String USER_DOCUMENT_PREFIX = "user::";
    private final Gson gson;
    private final JsonTranscoder jsonTranscoder;
    private final CounterRepository counterRepository;

    public UserRepository() {
        jsonTranscoder = new JsonTranscoder();
        gson = new Gson();
        counterRepository = new CounterRepository();
    }

    //TODO Exercise 3.2
    public void insertUser(User user) throws Exception {
        // get the user JsonDocument thanks to the userToDocument() method.
        final JsonDocument userJsonDocument = userToDocument(user);

        try {
            //Insert the document thanks to the Configuration.publicotaurusBucket()
            Configuration.publicotaurusBucket().insert(userJsonDocument);
        } catch (CASMismatchException e) {
            //Test should be ok if document has already been inserted, so nothing to do here
        }
    }
    //TODO Exercise 3.3
    public JsonDocument findUser(String firstName, String lastName) {
        //TODO Exercise 5.2
        counterRepository.incrementUserDocumentRetrieval();
        return Configuration.publicotaurusBucket().get(computeUserId(firstName, lastName));
    }

    //TODO Exercise 4a
    public void updateUser(JsonDocument user) {
        Configuration.publicotaurusBucket().replace(user);
    }

    //TODO Exercise 4b
    public JsonDocument getAndLock(String firstName, String lastName) {
        return Configuration.publicotaurusBucket().getAndLock(computeUserId(firstName, lastName), 5);
    }

    //TODO Exercise 6.2
    public void insertBulkOfUsers(List<User> users) throws Exception {
        for (User user : users) {
            try {
                upsertUser(user);
            } catch (Exception e) {
                System.out.println("Client planté. Réinitialisation de la connexion à la base.");
                reinitConnection();
                // Reprise du document planté après réinitialisation de la connexion
                upsertUser(user);
            }
        }
    }

    //TODO Exercise 6.1
    private void upsertUser(User user) {
        Configuration.publicotaurusBucket().upsert(userToDocument(user));
    }

    private String computeUserId(String firstName, String lastName) {
        return String.format("%s%s_%s", USER_DOCUMENT_PREFIX, firstName.toLowerCase(), lastName.toLowerCase());
    }

    //TODO Exercise 3.1
    private JsonDocument userToDocument(User user) {
        // Transform user to document thanks to a Gson object and a JsonTranscoder object
        // (both available in this class)
        final JsonObject userJsonObject;
        try {
            userJsonObject = jsonTranscoder.stringToJsonObject(gson.toJson(user));
            final UserProfile userProfile = user.getUserProfile();
            final String userDocumentId = computeUserId(userProfile.getFirstName(), userProfile.getLastName());
            return JsonDocument.create(userDocumentId, userJsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
