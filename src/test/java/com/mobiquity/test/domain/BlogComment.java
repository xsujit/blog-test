package com.mobiquity.test.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mobiquity.test.models.comment.Comment;
import com.mobiquity.test.models.post.Post;
import io.cucumber.guice.ScenarioScoped;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

@ScenarioScoped
@Slf4j
public class BlogComment {

    private final BlogPost blogPost;
    private final BlogUser blogUser;
    private final String appURL;
    private final List<Comment> comments;

    @Inject
    public BlogComment(BlogPost blogPost, BlogUser blogUser, @Named("app.url") String appURL) {
        this.blogPost = blogPost;
        this.blogUser = blogUser;
        this.appURL = appURL;
        this.comments = new ArrayList<>();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments() {
        for (Post post : blogPost.getPosts()) {
            final Integer postId = post.getId();
            try {
                this.comments.addAll(fetchComments(postId));
            } catch (JsonProcessingException e) {
                Assert.fail("Unable to map json to object :: " + e.getMessage());
            }
        }
        final String username = blogUser.getUser().getUsername();
        log.info("Found {} comments under posts by user {}", comments.size(), username);
        Assert.assertFalse("There are no comments for any posts by user " + username, comments.isEmpty());
    }

    private List<Comment> fetchComments(int id) throws JsonProcessingException {
        String path = String.format("posts/%d/comments", id);
        String body = RestAssured
                .given()
                .baseUri(appURL)
                .get(path)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(body, new TypeReference<List<Comment>>() {
        });
    }
}
