package com.xebia.couchbase.user;

public class UserWithCas {
    private User user;
    private Long casId;

    UserWithCas(User user, Long casId) {
        this.user = user;
        this.casId = casId;
    }

    public User getUser() {
        return user;
    }

    public Long getCasId() {
        return casId;
    }
}
