package com.neota.workflowserver;

import com.neota.workflowserver.enums.SessionState;
import com.neota.workflowserver.json.model.*;
import com.neota.workflowserver.nodeaction.*;

public class Session {
    private final Workflow workflow;
    private Node currentNode;
    private Node nextNode;
    private Lane currentLane;
    private Lane nextLane;
    private SessionState state;
    private final String name;
    private NodeAction action;

    public Session(Workflow workflow, String name) {
        this.workflow = workflow;
        this.name = name;
        initializeSession();
    }

    private void initializeSession() {
        this.currentNode = WorkflowUtils.getStartNode(workflow);
        this.nextNode = WorkflowUtils.getNextNode(workflow, currentNode);
        this.currentLane = WorkflowUtils.getLaneOfNode(workflow, currentNode);
        this.nextLane = WorkflowUtils.getLaneOfNode(workflow, nextNode);
        this.state = SessionState.INITIALIZED;
    }

    public SessionState getState() {
        return state;
    }

    public void setState(SessionState state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    /* Starts or resumes the workflow session. */
    public void go() {
        state = SessionState.RUNNING;
        while (state == SessionState.RUNNING) {
            if (!processNextNode()) break;
        }
    }

    /* Processes the next node in the workflow and updates session state. */
    private boolean processNextNode() {
        if (nextNode instanceof EndNode) return completeSession();
        if (shouldStopForNewLane()) return stopSession();
        if (nextNode instanceof TimeoutNode) moveToNextLane(); // Skips to the next lane to start timer execution

        currentNode = nextNode;

        // Perform action based on the type of the current node
        action = NodeActionFactory.createNodeAction(currentNode, workflow, this);
        nextNode = action.performActionAndGetNextNode();
        nextLane = WorkflowUtils.getLaneOfNode(workflow, nextNode);

        return true;
    }

    private boolean completeSession() {
        state = SessionState.FINISHED;
        System.out.println("\rSession " + name + " completed.");
        return false;
    }

    private boolean shouldStopForNewLane() {
        return !currentLane.equals(nextLane) && !(nextNode instanceof TimeoutNode);
    }

    private boolean stopSession() {
        state = SessionState.STOPPED;
        moveToNextLane();
        return false;
    }
    
    private void moveToNextLane() {
    	currentLane = nextLane;
        System.out.println("\rSession " + name + ": Waiting on lane " + currentLane.getName());
    }

    /* Stops the timer and continues to the next node. */
    public void killTimerAndResume() {
        if (action instanceof TimeoutAction timeoutAction) {
            timeoutAction.killTimerAndResume();
        }
    }

    /* Returns a message based on the current state of the session. */
    public String getStateMessage() {
        return switch (state) {
            case INITIALIZED -> "\rInitialized.";
            case TIMER_ON -> "\rWaiting for resume or timeout on lane " + currentLane.getName() + ".";
            case RUNNING -> "\r" + WorkflowUtils.getNodeStatusMessage(currentNode);
            case STOPPED -> "\rWaiting to resume on lane " + currentLane.getName() + ".";
            case FINISHED -> "\rEnded.";
            default -> "";
        };
    }
}
