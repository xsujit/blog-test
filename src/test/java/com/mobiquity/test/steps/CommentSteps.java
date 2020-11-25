package com.mobiquity.test.steps;

import com.google.inject.Inject;
import com.mobiquity.test.domain.BlogComment;
import com.mobiquity.test.validator.Validator;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommentSteps {

    private final BlogComment blogComment;
    private final Validator validator;

    @Inject
    public CommentSteps(BlogComment blogComment, Validator validator) {
        this.blogComment = blogComment;
        this.validator = validator;
    }

    @Then("I should see each comment has a valid email")
    public void i_should_see_each_comment_has_a_valid_email() {
        blogComment.setComments();
        validator.validateEmail();
    }

}
