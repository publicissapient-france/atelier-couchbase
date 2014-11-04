package com.xebia.couchbase.user;

import com.xebia.couchbase.batch.UserReaderFromCsv;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AsyncUserRepositoryTest {

    private AsyncUserRepository asyncUserRepository = new AsyncUserRepository();
    private UserRepository userRepository = new UserRepository();

    @Test
    //Exercise 6
    public void should_insert_many_users() throws Exception {
        // Given
        final List<User> users = UserReaderFromCsv.getUsersFrom("users.csv");

        // When
        asyncUserRepository.insert(users);

        // Then
        for (User user : users) {
            assertThat(userRepository.findUser(
                    user.getUserProfile().getFirstName(),
                    user.getUserProfile().getLastName())).isNotNull();
        }
    }

}