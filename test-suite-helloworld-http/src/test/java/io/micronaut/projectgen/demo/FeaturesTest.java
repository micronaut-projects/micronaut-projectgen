package io.micronaut.projectgen.demo;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.buildtools.gradle.GradleDsl;
import io.micronaut.projectgen.core.options.JdkVersion;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
class FeaturesTest {

    @Test
    void testFeaturesEndpoint(@Client("/") HttpClient httpClient) throws JSONException {
        BlockingHttpClient client = httpClient.toBlocking();

        Map<String, Object> form = new HashMap<>();
        form.put("name", "demo");
        form.put("packageName", "com.example");
        form.put("group", "io.micronaut.projectgen");
        form.put("artifact", "demo-project");
        form.put("version", "1.0.0");
        form.put("features", "hello-world-test");
        form.put("build", List.of(BuildTool.MAVEN, BuildTool.GRADLE));
        form.put("gradleDsl", GradleDsl.KOTLIN);
        form.put("java", JdkVersion.JDK_21);

        HttpRequest<?> request = HttpRequest.POST("/api/v1/features", form)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE);
        String json = assertDoesNotThrow(() -> client.retrieve(request));
        assertNotNull(json);
        String expected = """
            {
                "features":
                    [
                        {
                            "name":"hello-world-test",
                            "title":"Add Test",
                            "description":"Add Unit tests with Junit5 dependency",
                            "preview":false,
                            "community":false
                         }
                    ]
            }
            """;
        JSONAssert.assertEquals(
            expected, json, JSONCompareMode.LENIENT);
    }
}
