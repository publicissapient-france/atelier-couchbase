package com.xebia.couchbase.moderation;

import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.xebia.couchbase.user.UserRepository;
import com.xebia.couchbase.user.UserWithCas;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserViewTest {

    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        userRepository = new UserRepository();
    }

    @Test
    //Exercice 9
    public void should_retrieve_user_from_moderator_point_of_view() throws Exception {
        // Given
        disableAnUser("adrian_vincent");

        // When
        final ViewResponse inactiveUserResponse = UserView.getInactiveUsers();

        // Then
        final ViewRow userViewRow = inactiveUserResponse.iterator().next();
        assertThat(userViewRow).isNotNull();
        assertThat(userViewRow.getKey()).isEqualTo("VINCENT");
        assertThat(userViewRow.getValue()).isEqualTo("{\"userId\":\"adrian_vincent\"," +
                "\"userName\":\"ADRIAN VINCENT\",\"cityName\":\"San Diego\",\"active\":false}");
    }

    @Test
    //Exercice 10
    public void should_paginate() throws Exception {
        final UserView activeUserView = UserView.getPaginatedActiveUsers();

        // Then
        ViewRow userViewRow = activeUserView.nextPage().iterator().next();
        assertThat(userViewRow).isNotNull();
        assertThat(userViewRow.getKey()).isEqualTo("aaliyah_scott");

        userViewRow = activeUserView.nextPage().iterator().next();
        assertThat(userViewRow).isNotNull();
        assertThat(userViewRow.getKey()).isEqualTo("arielle_le");
    }

    public void disableAnUser(String userId) throws IOException {
        final UserWithCas userWithCas = userRepository.findUserWithCas(userId);
        userWithCas.getUser().setActive(false);
        userRepository.updateUser(userId, userWithCas.getCasId(), userWithCas.getUser());
    }
}