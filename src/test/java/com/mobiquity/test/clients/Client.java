package com.mobiquity.test.clients;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.restassured.RestAssured;

public class Client {

    private final String appURL;

    @Inject
    public Client(@Named("app.url") String appURL) {
        this.appURL = appURL;
    }

    public String getRequest(String path) {
        return RestAssured
                .given()
                .baseUri(appURL)
                .get(path)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
    }
}
