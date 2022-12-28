package milkycode.dependency_resolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DependencyTree {
    private static final Pattern requirePattern = Pattern.compile("^require '(.*)'$");
    private final List<FileNode> nodes;

    private DependencyTree(List<FileNode> nodes) {
        this.nodes = nodes;
    }

    public List<FileNode> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public static List<Path> getValidPaths(Path path) throws IOException {
        try (Stream<Path> stream = Files.walk(path)) {
            return stream.filter(Files::isRegularFile).map(Path::normalize).toList();
        }
    }

    public static DependencyTree build(Path rootPath) throws IOException {
        List<Path> paths = getValidPaths(rootPath);
        List<FileNode> nodes = new ArrayList<>(paths.stream().map(FileNode::new).toList());
        Map<Path, FileNode> map = nodes.stream().collect(Collectors.toMap(FileNode::getPath, Function.identity()));

        for (FileNode node : nodes) {
            List<Path> dependencyPaths = getDependencies(node.getPath());
            for (Path dependency : dependencyPaths) {
                node.addDependency(map.get(rootPath.resolve(dependency)));
            }
        }

        return new DependencyTree(nodes);
    }

    public static List<Path> getDependencies(Path path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            return reader.lines()
                    .map(requirePattern::matcher)
                    .filter(Matcher::matches)
                    .map(matcher -> Path.of(matcher.group(1)).normalize())
                    .toList();
        }
    }
}
