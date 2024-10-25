package com.neota.workflowserver.model;

public class NOP implements Node {
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
		return "NOP{id='" + id + "'}\n";
	}
}
