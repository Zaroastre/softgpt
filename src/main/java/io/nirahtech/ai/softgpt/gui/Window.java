package io.nirahtech.ai.softgpt.gui;

import javax.swing.JFrame;

public abstract class Window extends JFrame {

    protected Window(final String title) {
        super(title);
    }
    
    public final void display() {
        this.setVisible(true);
    }
}
