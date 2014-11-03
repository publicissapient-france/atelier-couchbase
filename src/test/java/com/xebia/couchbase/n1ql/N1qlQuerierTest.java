package com.xebia.couchbase.n1ql;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class N1qlQuerierTest {

    @Test
    public void should_retrieve_most_present_cities() throws Exception {
        // Given
        final N1qlQuerier n1qlQuerier = new N1qlQuerier();

        // When
        final List<String> cityWithInhabitantsInitials = n1qlQuerier.getCityWithInhabitantsInitials("A", "W");

        // Then
        assertThat(cityWithInhabitantsInitials).containsOnly("San Antonio", "Houston");
    }

}