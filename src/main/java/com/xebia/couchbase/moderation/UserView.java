package com.xebia.couchbase.moderation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UserView {
    private String userId;
    private String userName;
    private String cityName;
    private boolean active;

    public UserView(String userId, String userName, String cityName, boolean active) {
        this.userId = userId;
        this.userName = userName;
        this.cityName = cityName;
        this.active = active;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        UserView rhs = (UserView) obj;
        return new EqualsBuilder()
                .append(this.userId, rhs.userId)
                .append(this.userName, rhs.userName)
                .append(this.cityName, rhs.cityName)
                .append(this.active, rhs.active)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(userId)
                .append(userName)
                .append(cityName)
                .append(active)
                .toHashCode();
    }
}
