package milkycode.dependency_resolver;

import java.nio.file.Path;

/**
 * An exception thrown if the dependency file was not found.
 */
public class DependencyNotFoundException extends Exception {
    /**
     * Creates an exception.
     *
     * @param dependentPath  The path of the file that is dependent on the other file.
     * @param dependencyPath The path of the dependency file.
     */
    public DependencyNotFoundException(Path dependentPath, Path dependencyPath) {
        super("Dependency of file '" + dependentPath + "' was not found: '" + dependencyPath + "'");
    }
}
