package com.mobiquity.test.domain;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mobiquity.test.clients.Client;
import com.mobiquity.test.models.user.User;
import io.cucumber.guice.ScenarioScoped;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.List;
import java.util.Objects;

@ScenarioScoped
@Slf4j
public class BlogUser {

    private final Client client;

    @Inject
    public BlogUser(Client client) {
        this.client = client;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(String username) {
        User user = getUserDetails(username);
        Assert.assertNotNull("No user exists with username: " + username, user);
        this.user = user;
    }

    private User getUserDetails(String username) {
        List<User> users = Objects.requireNonNull(getUsers());
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findAny()
                .orElse(null);
    }

    private List<User> getUsers() {
        String body = client.getRequest("users");
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(body, new TypeReference<List<User>>() {
            });
        } catch (JsonProcessingException e) {
            Assert.fail("Unable to map json to object: " + e.getMessage());
        }
        return null;
    }

}
