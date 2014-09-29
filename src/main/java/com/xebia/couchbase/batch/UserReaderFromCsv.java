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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserReaderFromCsv {

    private static final Logger logger = LoggerFactory.getLogger(UserReaderFromCsv.class);

    public static Collection<User> getUsersFrom(String usersFileName) {
        final List<User> users = new ArrayList<>();

        for (CSVRecord line : getCsvRecords(usersFileName)) {
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

        return users;
    }

    public static Collection<String> getKeysFrom(String usersFileName) {
        final List<String> keys = new ArrayList<>();

        for (CSVRecord line : getCsvRecords(usersFileName)) {
            keys.add("user::v1::" + line.get(0).toLowerCase() + "_" + line.get(1).toLowerCase());
        }

        return keys;
    }

    private static Iterable<CSVRecord> getCsvRecords(String usersFileName) {
        final InputStream inputStream = UserReaderFromCsv.class.getResourceAsStream("/" + usersFileName);
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        try {
            return CSVFormat.DEFAULT.parse(inputStreamReader);
        } catch (IOException e) {
            logger.error(String.format("The file %s couldn't be read.", usersFileName), e);
        }

        return null;
    }
}
