package com.neota.workflowserver;

import java.util.Optional;

import com.neota.workflowserver.model.EndNode;
import com.neota.workflowserver.model.Node;
import com.neota.workflowserver.model.StartNode;
import com.neota.workflowserver.model.Workflow;

public class WorkflowValidator {

    public static boolean validate(Workflow workflow) {
        boolean isStartNodeValid = validateSingleNodeOfType(workflow, StartNode.class, "Start Node");
        boolean isEndNodeValid = validateSingleNodeOfType(workflow, EndNode.class, "End Node");
        boolean isBranchingValid = validateNoBranching(workflow);

        return isStartNodeValid && isEndNodeValid && isBranchingValid;
    }

    private static <T> boolean validateSingleNodeOfType(Workflow workflow, Class<T> nodeType, String nodeTypeName) {
        boolean isValid = countNodesOfType(workflow, nodeType) == 1;
        if (!isValid) {
            System.out.println("There must be exactly one " + nodeTypeName + ".");
        }
        return isValid;
    }

    private static boolean validateNoBranching(Workflow workflow) {
        boolean hasBranching = findAnyNodeWithMultipleOutgoingLinks(workflow).isPresent();
        if (hasBranching) {
            System.out.println("There must be no branching.");
        }
        return !hasBranching;
    }

    private static <T> long countNodesOfType(Workflow workflow, Class<T> type) {
        return workflow.getSource().getNodes().values().stream()
                .filter(type::isInstance)
                .count();
    }

    // Find any node with more than one outgoing link
    private static Optional<Node> findAnyNodeWithMultipleOutgoingLinks(Workflow workflow) {
        return workflow.getSource().getNodes().values().stream()
                .filter(node -> hasMultipleOutgoingLinks(workflow, node))
                .findAny();
    }

    // Check if a node has multiple outgoing links
    private static boolean hasMultipleOutgoingLinks(Workflow workflow, Node node) {
        long outgoingLinkCount = workflow.getSource().getLinks().values().stream()
                .filter(link -> link.getFromNode().equals(node.getId()))
                .count();
        return outgoingLinkCount > 1;
    }
}
