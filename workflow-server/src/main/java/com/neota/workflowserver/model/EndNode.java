package com.neota.workflowserver.model;

public class EndNode implements Node {
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
        return "EndNode{id='" + id + "'}\n";
    }
}
