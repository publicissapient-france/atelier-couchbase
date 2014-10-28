package com.xebia.couchbase.user;

import com.couchbase.client.java.document.JsonLongDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.xebia.couchbase.Configuration.publicotaurusBucket;

public class CounterRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(CounterRepository.class);
    public static final String USER_DOCUMENT_RETRIEVAL_COUNT_ID = "user_document_retrieval_count";

    //TODO Exercice 5.1
    public void incrementUserDocumentRetrieval() {
        final JsonLongDocument counter = publicotaurusBucket().counter(USER_DOCUMENT_RETRIEVAL_COUNT_ID, 1, 0);
        LOGGER.info("Current counter value : {}", counter.content());
    }

}
