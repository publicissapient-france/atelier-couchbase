package com.xebia.couchbase.user;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.CASMismatchException;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.couchbase.client.java.transcoder.JsonTranscoder;
import com.google.gson.Gson;
import com.xebia.couchbase.Configuration;
import rx.Observable;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserRepository {
    public static final String USER_DOCUMENT_PREFIX = "user::v1::";
    private final Gson gson;
    private final JsonTranscoder jsonTranscoder;
    private final CounterRepository counterRepository;

    public UserRepository() {
        jsonTranscoder = new JsonTranscoder();
        gson = new Gson();
        counterRepository = new CounterRepository();
    }

    public JsonDocument userToDocument(User user) {
        final JsonObject userJsonObject;
        try {
            userJsonObject = jsonTranscoder.stringToJsonObject(gson.toJson(user));
            final UserProfile userProfile = user.getUserProfile();
            final String userDocumentId = String.format("%s%s_%s", USER_DOCUMENT_PREFIX, userProfile.getFirstName().toLowerCase(), userProfile.getLastName().toLowerCase());
            return JsonDocument.create(userDocumentId, userJsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public JsonDocument getAndLock(String userId) {
        return Configuration.publicotaurusBucket().getAndLock(USER_DOCUMENT_PREFIX + userId, 5);
    }

    public JsonDocument findUser(String userId) {
        counterRepository.incrementUserDocumentRetrieval();
        return Configuration.publicotaurusBucket().get(userId);
    }

//    public void insertBulkOfUsers(List<User> users) throws Exception {
//        AtomicInteger counter = new AtomicInteger(0);
//        final List<JsonDocument> userDocuments = users.stream().map(this::userToDocument).collect(Collectors.toList());
//        final List<Observable<JsonDocument>> userDocumentObservables = userDocuments.stream().map(Observable::just).collect(Collectors.toList());
//        userDocumentObservables.forEach(
//                userDocumentObservable -> userDocumentObservable.flatMap(document -> Configuration.publicotaurusBucket().async().insert(document))
//                        .retry((integer, throwable) -> {
//                            final String name = throwable.getClass().getSimpleName();
//                            System.out.println(name + " count : " + counter.incrementAndGet());
//                            return !name.equals("DocumentAlreadyExistsException");
//                        }).subscribe()
//        );
//    }

//    public void insertBulkOfUsers(List<User> users) throws Exception {
//        final List<JsonDocument> userDocuments = users.stream().map(this::userToDocument).collect(Collectors.toList());
//        final List<Observable<JsonDocument>> userDocumentObservables = userDocuments.stream().map(Observable::just).collect(Collectors.toList());
//        userDocumentObservables.forEach(
//                userDocumentObservable -> userDocumentObservable.flatMap(document -> Configuration.publicotaurusBucket().async().insert(document))
//                        .retryWhen(attempts ->
//                            attempts.flatMap(attempt -> {
//                                if (attempt.getThrowable() instanceof DocumentAlreadyExistsException) {
//                                    return Observable.never();
//                                }
//                                final Observable<Integer> integerObservable = attempts.zipWith(Observable.range(1, 5), (n, i) -> i);
//                                return integerObservable.flatMap(i -> {
//                                    System.out.println("delay retry by " + i + " second(s)");
//                                    return Observable.timer(i*3, TimeUnit.SECONDS);
//                                });
//                            })
//                        ).subscribe()
//        );
//    }

    public void insertBulkOfUsers(List<User> users) throws Exception {
        AtomicInteger counter = new AtomicInteger();
        final List<JsonDocument> userDocuments = users.stream().map(this::userToDocument).collect(Collectors.toList());
        final List<Observable<JsonDocument>> userDocumentObservables = userDocuments.stream().map(Observable::just).collect(Collectors.toList());
        userDocumentObservables.forEach(
                userDocumentObservable -> userDocumentObservable.flatMap(document -> Configuration.publicotaurusBucket().async().insert(document))
                        .retryWhen(attempts ->
                                        attempts.flatMap(attempt -> {
                                            final Throwable throwable = attempt.getThrowable();
                                            if (throwable instanceof DocumentAlreadyExistsException) {
                                                System.out.println(throwable.getClass().getSimpleName() + " : " + counter.incrementAndGet());
                                                return Observable.never();
                                            }
                                            final Observable<Integer> integerObservable = attempts.zipWith(Observable.range(1, 5), (n, i) -> i);
                                            return integerObservable.flatMap(i -> {
                                                System.out.println("delay retry by " + i + " second(s)");
                                                return Observable.timer(i * 3, TimeUnit.SECONDS);
                                            });
                                        })
                        ).subscribe()
        );
    }

//    public void insertBulkOfUsers(List<User> users) throws Exception {
//        final Collection<JsonDocument> userDocuments = users.stream()
//                .map(this::userToDocument).collect(Collectors.toList());
//        Observable.from(userDocuments).flatMap(
//                document -> Configuration.publicotaurusBucket().async()
//                        .insert(document)
//        ).retryWhen(attempts -> attempts.flatMap(attempt -> {
//                    final Observable<Integer> integerObservable =
//                            attempts.zipWith(Observable.range(1, 5), (n, i) -> i);
//                    return integerObservable.flatMap(i -> {
//                        System.out.println("delay retry by " + i + " second(s)");
//                        return Observable.timer(i * 3, TimeUnit.SECONDS);
//                    });
//                })
//        ).subscribe();
//    }
}
