package com.neota.workflowserver.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = StartNode.class, name = "StartNode"),
		@JsonSubTypes.Type(value = EndNode.class, name = "EndNode"),
		@JsonSubTypes.Type(value = TaskNode.class, name = "TaskNode"),
		@JsonSubTypes.Type(value = NOP.class, name = "NOP") })
public interface Node {
	public String getId();

	public void setId(String id);
}
