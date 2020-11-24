package com.mobiquity.test.steps;

import com.google.inject.Inject;
import com.mobiquity.test.domain.BlogUser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StepDefinitions {

    private final BlogUser blogUser;
    private static final Logger logger = LoggerFactory.getLogger(StepDefinitions.class);

    @Inject
    public StepDefinitions(BlogUser blogUser) {
        this.blogUser = blogUser;
    }

    @Given("I am user {string}")
    public void i_am_user(String username) {
        blogUser.setUsername(username);
    }

    @When("I search for posts written by me")
    public void i_search_for_posts_written_by_me() {
        logger.info(blogUser.getUsername());
    }

    @Then("I should see each comment has a valid email")
    public void i_should_see_each_comment_has_a_valid_email() {
        logger.info(blogUser.getUsername());
    }

}
