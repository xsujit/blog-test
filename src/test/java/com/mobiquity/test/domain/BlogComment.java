package com.mobiquity.test.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mobiquity.test.clients.Client;
import com.mobiquity.test.models.comment.Comment;
import com.mobiquity.test.models.post.Post;
import io.cucumber.guice.ScenarioScoped;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ScenarioScoped
@Slf4j
public class BlogComment {

    private final BlogPost blogPost;
    private final BlogUser blogUser;
    private final Client client;
    private final List<Comment> comments;

    @Inject
    public BlogComment(BlogPost blogPost, BlogUser blogUser, Client client) {
        this.blogPost = blogPost;
        this.blogUser = blogUser;
        this.client = client;
        this.comments = new ArrayList<>();
    }

    public BlogComment setComments() {
        for (Post post : blogPost.getPosts()) {
            final int postId = post.getId();
            final List<Comment> comments = Objects.requireNonNull(fetchComments(postId));
            this.comments.addAll(comments);
        }
        return this;
    }

    public BlogComment verifyCommentsArePresent() {
        final String username = blogUser.getUser().getUsername();
        log.info("Found {} comments under posts by user {}", comments.size(), username);
        Assert.assertFalse("There are no comments for any posts by user: " + username, comments.isEmpty());
        return this;
    }

    private List<Comment> fetchComments(int id) {
        String path = String.format("posts/%d/comments", id);
        String body = client.getRequest(path);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(body, new TypeReference<List<Comment>>() {
            });
        } catch (JsonProcessingException e) {
            Assert.fail("Unable to map json to object: " + e.getMessage());
        }
        return null;
    }

    public void validateAllEmails() {
        comments.forEach(comment -> {
            String email = comment.getEmail();
            boolean isValid = EmailValidator.getInstance().isValid(email);
            Assert.assertTrue("Email is not valid: " + email, isValid);
        });
    }
}
