package com.neota.workflowserver.model;

public class TaskNode implements Node {
	private String id;
	private String name;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "TaskNode{id='" + id + "', name='" + name + "'}\n";
	}
}
