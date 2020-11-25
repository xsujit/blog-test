package com.mobiquity.test.steps;

import com.google.inject.Inject;
import com.mobiquity.test.domain.BlogUser;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserSteps {

    private final BlogUser blogUser;

    @Inject
    public UserSteps(BlogUser blogUser) {
        this.blogUser = blogUser;
    }

    @Given("I am user {string}")
    public void i_am_user(String username) {
        blogUser.setUser(username);
    }

}
