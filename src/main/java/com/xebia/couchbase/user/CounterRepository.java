package com.xebia.couchbase.user;

public class CounterRepository {

    public static final String USER_DOCUMENT_RETRIEVAL_COUNT_ID = "user_document_retrieval_count";

    public void incrementUserDocumentRetrieval() {
//        PUBLICOTAURUS_BUCKET.counter(USER_DOCUMENT_RETRIEVAL_COUNT_ID, 1, 0);
    }

}
