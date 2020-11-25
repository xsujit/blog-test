package com.mobiquity.test.steps;

import com.google.inject.Inject;
import com.mobiquity.test.domain.BlogPost;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostSteps {

    private final BlogPost blogPost;

    @Inject
    public PostSteps(BlogPost blogPost) {
        this.blogPost = blogPost;
    }

    @When("I search for posts written by me")
    public void i_search_for_posts_written_by_me() {
        blogPost.setPosts();
    }

}
