package com.neota.workflowserver.json.model;

import java.util.Map;

public class Source {
	private Map<String, Node> nodes;
	private Map<String, Lane> lanes;
	private Map<String, Link> links;
	private Map<String, Link> timeoutlinks;

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

	public Map<String, Link> getTimeoutlinks() {
		return timeoutlinks;
	}

	public void setTimeoutlinks(Map<String, Link> timeoutlinks) {
		this.timeoutlinks = timeoutlinks;
	}

	@Override
	public String toString() {
		return "Source{\n" + 
				"nodes=\n" + 
				nodes + ", " + "lanes=\n" + 
				lanes + ", " + "links=\n" + 
				links + ", " + "timeoutlinks=\n" + 
				timeoutlinks + "}\n";
	}
}
