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
    private int startIndex;
    private int documentsByPage;

    public UserView(int startIndex, int documentsByPage) {
        this.startIndex = startIndex;
        this.documentsByPage = documentsByPage;
    }

    //TODO Exercise 9
    public static ViewResult getInactiveUsers() {
        return Configuration.publicotaurusBucket().query(ViewQuery.from("moderator", "inactive_user").stale(Stale.FALSE));
    }

    public static UserView getPaginatedActiveUsers(int documentsByPage) {
        return new UserView(0, documentsByPage);
    }

    //TODO Exercise 10.2
    public Iterable<ViewRow> nextPage() {
        int currentIndex = startIndex++;
        return Configuration.publicotaurusBucket()
                .query(ViewQuery
                        .from("moderator", "active_user")
                        .skip(currentIndex * documentsByPage)
                        .stale(Stale.FALSE)
                        .limit(documentsByPage)).allRows();
    }

    //TODO Exercise 11
    public static void disableUser(String lastName) {
        final ViewResult phoenixKlineSingletonBefore = Configuration.publicotaurusBucket().query(
                ViewQuery.from("moderator", "user").key(lastName));

        final JsonDocument phoenixKline = Iterables.getFirst(phoenixKlineSingletonBefore.allRows(), null).document();
        phoenixKline.content().put("active", false);
        final UserRepository userRepository = new UserRepository();
        userRepository.updateUser(phoenixKline);
    }
}
