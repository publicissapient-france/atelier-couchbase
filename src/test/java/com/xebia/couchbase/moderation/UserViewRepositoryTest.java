package com.xebia.couchbase.moderation;

import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.xebia.couchbase.Configuration;
import org.junit.Test;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.x;
import static org.assertj.core.api.Assertions.assertThat;

public class UserViewRepositoryTest {
    @Test
    public void should_retrieve_user_from_moderator_point_of_view() throws Exception {
        // Given
        // When
        final ViewResult activeUsers =
                Configuration.publicotaurusBucket().query(ViewQuery.from("moderator", "inactive_user"));

        // Then
        assertThat(activeUsers.allRows().get(0)).isNotNull();
    }

    @Test
    public void should_retrieve_most_present_cities() throws Exception {
        // Given

        // When
        Configuration.publicotaurusBucket()
                .async()
                .query(
                        select("*").from(Configuration.PUBLICOTAURUS_BUCKET_NAME).where(x("active").is(x(false))))
        .doOnNext(result -> {
            if (!result.success()) {
                System.err.println(result.error());
            }
        });

        // Then

    }
}