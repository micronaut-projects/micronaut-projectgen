In the root of the project, build the `test-suite-helloworld-cli` module

```
./gradlew :test-suite-helloworld-cli:build
```

You need to publish to Maven Local the `test-suite-helloworld-openrewrite` which contains the
OpenRewrite recipes:

`./gradlew publishToMavenLocal`

create folder where you want to generate the project into.

For example:

```
mkdir demo
```

## Generate Project

Generate the project:

```
java -jar test-suite-helloworld-cli/build/libs/test-suite-helloworld-cli-0.1-all.jar create --output demo
```

If you check the project structure you will see something like this:

```
% tree demo
demo
├── build.gradle.kts
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── projectgen.properties
├── settings.gradle.kts
└── src
    └── main
        └── java
            └── com
                └── example
                    └── HelloWorld.java
```

## Add feature

Add feature to an existing project:

```
java -jar test-suite-helloworld-cli/build/libs/test-suite-helloworld-cli-0.1-all.jar add --project demo
```

This should run two OpenRewrite recipes (one declarative adding a dependency, one imperative creating a Java Test).

You should see an oputput such as:


```
All sources parsed, running active recipes: io.micronaut.projectgen.demo.junit-jupiter, io.micronaut.projectgen.demo.GenerateHelloWorldTestFile
Generated new file src/test/java/com/example/HelloWorldTest.java by:
    io.micronaut.projectgen.demo.GenerateHelloWorldTestFile
Changes have been made to demo/build.gradle.kts by:
    io.micronaut.projectgen.demo.junit-jupiter
        org.openrewrite.java.dependencies.AddDependency: {groupId=org.junit.jupiter, artifactId=junit-jupiter, version=5.10.2, configuration=testImplementation, scope=test}
Please review and commit the results.
Estimate time saved: 10m
```

After a success execution the project should look like (skipping the build folder)

```
% tree demo
demo
├── build.gradle.kts
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── projectgen.properties
├── settings.gradle.kts
└── src
    └── main
        └── java
            └── com
                └── example
                    └── HelloWorld.java
└── src
    └── test
        └── java
            └── com
                └── example
                    └── HelloWorldTest.java
```

You need to be able to run gradle tests:

```
cd demo
./gradlew test
```

You need to be able to run Maven tests:

```
cd demo
./mvnw test
```

















