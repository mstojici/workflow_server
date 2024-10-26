package com.neota.workflowserver.model;

public class Workflow {
    private Source source;

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Root{\\n" +
                "source=" + source +
                "}\n";
    }
}
