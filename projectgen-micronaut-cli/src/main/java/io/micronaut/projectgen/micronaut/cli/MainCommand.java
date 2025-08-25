package io.micronaut.projectgen.micronaut.cli;

import io.micronaut.configuration.picocli.PicocliRunner;
import picocli.CommandLine.Command;

@Command(
    name = "projectgen",
    mixinStandardHelpOptions = true,
    subcommands = {
        CreateCommand.class,
        AddFeatureCommand.class,
        UpdateCommand.class
    }
)
public class MainCommand implements Runnable {

    public static void main(String[] args) {
        PicocliRunner.run(MainCommand.class, args);
    }

    @Override
    public void run() {
        // This will be executed if no subcommand is specified
        System.out.println("Please specify a command. Use --help to see available commands.");
    }
}
