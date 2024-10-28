package com.neota.workflowserver.nodeaction;

import com.neota.workflowserver.Session;
import com.neota.workflowserver.json.model.Node;
import com.neota.workflowserver.json.model.Workflow;

public abstract class NodeAction {
    protected Node node;
    protected Workflow workflow;
    protected Session session;

    public NodeAction(Node node, Workflow workflow, Session session) {
		super();
		this.node = node;
		this.workflow = workflow;
		this.session = session;
	}

	public abstract Node performActionAndGetNextNode();
}
