package com.xebia.couchbase.moderation;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.view.Stale;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.view.ViewRow;
import com.google.common.collect.Iterables;
import com.xebia.couchbase.user.UserRepository;

import static com.xebia.couchbase.Configuration.publicotaurusBucket;

public class UserView {

    //TODO Exercise 10.1 : Store two attributes in order to iterates on result pages

    //TODO Exercise 9
    public static ViewResult getInactiveUsers() {
        return null;
    }

    public static UserView getPaginatedActiveUsers(int documentsByPage) {
        return null;
    }

    //TODO Exercise 10.2
    public Iterable<ViewRow> nextPage() {
        return null;
    }

    //TODO Exercise 12
    public static void disableUser(String lastName) {
    }
}
