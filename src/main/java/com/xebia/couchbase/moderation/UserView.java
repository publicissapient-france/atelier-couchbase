package com.xebia.couchbase.moderation;

import com.couchbase.client.protocol.views.*;

import static com.xebia.couchbase.Configuration.PUBLICOTAURUS_CLIENT;

public class UserView {

    private static final View inactiveUserView = PUBLICOTAURUS_CLIENT.getView("moderator", "inactive_user");
    private static final View activeUserView = PUBLICOTAURUS_CLIENT.getView("moderator", "active_user");
    //TODO Exercice 10 : Stocker un attribut permettant d'itérer sur les résultats
    private Paginator activeUserResponsePaginator;

    //TODO Exercice 10 : Stocker un attribut permettant d'itérer sur les résultats
    public UserView(Paginator activeUserResponsePaginator) {
        this.activeUserResponsePaginator = activeUserResponsePaginator;
    }

    //TODO Exercice 9
    public static ViewResponse getInactiveUsers() {
        final View inactiveUserView = UserView.inactiveUserView;
        final Query query = new Query();
        query.setStale(Stale.UPDATE_AFTER);
        return PUBLICOTAURUS_CLIENT.query(inactiveUserView, query);
    }

    //TODO Exercice 10
    public static UserView getPaginatedActiveUsers() {
        final Query query = new Query();
        query.setStale(Stale.UPDATE_AFTER);

        return new UserView(PUBLICOTAURUS_CLIENT.paginatedQuery(activeUserView, query, 100));
    }

    public Iterable<ViewRow> nextPage() {
        if (activeUserResponsePaginator.hasNext()) {
            return activeUserResponsePaginator.next();
        } else {
            return null;
        }
    }
}
