package com.neota.workflowserver.model;

public class StartNode implements Node {
	private String id;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "StartNode{id='" + id + "'}\n";
	}
}