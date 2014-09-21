package com.xebia.couchbase.batch;

import com.xebia.couchbase.location.AddressBuilder;
import com.xebia.couchbase.location.City;
import com.xebia.couchbase.location.Country;
import com.xebia.couchbase.user.User;
import com.xebia.couchbase.user.UserBuilder;
import com.xebia.couchbase.user.UserProfileBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

public class UserReaderFromCsv {

    private static final Logger logger = LoggerFactory.getLogger(UserReaderFromCsv.class);

    public static Collection<User> getUsersFrom(String usersFileName) {
        final ArrayList<User> users = new ArrayList<>();

        try {
            final InputStream inputStream = UserReaderFromCsv.class.getResourceAsStream("/" + usersFileName);
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final Iterable<CSVRecord> lines = CSVFormat.DEFAULT.parse(inputStreamReader);

            for (CSVRecord line : lines) {
                users.add(UserBuilder.anUser()
                        .withUserProfile(UserProfileBuilder.anUserProfile()
                                .withFirstName(line.get(0))
                                .withLastName(line.get(1))
                                .withSummary("Java Developer")
                                .withAddress(AddressBuilder.anAddress()
                                        .withCountry(new Country("US"))
                                        .withCity(new City(line.get(2), Integer.parseInt(line.get(3))))
                                        .build()).build()).build());
            }
        } catch (IOException e) {
            logger.error(String.format("The file %s couldn't be read.", usersFileName), e);
        }

        return users;
    }
}
