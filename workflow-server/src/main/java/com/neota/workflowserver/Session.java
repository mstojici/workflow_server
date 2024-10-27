package com.neota.workflowserver;

import com.neota.workflowserver.model.EndNode;
import com.neota.workflowserver.model.Lane;
import com.neota.workflowserver.model.NOP;
import com.neota.workflowserver.model.Node;
import com.neota.workflowserver.model.TaskNode;
import com.neota.workflowserver.model.Workflow;

public class Session {
    private final Workflow workflow;
    private Node currentNode;
    private Lane currentLane;
    private SessionState state;
    private String name;

    public Session(Workflow workflow, String name) {
        this.workflow = workflow;
        this.name = name;
        this.currentNode = WorkflowUtils.getStartNode(workflow);
        this.currentLane = WorkflowUtils.getLaneOfNode(workflow, currentNode);
        this.state = SessionState.INITIALIZED;
    }

    public SessionState getState() {
        return state;
    }

    // Used to start as well as to continue the session
    public void go() {
        state = SessionState.RUNNING;
        while (state == SessionState.RUNNING) {
            if (!processNextNode()) {
                break;
            }
            simulateSomeWork();
            printCurrentNodeDoneMessage();
        }
    }

    // Process the next node in the workflow, updating the session state
    private boolean processNextNode() {
        Node nextNode = WorkflowUtils.getNextNode(workflow, currentNode);
        Lane nextLane = WorkflowUtils.getLaneOfNode(workflow, nextNode);

        if (nextNode instanceof EndNode) {
            state = SessionState.FINISHED;
            System.out.println("Session " + name + " completed.");
            return false;
        }

        if (!currentLane.equals(nextLane)) {
            state = SessionState.STOPPED;
            currentLane = nextLane;
            return false;
        }

        currentNode = nextNode;
        return true;
    }

    // sleep to simulate node execution time
    private void simulateSomeWork() {
        try {
            Thread.sleep(WorkflowServer.nodeExecutionTimeSeconds * 1_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private void printCurrentNodeDoneMessage() {
    	System.out.print("Session " + name + ": ");
        if (currentNode instanceof TaskNode) {
            System.out.println(((TaskNode) currentNode).getName() + " done.");
        } else if (currentNode instanceof NOP) {
            System.out.println("NOP done.");
        }
    }

    public String getStateMessage() {
        return switch (state) {
            case INITIALIZED -> "Initialized.";
            case RUNNING -> getCurrentNodeStatusMessage();
            case STOPPED -> currentLane.getName();
            case FINISHED -> "Ended.";
            default -> "";
        };
    }

    // Returns a message based on the current node type
    private String getCurrentNodeStatusMessage() {
        return currentNode instanceof TaskNode
                ? ((TaskNode) currentNode).getName() + " in progress."
                : currentNode instanceof NOP
                ? "NOP in progress."
                : "";
    }
}
