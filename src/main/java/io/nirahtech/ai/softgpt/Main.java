package io.nirahtech.ai.softgpt;

import io.nirahtech.ai.softgpt.pipeline.Workflow;

public class Main {
    public static void main(String[] args) {
        final Workflow workflow = new Workflow();
        workflow.run();
    }
}