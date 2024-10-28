package com.neota.workflowserver.json.model;

public class TimeoutNode implements Node {
	private String id;
	private String timeout;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	@Override
	public String toString() {
		return "TimeoutNode{id='" + id + "', timeout='" + timeout + "'}\n";
	}
}
