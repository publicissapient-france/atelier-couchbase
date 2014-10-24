package com.xebia.couchbase.moderation;

import com.couchbase.client.java.view.Stale;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.xebia.couchbase.Configuration;

public class UserView {

    //TODO Exercice 9.2 : Requêter les vues
    public static ViewResult getInactiveUsers() {
        return Configuration.publicotaurusBucket().query(ViewQuery.from("moderator", "inactive_user").stale(Stale.FALSE));
    }
}
