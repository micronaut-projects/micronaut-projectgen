package io.micronaut.projectgen.demo;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller
class HomeController {

    @Get
    @Produces(MediaType.TEXT_HTML)
    String index() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
            <title>Download Project</title>
            </head>
            <body>
            <form action="/api/v1/download/zip" method="POST">
                <fieldset>
                    <input type="hidden" name="name" value="demo"/>
                    <input type="hidden" name="packageName" value="com.example"/>
                    <input type="hidden" name="group" value="io.micronaut.projectgen"/>
                    <input type="hidden" name="artifact" value="demo-project"/>
                    <input type="hidden" name="version" value="1.0.0"/>
                    <input type="hidden" name="build" value="MAVEN,GRADLE"/>
                    <input type="hidden" name="gradleDsl" value="KOTLIN"/>
                    <input type="hidden" name="java" value="21"/>
                    <input type="checkbox" name="features" value="hello-world-test"/> Generate Test<br/>
                    <input type="submit" value="Download" />
                </fieldset>

            </form>
            </body>
            </html>
            """;
    }
}
