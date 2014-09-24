package com.xebia.couchbase.moderation;

import com.couchbase.client.protocol.views.Paginator;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.google.gson.Gson;
import com.xebia.couchbase.user.UserRepository;
import com.xebia.couchbase.user.UserWithCas;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserViewTest {

    private static final Gson gson = new Gson();
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        userRepository = new UserRepository();
    }

    @Test
    public void should_retrieve_user_from_moderator_point_of_view() throws Exception {
        // Given
        disableAnUser();

        // When
        final ViewResponse inactiveUserResponse = UserView.getInactiveUsers();

        // Then
        final ViewRow userViewRow = inactiveUserResponse.iterator().next();
        assertThat(userViewRow).isNotNull();
        assertThat(userViewRow.getKey()).isEqualTo("user::v1::adrian_vincent");
    }

    @Test
    public void should_paginate() throws Exception {
        final Paginator activeUserResponsePaginator = UserView.getPaginatedActiveUsers();

        // Then
        activeUserResponsePaginator.hasNext();
        ViewRow userViewRow = activeUserResponsePaginator.next().iterator().next();
        assertThat(userViewRow).isNotNull();
        assertThat(userViewRow.getKey()).isEqualTo("user::v1::aaliyah_scott");

        activeUserResponsePaginator.hasNext();
        userViewRow = activeUserResponsePaginator.next().iterator().next();
        assertThat(userViewRow).isNotNull();
        assertThat(userViewRow.getKey()).isEqualTo("user::v1::arjan_levine");
    }

    public void disableAnUser() throws IOException {
        final UserWithCas userWithCas = userRepository.findUserWithCas("user::v1::adrian_vincent");
        userWithCas.getUser().setActive(false);
        userRepository.updateUser("user::v1::adrian_vincent", userWithCas.getCasId(), userWithCas.getUser());
    }
}