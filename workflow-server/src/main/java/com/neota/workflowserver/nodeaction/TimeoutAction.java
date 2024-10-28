package com.neota.workflowserver.nodeaction;

import com.neota.workflowserver.Session;
import com.neota.workflowserver.WorkflowUtils;
import com.neota.workflowserver.enums.SessionState;
import com.neota.workflowserver.json.model.Node;
import com.neota.workflowserver.json.model.TimeoutNode;
import com.neota.workflowserver.json.model.Workflow;

public class TimeoutAction extends NodeAction {
	private boolean timerKilled = false;

	public TimeoutAction(Node node, Workflow workflow, Session session) {
		super(node, workflow, session);
	}

	@Override
	public Node performActionAndGetNextNode() {
		int timeout = Integer.parseInt(((TimeoutNode) node).getTimeout());
		long timeToWait = System.currentTimeMillis() + timeout * 1_000;

    	session.setState(SessionState.TIMER_ON);
		
		while ((System.currentTimeMillis() < timeToWait) && !timerKilled) {
	        try {
	            Thread.sleep(1_000);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            e.printStackTrace();
	        }
		}
		
		session.setState(SessionState.RUNNING);
		
		if(timerKilled) {
			System.out.println("\rSession " + session.getName() + ": Timer killed, resuming...");
	        return WorkflowUtils.getNextNode(workflow, node);
		} else {
	        System.out.println("\rSession " + session.getName() + ": Timeout occured, proceeding with alternate task...");
	        return WorkflowUtils.getTimeoutNode(workflow, node);
		}
	}

	public void killTimerAndResume() {
		timerKilled = true;
	}
}
