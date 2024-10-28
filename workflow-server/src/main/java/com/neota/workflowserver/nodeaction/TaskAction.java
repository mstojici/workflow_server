package com.neota.workflowserver.nodeaction;

import com.neota.workflowserver.Session;
import com.neota.workflowserver.WorkflowServer;
import com.neota.workflowserver.WorkflowUtils;
import com.neota.workflowserver.json.model.Node;
import com.neota.workflowserver.json.model.TaskNode;
import com.neota.workflowserver.json.model.Workflow;

public class TaskAction extends NodeAction {

	public TaskAction(Node node, Workflow workflow, Session session) {
		super(node, workflow, session);
	}

	@Override
	public Node performActionAndGetNextNode() {
        try {
            Thread.sleep(WorkflowServer.nodeExecutionTimeSeconds * 1_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        
        System.out.println("\rSession " + session.getName() + ": " + ((TaskNode) node).getName() + " done.");
        return WorkflowUtils.getNextNode(workflow, node);
	}

}
