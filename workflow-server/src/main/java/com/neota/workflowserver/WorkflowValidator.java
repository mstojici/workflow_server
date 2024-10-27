package com.neota.workflowserver;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.neota.workflowserver.model.EndNode;
import com.neota.workflowserver.model.Node;
import com.neota.workflowserver.model.StartNode;
import com.neota.workflowserver.model.Workflow;

public class WorkflowValidator {

    public static boolean validate(Workflow workflow) {
        boolean isStartNodeValid = validateSingleNodeOfType(workflow, StartNode.class, "Start Node");
        boolean isEndNodeValid = validateSingleNodeOfType(workflow, EndNode.class, "End Node");
        boolean isBranchingValid = validateNoBranching(workflow);
        boolean isAcyclicStructureValid = validateAcyclicStructure(workflow);

        return isStartNodeValid && isEndNodeValid && isBranchingValid && isAcyclicStructureValid;
    }

    private static <T> boolean validateSingleNodeOfType(Workflow workflow, Class<T> nodeType, String nodeTypeName) {
        boolean isValid = WorkflowUtils.countNodesOfType(workflow, nodeType) == 1;
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
    
    private static boolean validateAcyclicStructure(Workflow workflow) {
    	boolean isAcyclic = !detectCycles(workflow);
        if (!isAcyclic) {
            System.out.println("Workflow must be acyclic.");
        }
        return isAcyclic;
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
    
    // Pass through the workflow from the start node to check if any node will be visited twice
    private static boolean detectCycles(Workflow workflow) {
        StartNode startNode = WorkflowUtils.getStartNode(workflow);
        return isCycleDetected(workflow, startNode, new HashSet<>());
    }

    private static boolean isCycleDetected(Workflow workflow, Node currentNode, Set<Node> visitedNodes) {
        if (currentNode instanceof EndNode) {
            return false;
        }
              
    	if(visitedNodes.contains(currentNode)) {
    		return true;
    	}
    	
    	visitedNodes.add(currentNode);
        Node nextNode = WorkflowUtils.getNextNode(workflow, currentNode);
        return isCycleDetected(workflow, nextNode, visitedNodes);
    }
}
