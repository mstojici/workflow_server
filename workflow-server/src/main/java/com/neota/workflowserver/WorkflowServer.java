package com.neota.workflowserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.neota.workflowserver.model.Workflow;

public class WorkflowServer {
    private static final Map<String, Workflow> workflows = new HashMap<>();
    private static final Map<String, Session> sessions = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("WorkflowServer is running.");
        try (Scanner scanner = new Scanner(System.in)) {
            String command;
            do {
                System.out.print("> ");
                command = scanner.nextLine();
            } while (processCommand(command));
        }
    }

    private static boolean processCommand(String command) {
        if (command.isBlank()) {
            return true;
        }

        switch (WorkflowUtils.getCommandTypeFromCommandString(command)) {
            case STOP:
                System.out.println("WorkflowServer stopped.");
                return false;
            case CREATE_WORKFLOW:
                handleCreateWorkflow(command);
                break;
            case START_SESSION:
                handleStartSession(command);
                break;
            case RESUME_SESSION:
                handleResumeSession(command);
                break;
            case SESSION_STATE:
                handleSessionState(command);
                break;
            case INVALID:
                System.out.println("Invalid command.");
                break;
        }
        return true;
    }

    private static void handleCreateWorkflow(String command) {
        String[] args = WorkflowUtils.parseCommandArgs(command, 3, 
                "create_workflow <workflow_name> <path_to_JSON_workflow>");

        String workflowName = args[1];
        String jsonFileName = args[2];

        WorkflowUtils.loadWorkflowFromFile(jsonFileName)
                .ifPresentOrElse(
                        workflow -> {
                            if (WorkflowValidator.validate(workflow)) {
                                workflows.put(workflowName, workflow);
                                System.out.println("Workflow " + workflowName  + " loaded.");
                            }
                        },
                        () -> System.out.println("Invalid JSON workflow definition.")
                );
    }

    private static void handleStartSession(String command) {
        String[] args = WorkflowUtils.parseCommandArgs(command, 3, 
                "start_session <session_name> <workflow_name>");

        String sessionName = args[1];
        String workflowName = args[2];
        Workflow workflow = workflows.get(workflowName);
        
        if(workflow == null)  {
        	System.out.println("Workflow " + workflowName + " does not exist.");
        	return;
        }
        
        Session session = new Session(workflow);
        sessions.put(sessionName, session);
        runSessionInNewThread(session);
        System.out.println("Session " + sessionName + " started.");
    }

    private static void handleResumeSession(String command) {
        String[] args = WorkflowUtils.parseCommandArgs(command, 2, "resume_session <session_name>");
        String sessionName = args[1];
        Session session = sessions.get(sessionName);
        
        if(session == null) {
			System.out.println("Session " + sessionName + " does not exist.");
			return;
        }
        if (session.getState().equals(SessionState.FINISHED)) {
        	System.out.println("Session " + sessionName + " is finished.");
        	return;
        }
        if (session.getState().equals(SessionState.RUNNING)) {
        	System.out.println("Session " + sessionName + " is already running.");
        	return;
        }
        
        runSessionInNewThread(session);
    	System.out.println("Resuming session " + sessionName + ".");
    }

    private static void handleSessionState(String command) {
        String[] args = WorkflowUtils.parseCommandArgs(command, 2, "session_state <session_name>");
        String sessionName = args[1];
        Session session = sessions.get(sessionName);
        
        if(session == null) {
			System.out.println("Session " + sessionName + " does not exist.");
			return;
        }
        
        System.out.println(session.getStateMessage());
    }


    // Start or continue a session in a new thread
    private static void runSessionInNewThread(Session session) {
        new Thread(session::go).start();
    }
}

