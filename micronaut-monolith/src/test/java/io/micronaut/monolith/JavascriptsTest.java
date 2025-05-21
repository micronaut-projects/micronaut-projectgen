package io.micronaut.monolith;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@MicronautTest
class JavascriptsTest {
    @Test
    void stylesheetsCanBeResolved(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/assets/javascripts/treeview.js")));
        assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/assets/javascripts/htmx.2.0.4.min.js")));
        assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/assets/javascripts/bootstrap-5.3.6/bootstrap.bundle.min.js")));
    }
}
