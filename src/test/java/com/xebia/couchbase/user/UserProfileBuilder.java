package com.xebia.couchbase.user;

import com.xebia.couchbase.location.Address;

public class UserProfileBuilder {
    private String summary;
    private String firstName;
    private String lastName;
    private Address address;

    private UserProfileBuilder() {
    }

    public static UserProfileBuilder anUserProfile() {
        return new UserProfileBuilder();
    }

    public UserProfileBuilder withSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public UserProfileBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserProfileBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserProfileBuilder withAddress(Address address) {
        this.address = address;
        return this;
    }

    public UserProfile build() {
        UserProfile userProfile = new UserProfile();
        userProfile.setSummary(summary);
        userProfile.setFirstName(firstName);
        userProfile.setLastName(lastName);
        userProfile.setAddress(address);
        return userProfile;
    }
}
