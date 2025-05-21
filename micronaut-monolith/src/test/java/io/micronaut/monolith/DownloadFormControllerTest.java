package io.micronaut.monolith;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class DownloadFormControllerTest {

    @Test
    void downloadFormDisplayed(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/").accept(MediaType.TEXT_HTML);
        String html = assertDoesNotThrow(() -> client.retrieve(request));
        assertTrue(html.contains("Download"), html);
    }
}
