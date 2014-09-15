package com.xebia.couchbase.user;

import com.xebia.couchbase.location.AddressBuilder;
import com.xebia.couchbase.location.City;
import com.xebia.couchbase.location.Country;
import org.junit.Test;

public class UserRepositoryTest {

    @Test
    public void should_insert_user_in_database() throws Exception {
        final User user = UserBuilder.anUser().withIdentifier(1L).withUserProfile(UserProfileBuilder
                .anUserProfile().withFirstName("Antoine").withLastName("Michaud").withSummary("Java Developer")
                .withAddress(AddressBuilder.anAddress().withCity(new City("Paris", 1_000_000)).withCountry(new Country("France"))
                        .build()).build()).build();

        final UserRepository userRepository = new UserRepository();
        userRepository.saveUser(user);
    }
}