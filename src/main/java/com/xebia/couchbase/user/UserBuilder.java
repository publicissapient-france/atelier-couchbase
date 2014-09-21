package com.xebia.couchbase.user;

public class UserBuilder {
    private UserProfile userProfile;

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder withUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public User build() {
        User user = new User();
        user.setUserProfile(userProfile);
        return user;
    }
}
