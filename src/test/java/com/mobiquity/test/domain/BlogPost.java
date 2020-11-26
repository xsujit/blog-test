package com.mobiquity.test.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mobiquity.test.clients.Client;
import com.mobiquity.test.models.post.Post;
import io.cucumber.guice.ScenarioScoped;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ScenarioScoped
@Slf4j
public class BlogPost {

    private final BlogUser blogUser;
    private final Client client;
    private final List<Post> posts;

    @Inject
    public BlogPost(BlogUser blogUser, Client client) {
        this.blogUser = blogUser;
        this.client = client;
        posts = new ArrayList<>();
    }

    public List<Post> getPosts() {
        return posts;
    }

    public BlogPost setPosts() {
        final List<Post> posts = Objects.requireNonNull(fetchPosts());
        for (Post post : posts)
            if (post.getUserId().equals(blogUser.getUser().getId()))
                this.posts.add(post);
        return this;
    }

    public void verifyCommentsArePresent() {
        final String username = blogUser.getUser().getUsername();
        log.info("Found {} posts by user {}", posts.size(), username);
        Assert.assertFalse("There are no posts by the user " + username, posts.isEmpty());
    }

    private List<Post> fetchPosts() {
        String body = client.getRequest("posts");
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(body, new TypeReference<List<Post>>() {
            });
        } catch (JsonProcessingException e) {
            Assert.fail("Unable to map json to object: " + e.getMessage());
        }
        return null;
    }
}
