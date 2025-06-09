package io.micronaut.projectgen.core.utils;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.projectgen.core.options.OperatingSystem;

import static io.micronaut.projectgen.core.options.OperatingSystem.LINUX;
import static io.micronaut.projectgen.core.options.OperatingSystem.MACOS;
import static io.micronaut.projectgen.core.options.OperatingSystem.SOLARIS;
import static io.micronaut.projectgen.core.options.OperatingSystem.WINDOWS;

public class OperatingSystemUtils {
    private OperatingSystemUtils() {
    }

    @Nullable
    public static OperatingSystem getOperatingSystem() {
        io.micronaut.context.condition.OperatingSystem operatingSystem = io.micronaut.context.condition.OperatingSystem.getCurrent();
        if (operatingSystem.isMacOs()) {
            return MACOS;
        }
        if (operatingSystem.isLinux()) {
            return LINUX;
        }
        if (operatingSystem.isWindows()) {
            return WINDOWS;
        }
        if (operatingSystem.isSolaris()) {
            return SOLARIS;
        }
        return null;
    }
}
