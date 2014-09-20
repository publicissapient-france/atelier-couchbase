package com.xebia.couchbase.user;

public class UserBuilder {
    private Long identifier;
    private UserProfile userProfile;

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder withIdentifier(Long identifier) {
        this.identifier = identifier;
        return this;
    }

    public UserBuilder withUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public User build() {
        User user = new User();
        user.setIdentifier(identifier);
        user.setUserProfile(userProfile);
        return user;
    }
}
