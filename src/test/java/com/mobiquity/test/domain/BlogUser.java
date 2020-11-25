package com.mobiquity.test.domain;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mobiquity.test.models.user.User;
import io.cucumber.guice.ScenarioScoped;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.List;

@ScenarioScoped
@Slf4j
public class BlogUser {

    private final String appURL;

    @Inject
    public BlogUser(@Named("app.url") String appURL) {
        this.appURL = appURL;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(String username) {
        User user = getUserDetails(username);
        Assert.assertNotNull("No user exists with username :: " + username, user);
        this.user = user;
    }

    private User getUserDetails(String username) {
        User user = null;
        try {
            user = getUsers().stream()
                    .filter(u -> u.getUsername().equalsIgnoreCase(username))
                    .findAny()
                    .orElse(null);
        } catch (JsonProcessingException e) {
            Assert.fail("Unable to map json to object :: " + e.getMessage());
        }
        return user;
    }

    private List<User> getUsers() throws JsonProcessingException {
        String body = RestAssured
                .given()
                .baseUri(appURL)
                .get("users")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(body, new TypeReference<List<User>>() {
        });
    }

}
