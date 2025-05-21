package io.micronaut.monolith;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.views.View;
import io.micronaut.views.fields.FormGenerator;
import io.micronaut.views.fields.messages.Message;

import java.util.Map;

@Controller
class DownloadFormController {
    public static final @NonNull Message MESSAGE_DOWNLOAD = Message.of("Download", "download");

    @Produces(MediaType.TEXT_HTML)
    @View("index.html")
    @Get
    Map<String, Object> index() {
        DownloadForm form = DownloadFormBuilder.builder()
            .name("demoapp")
            .build();
        return Map.of("form", form);
    }
}
