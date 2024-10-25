package com.neota.workflowserver.model;

import java.util.Map;

public class Source {
	private Map<String, Node> nodes;
	private Map<String, Lane> lanes;
	private Map<String, Link> links;

	public Map<String, Node> getNodes() {
		return nodes;
	}

	public void setNodes(Map<String, Node> nodes) {
		this.nodes = nodes;
	}

	public Map<String, Lane> getLanes() {
		return lanes;
	}

	public void setLanes(Map<String, Lane> lanes) {
		this.lanes = lanes;
	}

	public Map<String, Link> getLinks() {
		return links;
	}

	public void setLinks(Map<String, Link> links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "Source{\n" + 
				"nodes=\n" + 
				nodes + ", " + "lanes=\n" + 
				lanes + ", " + "links=\n" + 
				links + "}\n";
	}
}
