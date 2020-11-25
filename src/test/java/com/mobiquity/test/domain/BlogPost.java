package com.mobiquity.test.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mobiquity.test.models.post.Post;
import io.cucumber.guice.ScenarioScoped;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

@ScenarioScoped
@Slf4j
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
        try {
            for (Post post : fetchPosts())
                if (post.getUserId().equals(blogUser.getUser().getId()))
                    posts.add(post);
        } catch (JsonProcessingException e) {
            Assert.fail("Unable to map json to object :: " + e.getMessage());
        }
        final String username = blogUser.getUser().getUsername();
        log.info("Found {} posts by user {}", posts.size(), username);
        Assert.assertFalse("There are no posts by the user " + username, posts.isEmpty());
    }

    private List<Post> fetchPosts() throws JsonProcessingException {
        String body = RestAssured
                .given()
                .baseUri(appURL)
                .get("posts")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(body, new TypeReference<List<Post>>() {
        });
    }
}
