package com.xebia.couchbase.moderation;

import com.couchbase.client.java.view.Stale;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.view.ViewRow;
import com.xebia.couchbase.Configuration;

public class UserView {

    //TODO Exercice 10.1 : Stocker un attribut permettant d'itérer sur les résultats
    private int startIndex;
    private int documentsByPage;

    public UserView(int startIndex, int documentsByPage) {
        this.startIndex = startIndex;
        this.documentsByPage = documentsByPage;
    }

    //TODO Exercice 9.2 : Requêter les vues
    public static ViewResult getInactiveUsers() {
        return Configuration.publicotaurusBucket().query(ViewQuery.from("moderator", "inactive_user").stale(Stale.FALSE));
    }

    public static UserView getPaginatedActiveUsers(int documentsByPage) {
        return new UserView(0, documentsByPage);
    }

    public Iterable<ViewRow> nextPage() {
        return Configuration.publicotaurusBucket()
                .query(ViewQuery
                        .from("moderator", "active_user")
                        .skip(startIndex++)
                        .stale(Stale.FALSE)
                        .limit(documentsByPage)).allRows();
    }
}
