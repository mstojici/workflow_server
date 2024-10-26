package com.neota.workflowserver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neota.workflowserver.model.Workflow;

public class WorkflowServer {
	private static final Map<String, Workflow> workflows = new HashMap<>();

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String command;

		System.out.println("WorkflowServer is running.");

		do {
			System.out.print("> ");
			command = scanner.nextLine();
		} while (processCommand(command));

		scanner.close();
	}

	private static boolean processCommand(String command) {
		if (command.isBlank()) {
			return true;
		}

		switch (WorkflowUtils.getCommandTypeFromCommand(command)) {
		case STOP:
			System.out.println("WorkflowServer started.");
			return false;
		case CREATE_WORKFLOW:
			createWorkflow(command);
			break;
		case INVALID:
			System.out.println("Invalid command.");
		}
		return true;
	}

	private static void createWorkflow(String command) {
		Workflow workflow;

		String[] splitString = command.split("\\s+", 3);
		if (splitString.length < 3) {
			System.out.println("Syntax of create workflow command is:\n"
					+ "create_workflow <workflow_name> <path_to_JSON_workflow>");
			return;
		}
		String workflowName = splitString[1];
		String jsonFileName = splitString[2];

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			workflow = objectMapper.readValue(new File(jsonFileName), Workflow.class);
		} catch (IOException e) {
			System.out.println("Invalid JSON workflow definition:");
			System.out.println(e.getMessage());
			return;
		}

		if (WorkflowValidator.validate(workflow)) {
			workflows.put(workflowName, workflow);
			System.out.println(workflow);
		}

	}
}
