package com.mobiquity.test.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mobiquity.test.models.comment.Comment;
import com.mobiquity.test.models.post.Post;
import io.cucumber.guice.ScenarioScoped;
import io.restassured.RestAssured;
import org.json.JSONArray;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@ScenarioScoped
public class BlogComment {

    private final BlogPost blogPost;
    private final List<Comment> comments;

    @Inject
    public BlogComment(BlogPost blogPost) {
        this.blogPost = blogPost;
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
                Assert.fail("Unable to map json to object: " + e.getMessage());
            }
        }
    }

    private JSONArray fetchComments(int id) {
        String path = String.format("posts/%d/comments", id);
        String body = RestAssured.get(path)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
        return new JSONArray(body);
    }
}
