package com.neota.workflowserver.json.model;

public class EmailNode implements Node {
	private String id;
	private String email;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "EmailNode{id='" + id + "', email='" + email + "'}\n";
	}
}
