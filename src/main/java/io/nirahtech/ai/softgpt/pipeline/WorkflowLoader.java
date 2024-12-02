package io.nirahtech.ai.softgpt.pipeline;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;

import io.nirahtech.ai.softgpt.pipeline.Workflow;

public final class WorkflowLoader {
    private WorkflowLoader() { }

    public static final Workflow load(final File workflowFile) {
        return load(new FileInputStream(workflowFile));
    }
    
    public static final Workflow load(final InputStream workflowInputStream) {
        final BufferedReader 
    }
}
