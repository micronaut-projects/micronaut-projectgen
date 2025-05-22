package io.micronaut.monolith.controllers;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.views.View;

import java.util.Collections;
import java.util.Map;

@Controller
class HomeController {
    @Produces(MediaType.TEXT_HTML)
    @View("index.html")
    @Get
    Map<String, Object> index() {
        return Collections.emptyMap();
    }
}
