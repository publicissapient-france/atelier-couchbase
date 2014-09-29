package com.xebia.couchbase.moderation;

import com.couchbase.client.protocol.views.*;
import com.google.gson.Gson;

import static com.xebia.couchbase.Configuration.PUBLICOTAURUS_CLIENT;

public class UserView {

    //TODO Exercice 9.1 : Récupérer les vues
    private static final View inactiveUserView = PUBLICOTAURUS_CLIENT.getView("moderator", "inactive_user");
    private static final View activeUserView = PUBLICOTAURUS_CLIENT.getView("moderator", "active_user");
    private static final Gson gson = new Gson();
    //TODO Exercice 10.1 : Stocker un attribut permettant d'itérer sur les résultats
    private Paginator activeUserResponsePaginator;

    //TODO Exercice 10.2 : Stocker un attribut permettant d'itérer sur les résultats
    public UserView(Paginator activeUserResponsePaginator) {
        this.activeUserResponsePaginator = activeUserResponsePaginator;
    }

    //TODO Exercice 9.2 : Requêter les vues
    public static ViewResponse getInactiveUsers() {
        final Query query = new Query();
        query.setStale(Stale.FALSE);
        return PUBLICOTAURUS_CLIENT.query(UserView.inactiveUserView, query);
    }

    //TODO Exercice 10.3
    public static UserView getPaginatedActiveUsers() {
        final Query query = new Query();
        query.setStale(Stale.FALSE);

        return new UserView(PUBLICOTAURUS_CLIENT.paginatedQuery(activeUserView, query, 100));
    }

    //TODO Exercice 10.4
    public Iterable<ViewRow> nextPage() {
        if (activeUserResponsePaginator.hasNext()) {
            return activeUserResponsePaginator.next();
        } else {
            return null;
        }
    }
}
