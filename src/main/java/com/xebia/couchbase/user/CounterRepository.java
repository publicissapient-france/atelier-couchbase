package com.xebia.couchbase.user;

import static com.xebia.couchbase.Configuration.publicotaurusBucket;

public class CounterRepository {

    public static final String USER_DOCUMENT_RETRIEVAL_COUNT_ID = "user_document_retrieval_count";

    public void incrementUserDocumentRetrieval() {
        publicotaurusBucket().counter(USER_DOCUMENT_RETRIEVAL_COUNT_ID, 1, 0);
    }

}
