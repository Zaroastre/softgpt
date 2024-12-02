package io.nirahtech.ai.softgpt;

import java.io.IOException;

import io.nirahtech.ai.softgpt.pipeline.Workflow;
import io.nirahtech.ai.softgpt.system.OllamaService;

public class Main {
    public static void main(String[] args) throws IOException {

        final OllamaService ollamaService = new OllamaService();
        ollamaService.useSudoBecomeMethod(true);
        if (!ollamaService.isInstalled()) {
            ollamaService.install();
            ollamaService.enable();
        }

        if (!ollamaService.isInstalled()) {
            throw new IOException("Service Ollama is not installed.");
        }
        if (!ollamaService.isRunning()) {
            ollamaService.start();
        }

        if (!ollamaService.isRunning()) {
            throw new IOException("Service Ollama is not running.");
        }

        final Workflow workflow = new Workflow();
        workflow.initialize();
        workflow.run();
    }
}