package io.nirahtech.ai.softgpt.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class WorkflowLoader {
    private WorkflowLoader() { }

    public static final Workflow load(final File workflowFile) throws IOException {
        return load(new FileInputStream(workflowFile));
    }
    
    public static final Workflow load(final InputStream workflowInputStream) {
        return null;
    }
}
