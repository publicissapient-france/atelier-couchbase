package com.xebia.couchbase.user;

import com.couchbase.client.core.RequestCancelledException;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.StringDocument;
import com.couchbase.client.java.error.CASMismatchException;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;
import com.google.gson.Gson;
import com.xebia.couchbase.batch.UserReaderFromCsv;
import com.xebia.couchbase.location.City;
import com.xebia.couchbase.location.Country;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collection;

import static com.xebia.couchbase.Configuration.publicotaurusBucket;
import static com.xebia.couchbase.location.AddressBuilder.anAddress;
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
    public void should_insert_user_in_database() throws Exception {
        final User user = anUser().withUserProfile(
                anUserProfile().withFirstName("Antoine").withLastName("Michaud").withSummary("Java Developer")
                        .withAddress(anAddress().withCity(new City("Paris", 1_000_000)).withCountry(new Country("France"))
                                .build()).build()).build();

        try {
            userRepository.insertUser(user);
        } catch (DocumentAlreadyExistsException e) {
            //TODO remove document at the end of the test => no more try catch
            // If the document already exists
        }
        final User resultUser = findUser("user::v1::antoine_michaud");
        resultUser.getUserProfile().setSummary("Java Developer");
        assertThat(resultUser).isEqualTo(user);
    }

    @Test
    public void should_update_with_an_optimistic_lock() throws Exception {
        final JsonDocument user1 = findDocument("user::v1::antoine_michaud");
        final JsonDocument user2 = findDocument("user::v1::antoine_michaud");
        user1.content().getObject("userProfile").put("summary", "Couchbase Developer");
        user2.content().getObject("userProfile").put("summary", "PHP Developer");

        userRepository.updateUser(user1);
        try {
            userRepository.updateUser(user2);
        } catch (CASMismatchException e) {
            //CASMismatchException is actually expected here
        }
        assertThat(user1.content().getObject("userProfile").get("summary")).isEqualTo("Couchbase Developer");
    }

    @Test
    public void should_count_number_of_document_retrieval() throws Exception {
        // Given

        final StringDocument userDocumentRetrievalCountDocument = publicotaurusBucket()
                .get("user_document_retrieval_count", StringDocument.class);

        Long userDocumentRetrievalCount = userDocumentRetrievalCountDocument != null
                ? parseLong(userDocumentRetrievalCountDocument.content())
                : 0L;

        // When
        userRepository.findUser("antoine_michaud");

        // Then
        final Long eventualUserDocumentRetrievalCount = parseLong(publicotaurusBucket()
                .get("user_document_retrieval_count", StringDocument.class)
                .content());
        assertThat(eventualUserDocumentRetrievalCount).isEqualTo(userDocumentRetrievalCount + 1);
    }

    @Test
    public void should_not_allow_read_during_edition() throws Exception {
        assertThat(userRepository.getAndLock("antoine_michaud")).isNotNull();
        assertThat(userRepository.getAndLock("antoine_michaud")).isNull();
    }

    @Test
    public void should_insert_a_bulk_of_users() throws Exception {
        final Collection<User> users = UserReaderFromCsv.getUsersFrom("users.csv");

        for (User user : users) {
            try {
                userRepository.insertUser(user);
            } catch (DocumentAlreadyExistsException | RequestCancelledException e) {
                // Good enough if user has already been inserted
            }
        }
    }

    private User findUser(String id) throws java.io.IOException {
        return gson.getAdapter(User.class).fromJson(
                findDocument(id).content().toString());
    }

    private JsonDocument findDocument(String id) {
        return publicotaurusBucket().get(id);
    }
}