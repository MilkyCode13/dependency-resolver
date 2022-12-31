package milkycode.dependency_resolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An exception thrown if a cyclic dependency found in a dependency tree.
 */
public class CyclicDependencyException extends Exception {
    private final FileNode cycledNode;
    private final List<FileNode> cycledNodes = new ArrayList<>();
    private boolean finished = false;

    /**
     * Creates an exception.
     *
     * @param cycledNode The node on which the cyclic dependency was found.
     */
    public CyclicDependencyException(FileNode cycledNode) {
        this.cycledNode = cycledNode;
    }

    /**
     * Adds a node in the tree path to exception. If the end node was not encountered, the node is added to the list of
     * cycled nodes. Otherwise, it is ignored.
     *
     * @param node The file node to add.
     */
    public void addCycledNode(FileNode node) {
        if (finished) {
            return;
        }

        cycledNodes.add(node);
        if (node == cycledNode) {
            finished = true;
        }
    }

    /**
     * Gets the list of cycled file nodes.
     *
     * @return The list of cycled file nodes.
     */
    public List<FileNode> getCycledNodes() {
        return Collections.unmodifiableList(cycledNodes);
    }
}
