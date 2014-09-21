package com.xebia.couchbase.location;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class City {
    private String name;
    private int inhabitants;

    public City(String name, int inhabitants) {
        this.name = name;
        this.inhabitants = inhabitants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInhabitants() {
        return inhabitants;
    }

    public void setInhabitants(int inhabitants) {
        this.inhabitants = inhabitants;
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
        City rhs = (City) obj;
        return new EqualsBuilder()
                .append(this.name, rhs.name)
                .append(this.inhabitants, rhs.inhabitants)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(inhabitants)
                .toHashCode();
    }
}
