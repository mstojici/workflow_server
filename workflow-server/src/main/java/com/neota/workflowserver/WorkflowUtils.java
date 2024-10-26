package com.neota.workflowserver;

import java.util.Arrays;

import com.neota.workflowserver.model.EndNode;
import com.neota.workflowserver.model.StartNode;
import com.neota.workflowserver.model.Workflow;

public class WorkflowUtils {
	
	public static CommandType getCommandTypeFromCommand(String command) {
		String commandTypeString = command.split("\\s+", 2)[0].toUpperCase();
		return Arrays.stream(CommandType.values()).filter(a -> a.name().equals(commandTypeString)).findFirst()
				.orElse(CommandType.INVALID);
	}

	public static StartNode getStartNode(Workflow workflow) {
		return (StartNode) workflow.getSource().getNodes().values().stream().filter(StartNode.class::isInstance).findFirst().get();
	}
	
	public static StartNode getEndNode(Workflow workflow) {
		return (StartNode) workflow.getSource().getNodes().values().stream().filter(EndNode.class::isInstance).findFirst().get();
	}
}
