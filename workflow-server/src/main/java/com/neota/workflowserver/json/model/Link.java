package com.neota.workflowserver.json.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Link {
	private String id;
	@JsonProperty("from")
	private String fromNode;
	@JsonProperty("to")
	private String toNode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromNode() {
		return fromNode;
	}

	public void setFromNode(String fromNode) {
		this.fromNode = fromNode;
	}

	public String getToNode() {
		return toNode;
	}

	public void setToNode(String toNode) {
		this.toNode = toNode;
	}

	@Override
	public String toString() {
		return "Link{" + "id='" + id + '\'' + ", fromNode='" + fromNode + '\'' + ", toNode='" + toNode + '\'' + "}\n";
	}
}
