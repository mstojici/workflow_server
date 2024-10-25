package com.neota.workflowserver.model;

import java.util.List;

public class Lane {
    private String id;
    private String name;
    private List<String> nodes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "Lane{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nodes=" + nodes +
                "}\n";
    }
}

