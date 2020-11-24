package com.mobiquity.test.domain;

import io.cucumber.guice.ScenarioScoped;
import io.restassured.RestAssured;
import org.json.JSONArray;
import org.json.JSONObject;

@ScenarioScoped
public class BlogUser {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JSONObject getMyDetails() {
        JSONArray users = getUsers();
        for (int i = 0; i <users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("username").equalsIgnoreCase(username))
                return user;
        }
        return null;
    }

    private JSONArray getUsers() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        String body = RestAssured.get("/users")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
        return new JSONArray(body);
    }
}
