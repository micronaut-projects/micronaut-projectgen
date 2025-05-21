package io.micronaut.monolith;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@MicronautTest
class StylesheetsTest {
    @Test
    void stylesheetsCanBeResolved(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/assets/stylesheets/style.css")));
        assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/assets/stylesheets/treeview.css")));
        assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/assets/stylesheets/bootstrap-5.3.6/bootstrap.min.css")));
    }
}
