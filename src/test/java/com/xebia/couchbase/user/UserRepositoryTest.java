package com.xebia.couchbase.user;

import com.xebia.couchbase.batch.UserReaderFromCsv;
import com.xebia.couchbase.location.AddressBuilder;
import com.xebia.couchbase.location.City;
import com.xebia.couchbase.location.Country;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

public class UserRepositoryTest {

    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        userRepository = new UserRepository();
    }

    @Test
    public void should_insert_user_in_database() throws Exception {
        final User user = UserBuilder.anUser().withIdentifier(1L).withUserProfile(UserProfileBuilder
                .anUserProfile().withFirstName("Antoine").withLastName("Michaud").withSummary("Java Developer")
                .withAddress(AddressBuilder.anAddress().withCity(new City("Paris", 1_000_000)).withCountry(new Country("France"))
                        .build()).build()).build();

        userRepository.saveUser(user);
    }

    @Test
    public void should_insert_a_bulk_of_users() throws Exception {
        final Collection<User> users = UserReaderFromCsv.getUsersFrom("users.csv");

        for (User user : users) {
            userRepository.saveUser(user);
        }
    }
}