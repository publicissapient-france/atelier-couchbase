package com.xebia.couchbase.location;

public class AddressBuilder {
    private Country country;
    private City city;

    private AddressBuilder() {
    }

    public static AddressBuilder anAddress() {
        return new AddressBuilder();
    }

    public AddressBuilder withCountry(Country country) {
        this.country = country;
        return this;
    }

    public AddressBuilder withCity(City city) {
        this.city = city;
        return this;
    }

    public Address build() {
        Address address = new Address();
        address.setCountry(country);
        address.setCity(city);
        return address;
    }
}
