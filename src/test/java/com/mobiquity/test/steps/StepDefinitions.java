package com.mobiquity.test.steps;

import com.google.inject.Inject;
import com.mobiquity.test.domain.BlogComment;
import com.mobiquity.test.domain.BlogPost;
import com.mobiquity.test.domain.BlogUser;
import com.mobiquity.test.models.comment.Comment;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StepDefinitions {

    private final BlogUser blogUser;
    private final BlogPost blogPost;
    private final BlogComment blogComment;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public StepDefinitions(BlogUser blogUser, BlogPost blogPost, BlogComment blogComment) {
        this.blogUser = blogUser;
        this.blogPost = blogPost;
        this.blogComment = blogComment;
    }

    @Given("I am user {string}")
    public void i_am_user(String username) {
        blogUser.setUser(username);
    }

    @When("I search for posts written by me")
    public void i_search_for_posts_written_by_me() {
        blogPost.setPosts();
    }

    @Then("I should see each comment has a valid email")
    public void i_should_see_each_comment_has_a_valid_email() {
        blogComment.setComments();
        List<Comment> comments = blogComment.getComments();
        logger.info(String.valueOf(comments.size()));
        comments.forEach(comment -> logger.info(comment.toString()));
    }

}
