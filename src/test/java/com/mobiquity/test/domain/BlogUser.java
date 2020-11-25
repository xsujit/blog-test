package com.mobiquity.test.domain;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiquity.test.models.user.User;
import io.cucumber.guice.ScenarioScoped;
import io.restassured.RestAssured;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

@ScenarioScoped
public class BlogUser {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(String username) {
        final User user = getMyDetails(username);
        Assert.assertNotNull("No user exists with username :: " + username,user);
        this.user = user;
    }

    private User getMyDetails(String username) {
        JSONArray users = getUsers();
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("username").equalsIgnoreCase(username)) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    return mapper.readValue(user.toString(), User.class);
                } catch (JsonProcessingException e) {
                    Assert.fail("Unable to map json to object: " + e.getMessage());
                }
            }
        }
        return null;
    }

    private JSONArray getUsers() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        String body = RestAssured.get("users")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
        return new JSONArray(body);
    }

}
