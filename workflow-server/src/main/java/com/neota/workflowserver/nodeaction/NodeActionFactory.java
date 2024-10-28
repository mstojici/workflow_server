package com.neota.workflowserver.nodeaction;

import com.neota.workflowserver.Session;
import com.neota.workflowserver.json.model.*;

public class NodeActionFactory {

    public static NodeAction createNodeAction(Node node, Workflow workflow, Session session) {
        if (node instanceof TaskNode) return new TaskAction(node, workflow, session);
        if (node instanceof NOP) return new NOPAction(node, workflow, session);
        if (node instanceof EmailNode) return new EmailAction(node, workflow, session);
        if (node instanceof TimeoutNode) return new TimeoutAction(node, workflow, session);
        return null;
    }
}