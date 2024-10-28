package com.neota.workflowserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.neota.workflowserver.enums.CommandType;
import com.neota.workflowserver.json.model.Workflow;

public class WorkflowServer {
    private static final Map<String, Workflow> workflows = new HashMap<>();
    private static final Map<String, Session> sessions = new HashMap<>();
    public static int nodeExecutionTimeSeconds = 60;

    public static void main(String[] args) {
        System.out.println("WorkflowServer is running.");
        try (Scanner scanner = new Scanner(System.in)) {
            runCommandLoop(scanner);
        }
    }

    /* The main loop */
    private static void runCommandLoop(Scanner scanner) {
        String command;
        do {
            System.out.print("> ");
            command = scanner.nextLine();
        } while (processCommand(command));
    }

    /* Parses and handles the command string from console */
    private static boolean processCommand(String command) {
        if (command.isBlank()) {
            return true;
        }

        CommandType commandType = WorkflowUtils.getCommandTypeFromCommandString(command);
        switch (commandType) {
            case STOP -> {
                System.out.println("\rWorkflowServer stopped.");
                return false;
            }
            case CREATE_WORKFLOW -> handleCreateWorkflow(command);
            case START_SESSION -> handleStartSession(command);
            case RESUME_SESSION -> handleResumeSession(command);
            case SESSION_STATE -> handleSessionState(command);
            case SET_EXEC_TIME -> handleSetExecTime(command);
            case INVALID -> System.out.println("\rInvalid command.");
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
                                System.out.println("\rWorkflow " + workflowName + " loaded.");
                            }
                        },
                        () -> System.out.println("\rInvalid JSON workflow definition.")
                );
    }

    private static void handleStartSession(String command) {
        String[] args = WorkflowUtils.parseCommandArgs(command, 3, 
                "start_session <session_name> <workflow_name>");

        String sessionName = args[1];
        String workflowName = args[2];
        Workflow workflow = workflows.get(workflowName);

        if (workflow == null) {
            System.out.println("\rWorkflow " + workflowName + " does not exist.");
            return;
        }

        Session session = new Session(workflow, sessionName);
        sessions.put(sessionName, session);
        startSessionThread(session);
        System.out.println("Session " + sessionName + " started.");
    }

    private static void handleResumeSession(String command) {
        String[] args = WorkflowUtils.parseCommandArgs(command, 2, "resume_session <session_name>");
        String sessionName = args[1];
        Session session = sessions.get(sessionName);

        if (session == null) {
            System.out.println("\rSession " + sessionName + " does not exist.");
            return;
        }
        switch (session.getState()) {
            case FINISHED -> System.out.println("\rSession " + sessionName + " is finished.");
            case RUNNING -> System.out.println("\rSession " + sessionName + " is already running.");
            case TIMER_ON -> session.killTimerAndResume();
            default -> {
                startSessionThread(session);
                System.out.println("\rResuming session " + sessionName + ".");
            }
        }
    }

    private static void handleSessionState(String command) {
        String[] args = WorkflowUtils.parseCommandArgs(command, 2, "session_state <session_name>");
        String sessionName = args[1];
        Session session = sessions.get(sessionName);

        if (session == null) {
            System.out.println("\rSession " + sessionName + " does not exist.");
            return;
        }

        System.out.println(session.getStateMessage());
    }

    private static void handleSetExecTime(String command) {
        String[] args = WorkflowUtils.parseCommandArgs(command, 2, "set_exec_time <time_in_seconds>");
        String timeString = args[1];

        try {
            nodeExecutionTimeSeconds = Integer.parseInt(timeString);
            System.out.println("\rExecution time set to " + nodeExecutionTimeSeconds + " seconds.");
        } catch (NumberFormatException e) {
            System.out.println("\rInvalid integer value: " + timeString);
        }
    }

    private static void startSessionThread(Session session) {
        new Thread(session::go).start();
    }
}
