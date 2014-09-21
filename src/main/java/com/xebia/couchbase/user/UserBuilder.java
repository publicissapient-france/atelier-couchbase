package com.xebia.couchbase.user;

public class UserBuilder {
    private UserProfile userProfile;
    private boolean active = true;

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder withUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public UserBuilder active() {
        this.active = true;
        return this;
    }

    public UserBuilder nonactive() {
        this.active = false;
        return this;
    }

    public User build() {
        User user = new User();
        user.setUserProfile(userProfile);
        user.setActive(active);
        return user;
    }
}
