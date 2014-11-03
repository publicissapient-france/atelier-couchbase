package com.xebia.couchbase.user;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.StringDocument;
import com.couchbase.client.java.error.CASMismatchException;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.google.gson.Gson;
import com.xebia.couchbase.batch.UserReaderFromCsv;
import com.xebia.couchbase.location.City;
import com.xebia.couchbase.location.Country;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.xebia.couchbase.Configuration.publicotaurusBucket;
import static com.xebia.couchbase.location.AddressBuilder.anAddress;
import static com.xebia.couchbase.user.CounterRepository.USER_DOCUMENT_RETRIEVAL_COUNT_ID;
import static com.xebia.couchbase.user.UserBuilder.anUser;
import static com.xebia.couchbase.user.UserProfileBuilder.anUserProfile;
import static java.lang.Long.parseLong;
import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest {

    private static UserRepository userRepository;
    private static Gson gson;

    @BeforeClass
    public static void staticSetUp() throws Exception {
        userRepository = new UserRepository();
        gson = new Gson();
    }

    @Test
    //Exercise 3
    public void should_insert_user_and_retrieve_an_user_in_database() throws Exception {
        // Given
        final User user = anUser().withUserProfile(
                anUserProfile().withFirstName("Antoine").withLastName("Michaud").withSummary("Java Developer")
                        .withAddress(anAddress().withCity(new City("Paris", 1_000_000)).withCountry(new Country("France"))
                                .build()).build()).build();

        try {
            userRepository.insertUser(user);
        } catch (DocumentAlreadyExistsException e) {
            // If the document already exists
        }
        final JsonDocument resultDocument = userRepository.findUser("Antoine", "Michaud");

        assertThat(resultDocument).as("document from CouchBase is null, please implement userRepository.findUser().").isNotNull();

        final User resultUser = fromDocumentToUser(resultDocument);
        resultUser.getUserProfile().setSummary("Java Developer");
        assertThat(resultUser).isEqualTo(user);
        assertThat(resultDocument.id()).isEqualTo("user::antoine_michaud");
    }

    @Test(expected = CASMismatchException.class)
    //Exercise 4a
    public void should_update_with_an_optimistic_lock() throws Exception {
        // Given
        final JsonDocument user1 = userRepository.findUser("Antoine", "Michaud");
        final JsonDocument user2 = userRepository.findUser("Antoine", "Michaud");
        user1.content().getObject("userProfile").put("summary", "Couchbase Developer");
        user2.content().getObject("userProfile").put("summary", "PHP Developer");

        // When
        JsonDocument updatedUser1 = null;
        JsonDocument updatedUser2 = null;
        try {
            updatedUser1 = userRepository.updateUser(user1);
            updatedUser2 = userRepository.updateUser(user2);
        } finally {
            // Then
            assertThat(updatedUser1).isNotNull();
            assertThat(updatedUser2).isNull();
            assertThat(updatedUser1.content().getObject("userProfile").get("summary")).isEqualTo("Couchbase Developer");
        }
    }

    @Test
    //Exercise 4b
    public void should_not_allow_read_during_edition() {
        // Given
        final JsonDocument firstGetAndLock = userRepository.getAndLock("Antoine", "Michaud");

        // When
        final JsonDocument secondGetAndLock = userRepository.getAndLock("Antoine", "Michaud");

        // Then
        assertThat(firstGetAndLock).isNotNull();
        assertThat(secondGetAndLock).isNull();
    }

    @Test
    //Exercise 5
    public void should_count_number_of_document_retrieval() throws Exception {
        // Given
        final StringDocument userDocumentRetrievalCountDocument = publicotaurusBucket()
                .get(USER_DOCUMENT_RETRIEVAL_COUNT_ID, StringDocument.class);

        Long previousCount = previousCount(userDocumentRetrievalCountDocument);

        // When
        userRepository.findUser("Antoine", "Michaud");

        // Then
        final Long newDocumentRetrievalCount = parseLong(publicotaurusBucket()
                .get(USER_DOCUMENT_RETRIEVAL_COUNT_ID, StringDocument.class)
                .content());
        assertThat(newDocumentRetrievalCount).isEqualTo(previousCount + 1);
    }

    @Test
    //Exercise 6
    public void should_insert_a_bulk_of_users() throws Exception {
        final List<User> users = UserReaderFromCsv.getUsersFrom("users.csv");
        userRepository.insertBulkOfUsers(users);
    }

    private User fromDocumentToUser(JsonDocument userJsonDocument) throws IOException {
        return gson.getAdapter(User.class).fromJson(
                userJsonDocument.content().toString());
    }

    private long previousCount(StringDocument userDocumentRetrievalCountDocument) {
        return userDocumentRetrievalCountDocument != null
                ? parseLong(userDocumentRetrievalCountDocument.content())
                : -1L;
    }

}