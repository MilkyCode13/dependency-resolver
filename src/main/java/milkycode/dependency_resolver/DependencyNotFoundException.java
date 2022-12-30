package milkycode.dependency_resolver;

import java.nio.file.Path;

public class DependencyNotFoundException extends Exception {
    public DependencyNotFoundException(Path dependentPath, Path dependencyPath) {
        super("Dependency of file '" + dependentPath + "' was not found: '" + dependencyPath + "'");
    }
}
