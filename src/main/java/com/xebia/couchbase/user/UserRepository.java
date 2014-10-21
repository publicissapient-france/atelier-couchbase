package com.xebia.couchbase.user;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.CASMismatchException;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
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

    public void insertUser(User user) throws Exception {
        final JsonDocument userJsonDocument = userToDocument(user);

        try {
            Configuration.publicotaurusBucket().insert(userJsonDocument);
        } catch (CASMismatchException e) {
            //Test should be ok if document has already been inserted
        }
    }

    public void updateUser(JsonDocument user) {
        Configuration.publicotaurusBucket().replace(user);
    }

    public JsonDocument getAndLock(String firstName, String lastName) {
        return Configuration.publicotaurusBucket().getAndLock(computeUserId(firstName, lastName), 5);
    }

    public JsonDocument findUser(String firstName, String lastName) {
        counterRepository.incrementUserDocumentRetrieval();
        return Configuration.publicotaurusBucket().get(computeUserId(firstName, lastName));
    }

    public void insertBulkOfUsers(List<User> users) throws Exception {
        for (User user : users) {
            try {
                safeInsertion(user);
            } catch (Exception e) {
                System.out.println("Client planté. Réinitialisation de la connexion à la base.");
                reinitConnection();
                // Reprise du document planté après réinitialisation de la connexion
                safeInsertion(user);
            }
        }
    }

    private void safeInsertion(User user) throws Exception {
        try {
            insertUser(user);
        } catch (DocumentAlreadyExistsException e) {
            System.out.println("Document déjà inséré. On continue...");
        }
    }

    private String computeUserId(String firstName, String lastName) {
        return String.format("%s%s_%s", USER_DOCUMENT_PREFIX, firstName.toLowerCase(), lastName.toLowerCase());
    }

    private JsonDocument userToDocument(User user) {
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
