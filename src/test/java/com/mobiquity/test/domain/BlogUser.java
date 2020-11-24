package com.mobiquity.test.domain;

import io.cucumber.guice.ScenarioScoped;

@ScenarioScoped
public class BlogUser {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
