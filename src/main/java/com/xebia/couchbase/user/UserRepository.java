package com.xebia.couchbase.user;

import com.google.gson.Gson;
import com.xebia.couchbase.Configuration;
import net.spy.memcached.OperationTimeoutException;
import net.spy.memcached.compat.log.LoggerFactory;

import java.io.IOException;

import static com.xebia.couchbase.Configuration.COUCHBASE_CLIENT;

public class UserRepository {
    public static final String USER_DOCUMENT_PREFIX = "user::v1::";
    private final Gson gson;
    private final CounterRepository counterRepository;

    public UserRepository() {
        gson = new Gson();
        counterRepository = new CounterRepository();
    }

    public void insertUser(User user) throws Exception {
        final UserProfile userProfile = user.getUserProfile();
        final String userDocumentId = String.format("%s%s_%s", USER_DOCUMENT_PREFIX, userProfile.getFirstName().toLowerCase(), userProfile.getLastName().toLowerCase());
        Configuration.COUCHBASE_CLIENT.set(userDocumentId, gson.toJson(user));
    }

    public void updateUser(String key, long casId, User user) {
        Configuration.COUCHBASE_CLIENT.cas(key, casId, gson.toJson(user));
    }

    public User findUser(String id) throws java.io.IOException {
        final User user = documentToJson(COUCHBASE_CLIENT.get(USER_DOCUMENT_PREFIX + id));
        COUCHBASE_CLIENT.incr("user_document_retrieval_count", 1, 0);
        return user;
    }

    private User documentToJson(Object document) throws java.io.IOException {
        return gson.getAdapter(User.class).fromJson(document.toString());
    }

    public User getAndLock(String userId) {
        try {
            final Object userDocument = COUCHBASE_CLIENT.getAndLock(USER_DOCUMENT_PREFIX + userId, 5).getValue();
            LoggerFactory.getLogger(UserRepository.class).error(userDocument);
            return userDocument == null ? null : documentToJson(userDocument);
        } catch (OperationTimeoutException | IOException e) {
            // Doing nothing
            return null;
        }
    }
}
