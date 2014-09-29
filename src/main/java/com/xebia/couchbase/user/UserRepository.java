package com.xebia.couchbase.user;

import com.google.gson.Gson;
import com.xebia.couchbase.Configuration;
import net.spy.memcached.CASValue;
import net.spy.memcached.PersistTo;
import net.spy.memcached.internal.OperationFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.xebia.couchbase.Configuration.PUBLICOTAURUS_CLIENT;

public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    public static final String USER_DOCUMENT_PREFIX = "user::v1::";
    private final Gson gson;

    public UserRepository() {
        gson = new Gson();
    }

    //TODO Exercice 3
    public void insertUser(User user) throws Exception {
        final UserProfile userProfile = user.getUserProfile();
        OperationFuture<Boolean> future;
        do {
            final String userDocumentId = String.format("%s%s_%s", USER_DOCUMENT_PREFIX, userProfile.getFirstName().toLowerCase(), userProfile.getLastName().toLowerCase());
            future = PUBLICOTAURUS_CLIENT.set(userDocumentId, gson.toJson(user));
        } while (!future.get());
    }

    //TODO Exercice 3, puis Exercice 5
    public User findUser(String id) throws java.io.IOException {
        final User user = documentToJson(PUBLICOTAURUS_CLIENT.get(USER_DOCUMENT_PREFIX + id));
        PUBLICOTAURUS_CLIENT.incr("user_document_retrieval_count", 1, 0);
        return user;
    }

    //TODO Exercice 4
    public UserWithCas findUserWithCas(String id) throws IOException {
        final CASValue<Object> documentWithCas = findDocumentWithCas(id);
        final User user = gson.getAdapter(User.class).fromJson(
                documentWithCas.getValue().toString());

        return new UserWithCas(user, documentWithCas.getCas());
    }

    //TODO Exercice 5
    public void updateUser(String key, long casId, User user) {
        Configuration.PUBLICOTAURUS_CLIENT.cas(key, casId, gson.toJson(user), PersistTo.MASTER);
    }

    //TODO Exercice 6
    public Collection<User> findAll(Collection<String> userKeys) {
        List<User> result = new ArrayList<>();

        for (Object userObject : Configuration.PUBLICOTAURUS_CLIENT.getBulk(userKeys).values()) {
            result.add(gson.fromJson(userObject.toString(), User.class));
        }

        return result;
    }

    private User documentToJson(Object document) throws java.io.IOException {
        return gson.getAdapter(User.class).fromJson(document.toString());
    }

    private static CASValue<Object> findDocumentWithCas(String id) {
        return PUBLICOTAURUS_CLIENT.gets(id);
    }
}
