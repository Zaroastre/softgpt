package io.nirahtech.ai.softgpt.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.nirahtech.ai.softgpt.gui.threads.WorkfloxwRendererTask;
import io.nirahtech.ai.softgpt.pipeline.Workflow;
import io.nirahtech.ai.softgpt.system.OllamaService;

public final class ControllerWindow extends Window {

    private final Dimension size;

    private final UserChatPanel chatPanel;
    private final StepsPanel stepsPanel;
    private final WorkfloxwRendererTask workfloxwRendererTask;
    private final ExecutorService executorService;

    public ControllerWindow(final OllamaService ollamaService, final Workflow workflow) {
        super("Controller Window");
        Objects.requireNonNull(ollamaService);
        Objects.requireNonNull(workflow);

        this.setLayout(new BorderLayout());
        this.size = new Dimension(800, 600);
        this.setPreferredSize(size);
        this.setSize(size);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.chatPanel = new UserChatPanel();
        this.stepsPanel = new StepsPanel(workflow);

        this.chatPanel.setOnSendTextEventListener(workflow::accept);

        this.add(this.stepsPanel, BorderLayout.CENTER);
        this.add(this.chatPanel, BorderLayout.SOUTH);

        this.workfloxwRendererTask = new WorkfloxwRendererTask(this);
        this.executorService = Executors.newSingleThreadExecutor();
        this.executorService.submit(workfloxwRendererTask);
    }
}
