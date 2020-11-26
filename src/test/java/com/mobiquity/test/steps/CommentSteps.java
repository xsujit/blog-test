package com.mobiquity.test.steps;

import com.google.inject.Inject;
import com.mobiquity.test.domain.BlogComment;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentSteps {

    private final BlogComment blogComment;

    @Inject
    public CommentSteps(BlogComment blogComment) {
        this.blogComment = blogComment;
    }

    @Then("I should see each comment has a valid email")
    public void i_should_see_each_comment_has_a_valid_email() {
        blogComment.setComments()
                .verifyCommentsArePresent()
                .validateAllEmails();
    }

}
