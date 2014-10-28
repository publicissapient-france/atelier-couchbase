package com.xebia.couchbase.user;

import static com.xebia.couchbase.Configuration.publicotaurusBucket;

public class CounterRepository {

    private static final String USER_DOCUMENT_RETRIEVAL_COUNT_ID = "user_document_retrieval_count";

    //TODO Exercice 5.1
    public void incrementUserDocumentRetrieval() {
        publicotaurusBucket().counter(USER_DOCUMENT_RETRIEVAL_COUNT_ID, 1, 0);
    }

}
