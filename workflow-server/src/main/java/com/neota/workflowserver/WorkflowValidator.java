package com.neota.workflowserver;

import java.util.Optional;

import com.neota.workflowserver.model.EndNode;
import com.neota.workflowserver.model.Node;
import com.neota.workflowserver.model.StartNode;
import com.neota.workflowserver.model.Workflow;

public class WorkflowValidator {

	public static boolean validate(Workflow workflow) {
		boolean startNodesCountValid = countNodesOfType(workflow, StartNode.class) == 1;
		boolean endNodesCountValid = countNodesOfType(workflow, EndNode.class) == 1;
		boolean branching = findAnyNodeWithMultipleOutgoingLinks(workflow).isPresent();

		if (!startNodesCountValid) {
			System.out.println("There must be exactly one Start Node.");
		}
		if (!endNodesCountValid) {
			System.out.println("There must be exactly one End Node.");
		}
		if (branching) {
			System.out.println("There must be no branching.");
		}

		return startNodesCountValid && endNodesCountValid && !branching;
	}

	private static <T> long countNodesOfType(Workflow workflow, Class<T> type) {
		return workflow.getSource().getNodes().values().stream().filter(type::isInstance).count();
	}
	
	private static Optional<Node> findAnyNodeWithMultipleOutgoingLinks(Workflow workflow) {
	    return workflow.getSource().getNodes().values().stream()
	        .filter(node -> hasMultipleOutgoingLinks(workflow, node))
	        .findAny();
	}
	
	private static boolean hasMultipleOutgoingLinks(Workflow workflow, Node node) {
	    long count = workflow.getSource().getLinks().values().stream()
	        .filter(link -> link.getFromNode().equals(node.getId()))
	        .count();
	    return count > 1;
	}
	
}
