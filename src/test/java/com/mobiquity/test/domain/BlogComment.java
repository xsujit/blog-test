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
import org.json.JSONArray;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

@ScenarioScoped
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
        List<Post> posts = blogPost.getPosts();
        ObjectMapper mapper = new ObjectMapper();
        for (Post post : posts) {
            JSONArray ja = fetchComments(post.getId());
            try {
                List<Comment> comments = mapper.readValue(ja.toString(), new TypeReference<List<Comment>>() {
                });
                this.comments.addAll(comments);
            } catch (JsonProcessingException e) {
                Assert.fail("Unable to map json to object :: " + e.getMessage());
            }
        }
        String msg = "There are no comments for any posts by username :: " + blogUser.getUser().getUsername();
        Assert.assertFalse(msg, comments.isEmpty());
    }

    private JSONArray fetchComments(int id) {
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
        return new JSONArray(body);
    }
}
