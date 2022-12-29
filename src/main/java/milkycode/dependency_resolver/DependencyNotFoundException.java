package milkycode.dependency_resolver;

import java.nio.file.Path;

public class DependencyNotFoundException extends Exception {
    public DependencyNotFoundException(Path dependencyPath) {
        super("Dependency was not found: " + dependencyPath);
    }
}
