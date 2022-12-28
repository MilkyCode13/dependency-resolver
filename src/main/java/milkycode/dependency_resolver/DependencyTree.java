package milkycode.dependency_resolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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

    public static DependencyTree build(Path rootPath) throws IOException, DependencyNotFoundException {
        List<Path> paths = getValidPaths(rootPath);
        List<FileNode> nodes = new ArrayList<>(paths.stream().map(FileNode::new).toList());
        Map<Path, FileNode> map = nodes.stream().collect(Collectors.toMap(FileNode::getPath, Function.identity()));

        for (FileNode node : nodes) {
            List<Path> dependencyPaths = getDependencies(node.getPath());
            for (Path dependency : dependencyPaths) {
                FileNode dependencyNode = map.get(rootPath.resolve(dependency));
                if (dependencyNode == null) {
                    throw new DependencyNotFoundException();
                }
                node.addDependency(dependencyNode);
            }
        }

        return new DependencyTree(nodes);
    }

    private static List<Path> getValidPaths(Path path) throws IOException {
        try (Stream<Path> stream = Files.walk(path)) {
            return stream.filter(Files::isRegularFile).map(Path::normalize).toList();
        }
    }

    private static List<Path> getDependencies(Path path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            return reader.lines()
                    .map(requirePattern::matcher)
                    .filter(Matcher::matches)
                    .map(matcher -> Path.of(matcher.group(1)).normalize())
                    .toList();
        }
    }

    public List<FileNode> getOrderedList() throws CyclicDependencyException {
        List<FileNode> list = new ArrayList<>();

        walkTree(list);

        return Collections.unmodifiableList(list);
    }

    private Set<FileNode> getRootNodes() {
        Set<FileNode> rootNodes = new HashSet<>(nodes);

        for (FileNode node : nodes) {
            for (FileNode dependency : node.getDependencies()) {
                rootNodes.remove(dependency);
            }
        }

        return rootNodes;
    }

    private enum WalkStatus {
        UNDISCOVERED,
        DISCOVERED,
        VISITED
    }

    private void walkTree(List<FileNode> list) throws CyclicDependencyException {
        Set<FileNode> rootNodes = getRootNodes();

        if (rootNodes.isEmpty() && !nodes.isEmpty()) {
            throw new CyclicDependencyException();
        }

        Map<FileNode, WalkStatus> statusMap = new HashMap<>(nodes.stream().collect(Collectors.toMap(Function.identity(), fileNode -> WalkStatus.UNDISCOVERED)));

        for (FileNode node : rootNodes) {
            walkTree(list, statusMap, node);
        }
    }

    private void walkTree(List<FileNode> list, Map<FileNode, WalkStatus> statusMap, FileNode currentNode) throws CyclicDependencyException {
        if (statusMap.get(currentNode) == WalkStatus.VISITED) {
            return;
        }

        if (statusMap.get(currentNode) == WalkStatus.DISCOVERED) {
            throw new CyclicDependencyException();
        }

        statusMap.put(currentNode, WalkStatus.DISCOVERED);

        for (FileNode dependency : currentNode.getDependencies()) {
            walkTree(list, statusMap, dependency);
        }

        list.add(currentNode);
        statusMap.put(currentNode, WalkStatus.VISITED);
    }
}
