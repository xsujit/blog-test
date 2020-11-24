package com.mobiquity.test.validator;

import com.google.inject.Inject;
import com.mobiquity.test.domain.BlogComment;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.Assert;

public class Validator {

    private final BlogComment comments;

    @Inject
    public Validator(BlogComment comments) {
        this.comments = comments;
    }

    public void validateEmail() {
        comments.getComments().forEach(comment -> {
            String email = comment.getEmail();
            boolean isValid = EmailValidator.getInstance().isValid(email);
            Assert.assertTrue("Following comment has invalid email: " + comment, isValid);
        });
    }
}
