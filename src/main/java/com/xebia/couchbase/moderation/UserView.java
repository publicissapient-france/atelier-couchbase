package com.xebia.couchbase.moderation;

import com.couchbase.client.protocol.views.*;

import static com.xebia.couchbase.Configuration.COUCHBASE_CLIENT;

public class UserView {

    private static final View inactiveUserView = COUCHBASE_CLIENT.getView("moderator", "inactive_user");
    private static final View activeUserView = COUCHBASE_CLIENT.getView("moderator", "active_user");

    public static ViewResponse getInactiveUsers() {
        final View inactiveUserView = UserView.inactiveUserView;
        final Query query = new Query();
        query.setStale(Stale.UPDATE_AFTER);
        return COUCHBASE_CLIENT.query(inactiveUserView, query);
    }

    public static Paginator getPaginatedActiveUsers() {
        final Query query = new Query();
        query.setStale(Stale.UPDATE_AFTER);
        return COUCHBASE_CLIENT.paginatedQuery(activeUserView, query, 100);
    }
}
