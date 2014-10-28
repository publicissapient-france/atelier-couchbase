package com.xebia.couchbase.moderation;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.view.Stale;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.view.ViewRow;
import com.google.common.collect.Iterables;
import com.xebia.couchbase.Configuration;
import com.xebia.couchbase.user.UserRepository;

public class UserView {
    //TODO Exercise 10.1 : Store two attributes in order to iterates on result pages

    //TODO Exercise 9
    public static ViewResult getInactiveUsers() {
        return null;
    }

    //TODO Exercise 10.2
    public static UserView getPaginatedActiveUsers(int documentsByPage) {
        return null;
    }

    //TODO Exercise 10.3
    public Iterable<ViewRow> nextPage() {
        return null;
    }

    //TODO Exercise 11
    public static void disableUser(String lastName) {
    }
}
