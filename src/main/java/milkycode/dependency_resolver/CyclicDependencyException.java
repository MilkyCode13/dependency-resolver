package milkycode.dependency_resolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CyclicDependencyException extends Exception {
    private final FileNode cycledNode;
    private final List<FileNode> cycledNodes = new ArrayList<>();
    private boolean finished = false;

    public CyclicDependencyException(FileNode cycledNode) {
        this.cycledNode = cycledNode;
    }

    public void addCycledNode(FileNode node) {
        if (finished) {
            return;
        }

        cycledNodes.add(node);
        if (node == cycledNode) {
            finished = true;
        }
    }

    public List<FileNode> getCycledNodes() {
        return Collections.unmodifiableList(cycledNodes);
    }
}
