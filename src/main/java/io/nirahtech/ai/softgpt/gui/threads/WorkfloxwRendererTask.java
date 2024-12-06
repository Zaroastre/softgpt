package io.nirahtech.ai.softgpt.gui.threads;

import io.nirahtech.ai.softgpt.gui.Window;

public class WorkfloxwRendererTask implements Runnable {
    
    private final Window window;
    private final int framesPerSecond;
    private boolean isRendering;

    public WorkfloxwRendererTask(final Window window) {
        this.window = window;
        this.framesPerSecond = 30;
        this.isRendering = false;
    }

    private final void refreshWindow() {
        this.window.repaint();
        this.window.revalidate();
    }

    @Override
    public void run() {
        // this.isRendering = true;
        while (this.isRendering) {
            this.refreshWindow();
        }
        
    }
}
