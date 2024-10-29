package com.neota.workflowserver;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neota.workflowserver.enums.CommandType;
import com.neota.workflowserver.json.model.EmailNode;
import com.neota.workflowserver.json.model.EndNode;
import com.neota.workflowserver.json.model.Lane;
import com.neota.workflowserver.json.model.Link;
import com.neota.workflowserver.json.model.NOP;
import com.neota.workflowserver.json.model.Node;
import com.neota.workflowserver.json.model.StartNode;
import com.neota.workflowserver.json.model.TaskNode;
import com.neota.workflowserver.json.model.Workflow;

import java.util.Optional;

public class WorkflowUtils {

    // Helper method to find the first enum that matches a string, or return a default
    private static <E extends Enum<E>> Optional<E> getEnumFromString(Class<E> enumType, String value) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst();
    }

    public static CommandType getCommandTypeFromCommandString(String command) {
        String commandTypeString = command.split("\\s+", 2)[0];
        return getEnumFromString(CommandType.class, commandTypeString)
                .orElse(CommandType.INVALID);
    }
    
    // Helper method to parse command arguments and check if minimum argument length is met
    public static String[] parseCommandArgs(String command, int requiredLength, String usage) {
        String[] args = command.split("\\s+");
        if (args.length < requiredLength) {
            System.out.println("\rSyntax of command is:\n" + usage);
            return null;
        }
        return args;
    }    

    // Load workflow from a JSON file and handles exceptions
    public static Optional<Workflow> loadWorkflowFromFile(String jsonFileName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return Optional.of(objectMapper.readValue(new File(jsonFileName), Workflow.class));
        } catch (IOException e) {
            System.out.println("\rError loading workflow from JSON file: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Retrieve a node of a specified type (StartNode, EndNode) from the workflow
    private static <T extends Node> Optional<T> getNodeOfType(Workflow workflow, Class<T> nodeType) {
        return workflow.getSource().getNodes().values().stream()
                .filter(nodeType::isInstance)
                .map(nodeType::cast)
                .findFirst();
    }

    public static <T> long countNodesOfType(Workflow workflow, Class<T> type) {
        return workflow.getSource().getNodes().values().stream()
                .filter(type::isInstance)
                .count();
    }

    public static StartNode getStartNode(Workflow workflow) {
        return getNodeOfType(workflow, StartNode.class)
                .orElseThrow(() -> new IllegalStateException("\rStartNode not found in workflow."));
    }

    public static EndNode getEndNode(Workflow workflow) {
        return getNodeOfType(workflow, EndNode.class)
                .orElseThrow(() -> new IllegalStateException("\rEndNode not found in workflow."));
    }

    // Find the lane containing a specific node in the workflow
    public static Lane getLaneOfNode(Workflow workflow, Node node) {
        return workflow.getSource().getLanes().values().stream()
                .filter(lane -> lane.getNodes().contains(node.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("\rLane for node not found in workflow."));
    }

    // Find the first link that starts from the specified node
    public static Link getNextLink(Workflow workflow, Node node) {
        return workflow.getSource().getLinks().values().stream()
                .filter(link -> link.getFromNode().equals(node.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("\rNext link for node not found in workflow."));
    }

    // Find the first timeout link that starts from the specified timeout node
    public static Link getTimeoutLink(Workflow workflow, Node node) {
        return workflow.getSource().getTimeoutlinks().values().stream()
                .filter(link -> link.getFromNode().equals(node.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("\rNext link for node not found in workflow."));
    }

    // Find the next node based on the next link from the specified node
    public static Node getNextNode(Workflow workflow, Node node) {
        Link nextLink = getNextLink(workflow, node);
        return Optional.ofNullable(workflow.getSource().getNodes().get(nextLink.getToNode()))
                .orElseThrow(() -> new IllegalStateException("\rNext node not found in workflow."));
    }

    // Find the next node based on the next timeout link from the specified timeout node
    public static Node getTimeoutNode(Workflow workflow, Node node) {
        Link nextLink = getTimeoutLink(workflow, node);
        return Optional.ofNullable(workflow.getSource().getNodes().get(nextLink.getToNode()))
                .orElseThrow(() -> new IllegalStateException("\rNext node not found in workflow."));
    }
    
    /* Returns a status message based on the type of the current node. */
    public static String getNodeStatusMessage(Node node) {
    	if (node instanceof TaskNode) return ((TaskNode)node).getName() + " in progress.";
    	if (node instanceof NOP) return ("NOP in progress.");
    	if (node instanceof EmailNode) return ("Sending e-mail to : " + ((EmailNode)node).getEmail());
        return "";
    }
}

