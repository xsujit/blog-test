package com.mobiquity.test.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
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
    private final String appURL;
    private final List<Post> posts;

    @Inject
    public BlogPost(BlogUser blogUser, @Named("app.url") String appURL) {
        this.blogUser = blogUser;
        this.appURL = appURL;
        posts = new ArrayList<>();
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts() {
        JSONArray jsonArray = fetchPosts();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject post = jsonArray.getJSONObject(i);
            if (post.getInt("userId") == blogUser.getUser().getId()) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    posts.add(mapper.readValue(post.toString(), Post.class));
                } catch (JsonProcessingException e) {
                    Assert.fail("Unable to map json to object :: " + e.getMessage());
                }
            }
        }
        String msg = "There are no posts by the user " + blogUser.getUser().getUsername();
        Assert.assertFalse(msg, posts.isEmpty());
    }

    private JSONArray fetchPosts() {
        String body = RestAssured
                .given()
                .baseUri(appURL)
                .get("posts")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
        return new JSONArray(body);
    }
}
