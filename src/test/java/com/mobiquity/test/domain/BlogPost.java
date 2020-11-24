package com.mobiquity.test.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mobiquity.test.models.post.Post;
import io.cucumber.guice.ScenarioScoped;
import io.restassured.RestAssured;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

@ScenarioScoped
public class BlogPost {

    private final BlogUser blogUser;

    @Inject
    public BlogPost(BlogUser blogUser) {
        this.blogUser = blogUser;
    }

    public List<Post> getMyPosts() {
        JSONArray jsonArray = getPosts();
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject post = jsonArray.getJSONObject(i);
            if (post.getInt("userId") == blogUser.getUser().getId()) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    posts.add(mapper.readValue(post.toString(), Post.class));
                } catch (JsonProcessingException e) {
                    Assert.fail("Unable to map json to object: " + e.getMessage());
                }
            }
        }
        return posts;
    }

    private JSONArray getPosts() {
        String body = RestAssured.get("posts")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
        return new JSONArray(body);
    }
}
