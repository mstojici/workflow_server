package com.neota.workflowserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neota.workflowserver.model.*;

public class WorkflowServer {

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

		switch (getCommandTypeFromCommand(command)) {
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
		int firstSpaceIndex = command.indexOf(' ');
		String jsonFileName = command.substring(firstSpaceIndex + 1);
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			Root root = objectMapper.readValue(new File(jsonFileName), Root.class);
			System.out.println(root);

		} catch (IOException e) {
			System.out.println("Invalid JSON workflow definition:");
			System.out.println(e.getMessage());
		}
	}

	private static CommandType getCommandTypeFromCommand(String command) {
		String commandTypeString = command.split("\\s+")[0].toUpperCase();
		return Arrays.stream(CommandType.values()).filter(a -> a.name().equals(commandTypeString)).findFirst()
				.orElse(CommandType.INVALID);
	}
}
